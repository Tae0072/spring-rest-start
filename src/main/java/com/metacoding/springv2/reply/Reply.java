package com.metacoding.springv2.reply;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 게시글에 대한 댓글 정보를 나타내는 JPA 엔티티.
 * <p>
 * - DB의 {@code reply_tb} 테이블과 매핑되며<br>
 * - 작성자(User) 및 해당 게시글(Board)과의 다대일 연관관계를 가진다.
 * </p>
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "reply_tb")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 댓글 내용은 최대 100자까지 허용
    @Column(length = 100, nullable = false)
    private String comment;

    // 댓글을 작성한 사용자 (지연 로딩 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 댓글이 달린 게시글 (지연 로딩 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Reply(Integer id, String comment, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.board = board;
        this.createdAt = createdAt;
    }

    // 댓글 내용을 수정하는 도메인 메서드
    public void update(String comment) {
        this.comment = comment;
    }

}