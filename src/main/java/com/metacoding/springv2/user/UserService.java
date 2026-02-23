package com.metacoding.springv2.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception404;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 관련 비즈니스 로직을 담당하는 서비스 계층.
 * <p>
 * - 컨트롤러에서 들어온 요청을 받아 리포지토리를 호출하고<br>
 * - 엔티티를 DTO로 변환하는 역할을 담당한다.
 * </p>
 */
@RequiredArgsConstructor
@Service
public class UserService {
    // 생성자 주입(불변 필드)으로 UserRepository를 사용한다.
    private final UserRepository userRepository;

    /**
     * 사용자 정보 보기 비즈니스 로직
     * 
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 정보 DTO
     * @throws Exception404 해당 ID의 사용자가 없을 경우 발생
     */
    // 읽기 전용 트랜잭션으로 성능 최적화 및 불필요한 쓰기 잠금 방지
    @Transactional(readOnly = true)
    public UserResponse.UserDTO 회원정보보기(Integer id) {
        User userPS = userRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));
        return new UserResponse.UserDTO(userPS);
    }
}