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

/**
 * 게시글 관련 비즈니스 로직을 담당하는 서비스 계층.
 * <p>
 * <b>@Transactional(readOnly = true) 전역 설정 이유:</b><br>
 * 서비스의 대부분은 단순 조회(Read) 작업이므로 기본값을 읽기 전용으로 설정함.<br>
 * 이는 DB 성능 최적화(스냅샷 유지 비용 감소)와 데이터 변조 방지 효과가 있음.
 * <b>작성/수정/삭제:</b> 별도의 @Transactional을 메서드에 명시하여 쓰기 권한을 부여함.
 * </p>
 */
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 전역 적용 (성능 최적화 및 안정성 확보)
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 작성 비즈니스 로직
     * <p>
     * 왜? 컨트롤러는 요청만 받고, 실제 객체 생성과 DB 저장은 서비스가 담당함 (관심사의 분리).
     * </p>
     * 
     * @param reqDTO 게시글 작성 정보
     * @param userId 작성자 ID
     * @return 저장된 정보를 포함한 DetailDTO
     */
    @Transactional // 쓰기 권한 부여
    public BoardResponse.DetailDTO 게시글작성(BoardRequest.SaveDTO reqDTO, Integer userId) {
        // 1. 작성자 존재 확인 (안전한 처리를 위한 방어적 프로그래밍)
        var userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("해당 유저를 찾을 수 없습니다"));

        // 2. DTO -> Entity 변환: 데이터베이스에 저장할 객체 구성
        var board = Board.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .user(userPS)
                .build();

        // 3. 실제 저장 처리
        var boardPS = boardRepository.save(board);
        
        // 4. Entity -> DTO 변환: 응답은 반드시 DTO로 반환하여 엔티티 구조를 숨김 (보안 및 유지보수성)
        return new BoardResponse.DetailDTO(boardPS);
    }

    /**
     * 게시글 목록 조회
     * 
     * @param page 조회할 페이지 번호
     * @return DTO로 변환된 게시글 페이지
     */
    public Page<BoardResponse.ListDTO> 게시글목록보기(int page) {
        // 1. 페이징 및 정렬 조건 설정 (최근 등록 순)
        var pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // 2. Fetch Join을 사용하여 작성자 정보와 함께 한 번에 조회 (성능 최적화)
        var boardPage = boardRepository.findAllWithUser(pageable);
        
        // 3. Entity Page -> DTO Page 변환 후 반환
        return boardPage.map(BoardResponse.ListDTO::new);
    }

    /**
     * 게시글 상세 조회 비즈니스 로직
     * 
     * @param id 게시글 ID
     * @return 게시글 상세 정보 DTO
     */
    public BoardResponse.DetailDTO 게시글상세보기(Integer id) {
        var boardPS = boardRepository.findByIdWithUserAndReplies(id)
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
    @Transactional // 쓰기 작업이므로 별도의 @Transactional 설정
    public BoardResponse.DetailDTO 게시글수정하기(Integer id, BoardRequest.UpdateDTO reqDTO, Integer userId) {
        var boardPS = boardRepository.findById(id)
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
    @Transactional // 쓰기 작업이므로 별도의 @Transactional 설정
    public void 게시글삭제하기(Integer id, Integer userId) {
        var boardPS = boardRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 게시글을 찾을 수 없습니다"));

        // 권한 체크: 작성자만 삭제 가능
        if (!boardPS.getUser().getId().equals(userId)) {
            throw new Exception403("게시글을 삭제할 권한이 없습니다");
        }

        boardRepository.delete(boardPS);
    }
}
