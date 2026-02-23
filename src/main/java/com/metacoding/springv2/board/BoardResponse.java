package com.metacoding.springv2.board;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.metacoding.springv2.reply.Reply;
import com.metacoding.springv2.user.User;

import lombok.Data;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Timestamp createdAt;
        private List<ReplyDTO> replies;

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
            // 댓글 엔티티 리스트를 DTO 리스트로 변환하여 할당
            this.replies = board.getReplies().stream()
                    .map(ReplyDTO::new)
                    .toList(); // Java 16+ 스타일로 간소화
        }
    }

    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String content; // 게시글 내용 추가
        private String username;
        private Timestamp createdAt;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent(); // 게시글 내용 매핑
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
        }
    }

    @Data
    public static class ReplyDTO {
        private Integer id;
        private String comment;
        private Integer userId;
        private String username;
        private Timestamp createdAt;

        public ReplyDTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = reply.getUser().getId();
            this.username = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt();
        }
    }
}
