package Qraft.newsalert.infrastructure.adapter.in;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class LinkedBlockingQueueConsumer implements MessageConsumer {

    private final LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<>();

    @Override
    public String receiveMessage() {
        return queue.poll();
    }
}
