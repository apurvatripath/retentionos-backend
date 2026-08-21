package com.retentionos.backend.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BillStorageConfig implements WebMvcConfigurer {

    @Value("${app.bills.storage-path:bills}")
    private String storagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Paths.get(storagePath).toAbsolutePath().toString().replace("\\", "/") + "/";
        registry.addResourceHandler("/bills/**").addResourceLocations(location);
    }
}
