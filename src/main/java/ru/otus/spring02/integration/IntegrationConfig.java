package ru.otus.spring02.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel inBookChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public MessageChannel outBookChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow bookFlow() {
        return IntegrationFlows.from("inBookChannel")
                .handle("libraryServiceImpl", "addNewBook")
                .handle("randomCommentServiceImpl", "addRandomComment")
                .channel("outBookChannel")
                .get();
    }
}
