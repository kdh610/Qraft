package Qraft.newsalert.application.port.in;

/**
 * 메세지 큐에서 메세지를 수신하고 처리하는 포트입니다.
 */
public interface MessageConsumerPort {

    void receiveMessage();
}
