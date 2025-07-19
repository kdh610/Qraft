package Qraft.newsalert.infrastructure.adapter.out;

import Qraft.newsalert.application.port.out.MessageProducerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
public class InMemoryProducerAdapter implements MessageProducerPort {


    private final LinkedBlockingQueue<String> inMemoryMessageQueue;

    @Override
    public void send(String message) {
        inMemoryMessageQueue.offer(message);
    }

}
