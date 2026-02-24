package com.metacoding.springv2.user;

import java.sql.Timestamp;
import lombok.Data;

public class UserResponse {

    /**
     * 회원 정보 보기 응답 DTO
     */
    @Data
    public static class UserDTO {
        private Integer id;
        private String username;
        private String email;
        private String roles;
        private Timestamp createdAt;

        public UserDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.roles = user.getRoles();
            this.createdAt = user.getCreatedAt();
        }
    }
}
