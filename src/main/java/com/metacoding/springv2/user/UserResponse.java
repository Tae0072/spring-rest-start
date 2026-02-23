package com.metacoding.springv2.user;

import java.sql.Timestamp;

import com.metacoding.springv2.user.User;

import lombok.Data;

/**
 * 사용자 정보를 API 응답 형태로 전달하기 위한 DTO 모음 클래스.
 * <p>
 * 엔티티를 그대로 노출하지 않고, 필요한 필드만 선택해서 내려주기 위해 사용한다.
 * </p>
 */
public class UserResponse {

    @Data
    public static class UserDTO {
        private Integer id;
        private String username;
        private String email;
        private Timestamp createdAt;

        // User 엔티티를 읽기 전용 DTO로 변환하는 생성자
        public UserDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt();
        }
    }
}
