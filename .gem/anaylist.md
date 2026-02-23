# 프로젝트 분석 보고서 (업데이트)

## 1. 프로젝트 개요

본 프로젝트는 Spring Boot를 사용하여 구축된 RESTful API 서버입니다. 주요 기능으로 사용자 관리, 게시판, 댓글 기능을 제공하며, JWT 기반의 인증/인가 시스템을 갖추고 있습니다.

## 2. 핵심 아키텍처 및 기술 스택

- **언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스 연동**: Spring Data JPA (Hibernate)
- **보안**: Spring Security, JWT (JSON Web Token)
- **아키텍처**:
    - **3-Layer Architecture**: `Controller` - `Service` - `Repository` 계층으로 역할을 명확히 분리합니다.
    - **DTO Pattern**: `Request`/`Response` DTO를 사용하여 API의 입출력 명세를 관리하고 계층 간 데이터를 안전하게 전달합니다.
    - **중앙화된 예외 처리**: `@RestControllerAdvice`와 커스텀 예외 클래스(`Exception4xx`)를 사용하여 예외를 일관되게 처리합니다.

## 3. 주요 기능 및 API Endpoints

### 3.1. 인증 (Authentication) - `AuthController`

- **회원가입**: `POST /join`
- **로그인**: `POST /login`
- **서버 상태 체크**: `GET /health`
- **유저네임 중복 체크**: `GET /join/check-username`

### 3.2. 사용자 (User) - `UserController`

- **사용자 정보 조회**: `GET /api/users/{id}`
  - *인증이 필요한 API입니다.*

### 3.3. 게시판 (Board) - `BoardController`, `BoardService`

- **게시글 목록 조회**: `GET /api/boards` (누구나 접근 가능)
- **게시글 상세 조회**: `GET /api/boards/{id}` (누구나 접근 가능)
- **게시글 작성**: `POST /api/boards` (인증 필요)
- **게시글 수정**: `PUT /api/boards/{id}` (인증 필요)
- **게시글 삭제**: `DELETE /api/boards/{id}` (인증 필요)

### 3.4. 댓글 (Reply) - `ReplyController`, `ReplyService`

- **댓글 작성**: `POST /api/boards/{boardId}/replies` (인증 필요)
- **댓글 수정**: `PUT /api/replies/{id}` (인증 필요)
- **댓글 삭제**: `DELETE /api/replies/{id}` (인증 필요)

## 4. 보안 설정 (`SecurityConfig`)

- **JWT 필터**: 모든 요청에 대해 `JwtAuthorizationFilter`를 거쳐 토큰의 유효성을 검사하고 사용자 인증을 처리합니다.
- **CORS 설정**: `CorsConfigurationSource` Bean을 등록하여 모든 도메인(`*`)에서의 요청을 허용하도록 설정되어 있습니다.
- **인가 (Authorization)**:
    - `GET /api/boards/**` 경로는 누구나 접근 가능합니다.
    - `/api/**` 패턴의 그 외 모든 요청은 인증된 사용자만 접근할 수 있습니다.
    - `/admin/**` 패턴의 요청은 `ADMIN` 역할을 가진 사용자만 접근할 수 있습니다.
    - 그 외 (`/join`, `/login` 등) 요청은 누구나 접근 가능합니다.

## 5. 총평

본 프로젝트는 Spring Boot 기반 REST API 서버의 표준적인 관행을 잘 따르고 있습니다. 계층 분리, DTO 활용, 중앙화된 예외 처리, JWT 기반 보안 등 현대적인 웹 애플리케이션 개발에 필요한 핵심 요소들이 체계적으로 구현되어 있습니다.
