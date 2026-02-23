package com.metacoding.springv2.reply;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    /**
     * 댓글 상세 조회 - 작성자와 게시글 정보를 한 번에 가져오기 위한 Fetch Join
     * 
     * @param id 댓글 ID
     * @return 작성자 및 게시글 정보가 포함된 댓글 객체
     */
    @Query("SELECT r FROM Reply r JOIN FETCH r.user JOIN FETCH r.board WHERE r.id = :id")
    Optional<Reply> findByIdWithUserAndBoard(@Param("id") Integer id);
}
