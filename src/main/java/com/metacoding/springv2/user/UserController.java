package com.metacoding.springv2.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 관련 HTTP 요청을 처리하는 컨트롤러.
 * <p>
 * - URL 매핑과 HTTP 레벨의 책임만 갖고<br>
 * - 실제 비즈니스 로직은 UserService에 위임한다.
 * </p>
 */
@RequiredArgsConstructor
@RestController
public class UserController {
    // 서비스 계층 주입을 통해 컨트롤러는 비즈니스 로직에서 분리된다.
    private final UserService userService;

    /**
     * 사용자 정보 조회 API
     * 
     * @param id 조회할 사용자의 ID
     * @return 사용자의 상세 정보를 담은 응답
     */
    // 인증된 사용자 또는 관리자가 특정 사용자를 조회할 때 사용하는 엔드포인트
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> userInfo(@PathVariable Integer id) {
        var respDTO = userService.회원정보보기(id);
        return Resp.ok(respDTO);
    }
}
