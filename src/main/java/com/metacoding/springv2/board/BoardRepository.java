package com.metacoding.springv2.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    /**
     * 게시글 상세 조회 - 유저와 댓글, 댓글 작성자 정보를 한 번에 가져오기 위한 Fetch Join 쿼리
     * 
     * @param id 게시글 ID
     * @return 게시글, 작성자, 댓글 목록이 포함된 Optional 객체
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.replies r LEFT JOIN FETCH r.user WHERE b.id = :id")
    Optional<Board> findByIdWithUserAndReplies(@Param("id") Integer id);
}
