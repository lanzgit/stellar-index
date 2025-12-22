package br.edu.infnet.stellarindexapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api.nasa")
public class NasaApiConfig {
    private String baseUrl;
    private String apiKey;
}