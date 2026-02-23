package com.metacoding.springv2.board;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시글 작성 API
     * 
     * @param reqDTO 게시글 작성 요청 DTO
     * @return 작성된 게시글 상세 정보
     */
    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO reqDTO) {
        Integer userId = currentUserId();
        BoardResponse.DetailDTO respDTO = boardService.게시글작성(reqDTO, userId);
        return Resp.ok(respDTO);
    }

    /**
     * 게시글 목록 조회 API
     * 
     * @param page 페이지 번호 (기본값: 0)
     * @return 게시글 목록 페이지
     */
    @GetMapping("/api/boards")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page) {
        Page<BoardResponse.ListDTO> respDTO = boardService.게시글목록보기(page);
        return Resp.ok(respDTO);
    }

    /**
     * 게시글 상세 조회 API
     * 
     * @param id 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        BoardResponse.DetailDTO respDTO = boardService.게시글상세보기(id);
        return Resp.ok(respDTO);
    }

    /**
     * 게시글 수정 API
     * 
     * @param id     게시글 ID
     * @param reqDTO 게시글 수정 요청 DTO
     * @return 수정된 게시글 상세 정보
     */
    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
            @Valid @RequestBody BoardRequest.UpdateDTO reqDTO) {
        Integer userId = currentUserId();
        BoardResponse.DetailDTO respDTO = boardService.게시글수정하기(id, reqDTO, userId);
        return Resp.ok(respDTO);
    }

    /**
     * 게시글 삭제 API
     * 
     * @param id 게시글 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Integer userId = currentUserId();
        boardService.게시글삭제하기(id, userId);
        return Resp.ok("게시글 삭제가 완료되었습니다");
    }

    /**
     * 현재 로그인한 사용자의 ID를 반환하는 헬퍼 메서드
     * 
     * @return 현재 로그인한 사용자 ID
     */
    private Integer currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
