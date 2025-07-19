package Qraft.newsalert.infrastructure.adapter.in;

import Qraft.newsalert.application.port.in.MessageConsumerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
public class InMemoryConsumerAdapter implements MessageConsumerPort {

    private final LinkedBlockingQueue<String> inMemoryMessageQueue;

    @Override
    public String receiveMessage() {
        return inMemoryMessageQueue.poll();
    }
}
