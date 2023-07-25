package com.amigoscode.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

   @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowOrigins;

    @Value("#{'${cors.allowed-methods}'.split(',')}")
    private List<String> allowMethods;

    @Value("#{'${cors.allowed-headers}'.split(',')}")
    private List<String> allowHeaders;

    @Value("#{'${cors.exposed-headers}'.split(',')}")
    private List<String> expectedHeader;
    

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowOrigins);
        configuration.setAllowedMethods(allowMethods);
        configuration.setAllowedHeaders(allowHeaders);
        configuration.setExposedHeaders(expectedHeader);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
