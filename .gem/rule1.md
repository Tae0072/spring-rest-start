# 에플리케이션 제약(코드 컨벤션, 기술 스텍 정의, 코드 패턴 정의)

- 시큐리티에서 CORS 필터를 적용
- Controller에 주소는 RestAPI 규칙을 지켜서 /api/users/{id} 형태로 만들기
- Controller 메서드는 AuthController 참고해서 패턴 동일하게 만들기
- DTO는 @Data를 써
- 추가된 소스코드에는 꼭 주석을 친절하고 쉽게 달아줘
- 코드 수정이 일어나면, 대체된 필요없는 코드는 삭제해줘
  ex) SecurityConfig에 CORS필터를 직접 적용했는데, FilterConfig에 CORS부분과 filter폴더에 CorsFilter 코드를 삭제하지 않았어!
- @Transactional(readOnly = true)를 Service 클래스 위에 전역적으로 붙여줘
- Lazy로딩인 것들을 조회할때 select를 여러번 하지말고, fetch join 메서드 만들어서 조인해서 가져와줘
