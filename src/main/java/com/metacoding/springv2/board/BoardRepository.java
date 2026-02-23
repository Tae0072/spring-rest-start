package com.metacoding.springv2.board;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 게시글 엔티티에 대한 DB 접근을 담당하는 리포지토리.
 * <p>
 * <b>중요: Fetch Join 사용 이유</b><br>
 * JPA의 지연 로딩(Lazy Loading)으로 인해 발생하는 N+1 쿼리 문제를 해결하기 위해 도입됨.<br>
 * N+1 문제란? 한 번의 조회로 연관된 엔티티 정보까지 가져오려다, 각 결과마다 추가 쿼리가 발생하는 현상임.
 * </p>
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {

    /**
     * 게시글 목록 조회 - 작성자(User) 정보를 한 번에 가져오기 위한 Fetch Join
     * <p>
     * 왜? 리스트 화면에서 작성자 이름(username)을 노출해야 하는데, 일반 join을 쓰면 
     * 각 게시글마다 User를 조회하는 쿼리가 따로 날아가 성능이 저하됨.
     * </p>
     * 
     * @param pageable 페이징 정보
     * @return 작성자 정보가 이미 메모리에 로드된 게시글 페이지
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.user")
    Page<Board> findAllWithUser(Pageable pageable);

    /**
     * 게시글 상세 조회 - 유저와 댓글, 댓글 작성자 정보를 한 번의 쿼리로 병합 조회
     * <p>
     * 왜? 게시글 상세보기 화면에서 작성자, 모든 댓글, 그리고 그 댓글의 작성자까지 모두 보여줘야 함.
     * 이 때 Fetch Join을 쓰지 않으면 쿼리가 수십 번 발생할 수 있는 심각한 비효율이 발생함.
     * </p>
     * 
     * @param id 게시글 ID
     * @return 작성자, 댓글 목록, 댓글 작성자가 모두 포함된 Optional 객체
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.user LEFT JOIN FETCH b.replies r LEFT JOIN FETCH r.user WHERE b.id = :id")
    Optional<Board> findByIdWithUserAndReplies(@Param("id") Integer id);
}
