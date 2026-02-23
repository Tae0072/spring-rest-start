package com.metacoding.springv2._core.util;

import lombok.Data;
import org.springframework.http.*;

/**
 * API 공통 응답 형식을 정의하는 유틸리티 클래스.
 * <p>
 * <b>왜 공통 응답(Standard Response) 규격을 사용하는가?</b><br>
 * 프론트엔드(React, Mobile 등)가 성공/실패 여부를 동일한 필드(status, msg, body)로 파싱하여 
 * 일관성 있게 처리할 수 있도록 하기 위함.
 * </p>
 */
@Data
public class Resp<T> {
    private Integer status; // HTTP 상태 코드 (예: 200, 400, 401, 500)
    private String msg;     // 서버에서 전달하는 메시지 (예: "성공", "로그인 실패")
    private T body;         // 실제 결과 데이터 (객체, 리스트 등)

    public Resp(Integer status, String msg, T body) {
        this.status = status;
        this.msg = msg;
        this.body = body;
    }

    /**
     * 성공 응답 처리 (HTTP 200 OK)
     * 
     * @param body 전달할 데이터
     * @return 200 상태코드와 표준 응답 형식을 담은 ResponseEntity
     */
    public static <B> ResponseEntity<Resp<B>> ok(B body) {
        Resp<B> resp = new Resp<>(200, "성공", body);
        return new ResponseEntity<>(resp, HttpStatus.OK); // body, header를 함께 응답
    }

    /**
     * 실패 응답 처리
     * 
     * @param status 실패 상태 (HttpStatus 객체)
     * @param msg    실패 사유 메시지
     * @return 에러 코드와 에러 메시지를 포함한 ResponseEntity
     */
    public static ResponseEntity<?> fail(HttpStatus status, String msg) {
        Resp<?> resp = new Resp<>(status.value(), msg, null);
        return new ResponseEntity<>(resp, status);
    }
}