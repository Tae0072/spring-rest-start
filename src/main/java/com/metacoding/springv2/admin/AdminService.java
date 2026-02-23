package com.metacoding.springv2.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 관리자 관련 비즈니스 로직을 담당하는 서비스 계층.
 * <p>
 * - 추후 관리자용 대시보드 조회, 회원 관리 등의 기능이 추가될 예정.
 * </p>
 */
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 전역 적용 (데이터 정합성 및 성능 최적화)
@Service
public class AdminService {
    // 관리자 관련 전용 비즈니스 로직 구현 예정
}
