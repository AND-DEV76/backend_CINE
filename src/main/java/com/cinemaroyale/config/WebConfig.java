package com.cinemaroyale.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Ya no necesitamos servir archivos estáticos locales.
    // Las imágenes ahora se cargan directamente desde Cloudinary CDN.

}