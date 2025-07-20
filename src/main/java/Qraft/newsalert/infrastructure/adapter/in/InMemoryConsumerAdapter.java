package Qraft.newsalert.infrastructure.adapter.in;

import Qraft.newsalert.application.port.in.MessageConsumerPort;
import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import Qraft.newsalert.infrastructure.websocket.WebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryConsumerAdapter implements MessageConsumerPort {

    private final LinkedBlockingQueue<String> inMemoryMessageQueue;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final NewsRepository newsRepository;
    private final WebSocketHandler webSocketHandler;

    @Override
    @PostConstruct
    public void receiveMessage() {

        executorService.submit(() -> {
           while(!Thread.currentThread().isInterrupted()) {
               try {
                   String newsId = inMemoryMessageQueue.take();
                   News news = newsRepository.findById(newsId)
                           .orElseThrow(() -> new RuntimeException("News not found with id: " + newsId));
                   log.info("Received news: {}", news);
                    webSocketHandler.broadcastNewsUpdate(news);

               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

    }
}
