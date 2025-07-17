package com.katech.service.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.cache")
@Getter
@Setter
public class CachingProperties {

    private Long ttl;
}
