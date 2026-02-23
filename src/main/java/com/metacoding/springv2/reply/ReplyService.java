package com.metacoding.springv2.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.board.BoardRepository;
import com.metacoding.springv2.user.User;
import com.metacoding.springv2.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성 비즈니스 로직
     * 
     * @param boardId 게시글 ID
     * @param reqDTO  댓글 작성 요청 DTO
     * @param userId  작성자 ID
     * @return 작성된 댓글 정보 DTO
     */
    @Transactional
    public ReplyResponse.DTO 댓글작성(Integer boardId, ReplyRequest.SaveDTO reqDTO, Integer userId) {
        Board boardPS = boardRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다"));

        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));

        Reply reply = Reply.builder()
                .comment(reqDTO.getComment())
                .board(boardPS)
                .user(userPS)
                .build();

        Reply replyPS = replyRepository.save(reply);
        return new ReplyResponse.DTO(replyPS);
    }

    /**
     * 댓글 수정 비즈니스 로직
     * 
     * @param id     댓글 ID
     * @param reqDTO 댓글 수정 요청 DTO
     * @param userId 수정 요청한 사용자 ID
     * @return 수정된 댓글 정보 DTO
     */
    @Transactional
    public ReplyResponse.DTO 댓글수정하기(Integer id, ReplyRequest.UpdateDTO reqDTO, Integer userId) {
        Reply replyPS = replyRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글을 찾을 수 없습니다"));

        // 권한 체크: 작성자만 수정 가능
        if (!replyPS.getUser().getId().equals(userId)) {
            throw new Exception403("댓글을 수정할 권한이 없습니다");
        }

        replyPS.update(reqDTO.getComment());
        return new ReplyResponse.DTO(replyPS);
    }

    /**
     * 댓글 삭제 비즈니스 로직
     * 
     * @param id     댓글 ID
     * @param userId 삭제 요청한 사용자 ID
     */
    @Transactional
    public void 댓글삭제하기(Integer id, Integer userId) {
        Reply replyPS = replyRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글을 찾을 수 없습니다"));

        // 권한 체크: 작성자만 삭제 가능
        if (!replyPS.getUser().getId().equals(userId)) {
            throw new Exception403("댓글을 삭제할 권한이 없습니다");
        }

        replyRepository.delete(replyPS);
    }
}
