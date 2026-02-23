package com.metacoding.springv2.reply;

import java.sql.Timestamp;

import com.metacoding.springv2.user.User;

import lombok.Data;

public class ReplyResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String comment;
        private Integer userId;
        private String username;
        private Integer boardId;
        private Timestamp createdAt;

        public DTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = reply.getUser().getId();
            this.username = reply.getUser().getUsername();
            this.boardId = reply.getBoard().getId();
            this.createdAt = reply.getCreatedAt();
        }
    }
}
