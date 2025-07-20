# News Alert Service


## ✨ 주요 기능

- REST API를 통한 뉴스 수신 및 저장
- 메시지 큐를 이용한 비동기 이벤트 처리
- WebSocket을 통한 실시간 뉴스 업데이트 브로드캐스팅


## 🚀 실행 방법

### 사전 요구사항

- Java 17 이상
- Gradle

### 애플리케이션 실행



애플리케이션이 실행되면 다음 엔드포인트를 통해 테스트할 수 있습니다.

- **뉴스 저장 API**: `POST /news`
  - **Request Body**:
    ```json
    {
    "id": "a1b2352",
    "title": "Qraft AI Product팀 개발자 채용",
    "content": "핀테크 스타트업 크래프트 테크놀로지스(QRAFT Technologies)는",
    "publishedAt": "2025-06-05T10:00:00"
    }
    ```
- **WebSocket 접속**: `ws://localhost:8080/ws/news?token={token}`
  - WebSocket 클라이언트로 접속 후, 위 API를 호출하면 실시간으로 뉴스 객체가 브로드캐스팅됩니다.

---

## 🔧 향후 개선: AWS SQS로 전환 가이드

현재는 `java.util.concurrent.LinkedBlockingQueue`를 이용한 인메모리(In-Memory) 큐를 사용하고 있습니다. 이를 프로덕션 환경에 적합한 **AWS SQS**로 전환하기 위한 단계는 다음과 같습니다.


### SQS Producer 어댑터 구현

`MessageProducerPort`를 구현하는 `SqsProducerAdapter`를 생성합니다.

```java
// src/main/java/Qraft/newsalert/infrastructure/adapter/out/SqsProducerAdapter.java

@Component
@RequiredArgsConstructor
public class SqsProducerAdapter implements MessageProducerPort {

    private final SqsTemplate sqsTemplate;

    @Value("${sqs.queue.name}")
    private String queueName;

    @Override
    public void send(String message) {
        sqsTemplate.send(queueName, message);
    }
}
```

### 4. SQS Consumer 구현

`@SqsListener`를 사용하여 메시지를 수신하는 `SqsConsumerAdapter`를 생성합니다. 이 방식은 프레임워크가 메시지 수신을 관리해주므로, 기존의 `MessageConsumerPort` 인터페이스와 `InMemoryConsumerAdapter`의 복잡한 스레드 관리 로직이 더 이상 필요하지 않습니다.

```java
// src/main/java/Qraft/newsalert/infrastructure/adapter/in/SqsNewsConsumer.java

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsNewsConsumer {

    private final NewsRepository newsRepository;
    private final WebSocketHandler webSocketHandler;

    @SqsListener("${sqs.queue.name}")
    public void handleMessage(String newsId) {
        try {
            News news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new BaseException(Code.NEWS_NOT_FOUND));
            webSocketHandler.broadcastNewsUpdate(news);
        } catch (BaseException e) {
            log.error("Error processing message from SQS: {}, newsId: {}", e.getMessage(), newsId);
        }
    }
}
```

