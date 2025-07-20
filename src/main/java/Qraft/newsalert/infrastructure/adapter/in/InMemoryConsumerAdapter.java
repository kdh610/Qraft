package Qraft.newsalert.infrastructure.adapter.in;

import Qraft.newsalert.application.port.in.MessageConsumerPort;
import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import Qraft.newsalert.exception.BaseException;
import Qraft.newsalert.exception.Code;
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

    /**
     * 메세지 큐에서 뉴스를 가져와 웹소켓을 통해 브로드캐스트합니다.
     */
    @Override
    @PostConstruct
    public void receiveMessage() {
        executorService.submit(() -> {
           while(!Thread.currentThread().isInterrupted()) {
               try {
                   String newsId = inMemoryMessageQueue.take();
                   News news = newsRepository.findById(newsId)
                           .orElseThrow(() -> new BaseException(Code.NEWS_NOT_FOUND));

                    webSocketHandler.broadcastNewsUpdate(news);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               } catch (BaseException e) {
                   log.error("Error processing message: {}", e.getMessage());
               }
           }
        });

    }
}
