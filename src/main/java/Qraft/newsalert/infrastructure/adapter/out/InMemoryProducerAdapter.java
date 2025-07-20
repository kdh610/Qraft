package Qraft.newsalert.infrastructure.adapter.out;

import Qraft.newsalert.application.port.out.MessageProducerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 뉴스ID 메시지를 인메모리 큐에 저장하는 역할을 합니다.
 */
@Component
@RequiredArgsConstructor
public class InMemoryProducerAdapter implements MessageProducerPort {

    private final LinkedBlockingQueue<String> inMemoryMessageQueue;

    @Override
    public void send(String message) {
        inMemoryMessageQueue.offer(message);
    }

}
