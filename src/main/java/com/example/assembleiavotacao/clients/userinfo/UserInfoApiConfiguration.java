package com.example.assembleiavotacao.clients.userinfo;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class UserInfoApiConfiguration {

    @Value("${userinfo.api.url}")
    private String url;

    private final Client client;

    @Bean
    UserInfoApiClient getUserInfoApiClient() {

        return Feign.builder()
                .client(client)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(UserInfoApiClient.class))
                .logLevel(Logger.Level.BASIC)
                .retryer(Retryer.NEVER_RETRY)
                .target(UserInfoApiClient.class, url);
    }
}
