package Qraft.newsalert.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Code {


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에러"),
    WEBSOCKET_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "웹소켓 연결에 실패했습니다."),
    WEBSOCKET_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "웹소켓 메시지 전송에 실패했습니다."),
    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "뉴스를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
