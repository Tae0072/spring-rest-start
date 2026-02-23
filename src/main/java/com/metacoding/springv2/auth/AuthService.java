package com.metacoding.springv2.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception401;
import com.metacoding.springv2._core.handler.ex.Exception400;
import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.auth.AuthRequest.JoinDTO;
import com.metacoding.springv2.auth.AuthRequest.LoginDTO;
import com.metacoding.springv2.user.User;
import com.metacoding.springv2.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 로그인 비즈니스 로직
     * 
     * @param reqDTO 로그인 요청 정보 (username, password)
     * @return 생성된 JWT 액세스 토큰
     * @throws Exception401 유저 정보가 없거나 비밀번호가 일치하지 않을 경우 발생
     */
    public String 로그인(LoginDTO reqDTO) {
        // 1. UserRepository에서 유저네임 존재 여부 확인
        User findUser = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception401("유저네임을 찾을 수 없어요"));

        // 2. 해시된 비밀번호와 입력된 비밀번호 비교 검증
        boolean isSamePassword = bCryptPasswordEncoder.matches(reqDTO.getPassword(), findUser.getPassword());
        if (!isSamePassword)
            throw new Exception401("비밀번호가 틀렸어요");

        // 3. 인증 성공 시 JWT 토큰 생성 및 반환
        return JwtUtil.create(findUser);
    }

    /**
     * 사용자 이름 중복 체크 비즈니스 로직
     * 
     * @param username 중복 확인할 사용자 이름
     * @throws Exception400 동일한 사용자 이름이 이미 존재할 경우 발생
     */
    public void 유저네임중복체크(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new Exception400("이미 존재하는 유저네임입니다");
        });
    }

    @Transactional
    public AuthResponse.DTO 회원가입(JoinDTO reqDTO) {

        // 1. 패스워드 해시하기
        String encPassword = bCryptPasswordEncoder.encode(reqDTO.getPassword());

        User user = User.builder()
                .username(reqDTO.getUsername())
                .password(encPassword)
                .email(reqDTO.getEmail())
                .roles("USER")
                .build();
        userRepository.save(user);
        return new AuthResponse.DTO(user);
    }
}
