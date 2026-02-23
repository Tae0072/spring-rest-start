package com.metacoding.springv2.board;

import java.sql.Timestamp;
import java.util.*;
import org.hibernate.annotations.CreationTimestamp;
import com.metacoding.springv2.reply.Reply;
import com.metacoding.springv2.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 게시판 게시글 정보를 나타내는 JPA 엔티티.
 * <p>
 * - DB의 {@code board_tb} 테이블과 매핑되며<br>
 * - 작성자(User)와의 다대일 연관관계 및 댓글(Reply)과의 일대다 연관관계를 가진다.
 * </p>
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 제목은 최대 30자까지 허용
    @Column(length = 30, nullable = false)
    private String title;
    // 내용은 최대 300자까지 허용
    @Column(length = 300, nullable = false)
    private String content;

    // 게시글을 작성한 사용자 (지연 로딩 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    // 게시글에 달린 댓글 목록 (게시글 삭제 시 관련 댓글도 함께 삭제되도록 설정)
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    // 게시글의 제목과 내용을 수정하는 도메인 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public Board(Integer id, String title, String content, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

}