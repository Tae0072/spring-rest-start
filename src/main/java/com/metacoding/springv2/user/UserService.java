package com.metacoding.springv2.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metacoding.springv2._core.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;

/**
 * 유저 관련 비즈니스 로직을 처리하는 서비스 계층
 */
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터 정합성 보장 및 성능 최적화
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * 유저 정보 조회
     * 
     * @param id 조회할 유저 ID
     * @return 유저 정보 DTO
     * @throws Exception404 해당 ID의 유저가 존재하지 않을 때 발생
     */
    public UserResponse.UserDTO 회원정보보기(Integer id) {
        var userPS = userRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));
        return new UserResponse.UserDTO(userPS);
    }
}
