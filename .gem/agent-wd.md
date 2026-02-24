# 작업 로그

## [2026-02-23] 회원 정보 보기 기능 추가
- UserResponse.UserDTO 생성: 회원 정보를 담을 응답 객체 정의.
- UserService 구현: 유저 ID로 조회하여 DTO로 반환하는 로직 추가.
- UserController 구현: /api/users/{id} 엔드포인트 구현.
- SecurityConfig 수정: CORS 필터를 비활성화에서 활성화로 변경 및 설정 추가 (rule.md 규정 준수).
