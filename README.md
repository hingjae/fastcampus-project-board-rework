# 패스트 캠퍼스 게시판 프로젝트

이 프로젝트는 스프링 부트를 사용하여 게시판 애플리케이션을 만드는 패스트 캠퍼스 강의를 수강한 후, 스스로 공부한 내용을 추가로 적용하고 일부를 커스텀한 결과물입니다.

### 기술
- JAVA
- Spring Boot
- MySQL
- JPA
- QueryDsl
- Thymeleaf
- Spring Security

### 추가 적용
- 게시판 조건 검색 기능에 QueryDSL 사용
- 외부 시스템에 의존하지 않는 테스트 작성
- 단위 테스트 비중 늘리기
  - 비즈니스 로직은 최대한 도메인 객체 안에서 수행
- fetch join 사용으로 쿼리 최적화
- Article과 Hashtag의 관계를 다대다가 아닌 일대다 다대일 관계로 풀어낸다.
  - orphanRemoval 적용
- 일급 컬렉션 사용.
