package Qraft.newsalert.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class InMemoryQueueConfig {

    @Bean
    public LinkedBlockingQueue<String> inMemoryMessageQueue() {
        return new LinkedBlockingQueue<>();
    }

}
