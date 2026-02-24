package com.metacoding.springv2.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.metacoding.springv2._core.util.Resp;
import lombok.RequiredArgsConstructor;

/**
 * 유저 관련 API 컨트롤러
 */
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 유저 정보 보기 API
     * 
     * @param id 조회할 유저 ID
     * @return 유저 상세 정보
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> userInfo(@PathVariable Integer id) {
        var respDTO = userService.회원정보보기(id);
        return Resp.ok(respDTO);
    }
}
