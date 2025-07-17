package com.katech.service.customer.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {
        "/api/customers/register",
        "api/worker/**",    // 👈 thêm dòng này
        "/api/**", // cái này vẫn giữ nếu bạn muốn
        "/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(
                        cors ->
                                cors.configurationSource(
                                        request -> {
                                            CorsConfiguration configuration =
                                                    new CorsConfiguration();
                                            configuration.setAllowedMethods(List.of("*"));
                                            configuration.setAllowedHeaders(List.of("*"));
                                            configuration.setAllowedOrigins(List.of("*"));
                                            //
                                            // configuration.setAllowedOrigins(List.of("http://192.168.68.106:5173"));
                                            //
                                            // configuration.setAllowCredentials(true);
                                            return configuration;
                                        }));

        return http.build();
    }
}
