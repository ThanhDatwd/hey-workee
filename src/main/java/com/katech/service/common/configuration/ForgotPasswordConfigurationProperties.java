package com.katech.service.common.configuration;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "forgot-password")
@Getter
@Setter
public class ForgotPasswordConfigurationProperties {

    private Map<String, AppConfig> appConfigs;

    @Getter
    @Setter
    public static class AppConfig {

        private String baseUrl;

        private String successRedirectEndpoint;

        private String failedRedirectEndpoint;

        // In minute
        private Integer tokenExpiry;
    }
}
