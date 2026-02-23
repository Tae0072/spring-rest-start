package com.metacoding.springv2.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 이름 중복 체크 API
     * 
     * @param username 중복 확인할 사용자 이름
     * @return 사용 가능 여부 메시지를 담은 응답
     */
    @GetMapping("/join/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        authService.유저네임중복체크(username);
        return Resp.ok("사용가능한 유저네임입니다");
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AuthRequest.JoinDTO reqDTO) {
        var respDTO = authService.회원가입(reqDTO);
        return Resp.ok(respDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.LoginDTO reqDTO) {
        String accessToken = authService.로그인(reqDTO);
        return Resp.ok(accessToken);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
