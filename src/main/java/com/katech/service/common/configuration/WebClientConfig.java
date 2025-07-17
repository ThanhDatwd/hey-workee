package com.katech.service.common.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClientProperties webClientProperties;

    @Bean
    @Primary
    public ReactorClientHttpConnector reactorClientHttpConnector() {
        TcpClient tcpClient =
                TcpClient.create()
                        .option(
                                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                webClientProperties.getConnectionTimeOut())
                        .doOnConnected(
                                c ->
                                        c.addHandlerLast(
                                                        new ReadTimeoutHandler(
                                                                webClientProperties
                                                                        .getConnectionTimeOut()))
                                                .addHandlerLast(
                                                        new WriteTimeoutHandler(
                                                                webClientProperties
                                                                        .getConnectionTimeOut())));
        return new ReactorClientHttpConnector(HttpClient.from(tcpClient));
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("customConnectionPool")
                .maxConnections(webClientProperties.getMaxConnection())
                .pendingAcquireMaxCount(webClientProperties.getPendingAcquireMaxCount())
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder(
            ReactorClientHttpConnector reactorClientHttpConnector) {
        return WebClient.builder().clientConnector(reactorClientHttpConnector);
    }

    @Bean
    public WebClient webclientBuilder(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}
