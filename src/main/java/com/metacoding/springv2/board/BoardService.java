package com.metacoding.springv2.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.user.User;
import com.metacoding.springv2.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 작성 비즈니스 로직
     * 
     * @param reqDTO 게시글 작성 요청 DTO
     * @param userId 작성자 ID
     * @return 작성된 게시글 상세 정보 DTO
     */
    @Transactional
    public BoardResponse.DetailDTO 게시글작성(BoardRequest.SaveDTO reqDTO, Integer userId) {
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));

        Board board = Board.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .user(userPS)
                .build();

        Board boardPS = boardRepository.save(board);
        return new BoardResponse.DetailDTO(boardPS);
    }

    /**
     * 게시글 목록 조회 비즈니스 로직
     * 
     * @param page 페이지 번호 (0부터 시작)
     * @return 게시글 목록 페이지
     */
    @Transactional(readOnly = true)
    public Page<BoardResponse.ListDTO> 게시글목록보기(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(BoardResponse.ListDTO::new);
    }

    /**
     * 게시글 상세 조회 비즈니스 로직
     * 
     * @param id 게시글 ID
     * @return 게시글 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public BoardResponse.DetailDTO 게시글상세보기(Integer id) {
        Board boardPS = boardRepository.findByIdWithUserAndReplies(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다"));
        return new BoardResponse.DetailDTO(boardPS);
    }

    /**
     * 게시글 수정 비즈니스 로직
     * 
     * @param id     게시글 ID
     * @param reqDTO 게시글 수정 요청 DTO
     * @param userId 수정 요청한 사용자 ID
     * @return 수정된 게시글 상세 정보 DTO
     */
    @Transactional
    public BoardResponse.DetailDTO 게시글수정하기(Integer id, BoardRequest.UpdateDTO reqDTO, Integer userId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다"));

        // 권한 체크: 작성자만 수정 가능
        if (!boardPS.getUser().getId().equals(userId)) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        boardPS.update(reqDTO.getTitle(), reqDTO.getContent());
        return new BoardResponse.DetailDTO(boardPS);
    }

    /**
     * 게시글 삭제 비즈니스 로직
     * 
     * @param id     게시글 ID
     * @param userId 삭제 요청한 사용자 ID
     */
    @Transactional
    public void 게시글삭제하기(Integer id, Integer userId) {
        Board boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다"));

        // 권한 체크: 작성자만 삭제 가능
        if (!boardPS.getUser().getId().equals(userId)) {
            throw new Exception403("게시글을 삭제할 권한이 없습니다");
        }

        boardRepository.delete(boardPS);
    }
}
