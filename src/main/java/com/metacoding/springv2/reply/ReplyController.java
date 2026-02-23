package com.metacoding.springv2.reply;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;

    /**
     * 댓글 작성 API
     * 
     * @param boardId 게시글 ID
     * @param reqDTO  댓글 작성 요청 DTO
     * @return 작성된 댓글 정보
     */
    @PostMapping("/api/boards/{boardId}/replies")
    public ResponseEntity<?> save(@PathVariable Integer boardId,
            @Valid @RequestBody ReplyRequest.SaveDTO reqDTO) {
        Integer userId = currentUserId();
        ReplyResponse.DTO respDTO = replyService.댓글작성(boardId, reqDTO, userId);
        return Resp.ok(respDTO);
    }

    /**
     * 댓글 수정 API
     * 
     * @param id     댓글 ID
     * @param reqDTO 댓글 수정 요청 DTO
     * @return 수정된 댓글 정보
     */
    @PutMapping("/api/replies/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
            @Valid @RequestBody ReplyRequest.UpdateDTO reqDTO) {
        Integer userId = currentUserId();
        ReplyResponse.DTO respDTO = replyService.댓글수정하기(id, reqDTO, userId);
        return Resp.ok(respDTO);
    }

    /**
     * 댓글 삭제 API
     * 
     * @param id 댓글 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/api/replies/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Integer userId = currentUserId();
        replyService.댓글삭제하기(id, userId);
        return Resp.ok("댓글 삭제가 완료되었습니다");
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
