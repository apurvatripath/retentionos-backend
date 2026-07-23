package com.retentionos.backend.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.openpdf.text.Anchor;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.retentionos.backend.dto.BillItemRequest;
import com.retentionos.backend.entity.Business;
import com.retentionos.backend.entity.Customer;

@Service
public class BillPdfService {

    private static final Color BRAND_GREEN = new Color(31, 111, 84);

    @Value("${app.bills.storage-path:bills}")
    private String storagePath;

    public String generateBillPdf(Business business, Customer customer, List<BillItemRequest> items, double total, String signupUrl) {
        byte[] pdfBytes = buildPdfBytes(business, customer, items, total, signupUrl);

        String filename = UUID.randomUUID() + ".pdf";
        try {
            Path dir = Paths.get(storagePath);
            Files.createDirectories(dir);
            Files.write(dir.resolve(filename), pdfBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save bill PDF", e);
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/bills/")
                .path(filename)
                .build()
                .toUriString();
    }

    private byte[] buildPdfBytes(Business business, Customer customer, List<BillItemRequest> items, double total, String signupUrl) {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BRAND_GREEN);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

            Paragraph title = new Paragraph(business.getName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph customerLine = new Paragraph("Bill for: " + customer.getName(), normalFont);
            customerLine.setSpacingBefore(16);
            customerLine.setSpacingAfter(12);
            document.add(customerLine);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 2, 2});

            table.addCell(headerCell("Item", headerFont));
            table.addCell(headerCell("Qty", headerFont));
            table.addCell(headerCell("Price", headerFont));
            table.addCell(headerCell("Line Total", headerFont));

            for (BillItemRequest item : items) {
                double lineTotal = item.price() * item.quantity();
                table.addCell(new Phrase(item.name(), normalFont));
                table.addCell(new Phrase(String.valueOf(item.quantity()), normalFont));
                table.addCell(new Phrase(String.format("Rs.%.2f", item.price()), normalFont));
                table.addCell(new Phrase(String.format("Rs.%.2f", lineTotal), normalFont));
            }

            document.add(table);

            Paragraph totalPara = new Paragraph("Total: Rs." + String.format("%.2f", total), boldFont);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            totalPara.setSpacingBefore(12);
            document.add(totalPara);

            Paragraph thanks = new Paragraph(
                    "Thank you for visiting " + business.getName() + "! We hope to see you again soon.",
                    normalFont
            );
            thanks.setSpacingBefore(24);
            document.add(thanks);

            Anchor signupAnchor = new Anchor("Visit us again: " + signupUrl, normalFont);
            signupAnchor.setReference(signupUrl);
            Paragraph signupPara = new Paragraph();
            signupPara.setSpacingBefore(10);
            signupPara.add(signupAnchor);
            document.add(signupPara);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate bill PDF", e);
        }

        return out.toByteArray();
    }

    private PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BRAND_GREEN);
        cell.setPadding(6);
        return cell;
    }
}
