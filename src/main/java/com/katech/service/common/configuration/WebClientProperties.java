package com.katech.service.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "webclient")
@Getter
@Setter
public class WebClientProperties {

    private String notificationPath;

    private String templatePath;

    private int maxConnection;

    private int pendingAcquireMaxCount;

    private int connectionTimeOut;
}
