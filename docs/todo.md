## 할일
### 4/8
- 좋아요한 사람들 배열로 집어넣을 수 있는지 확인
- 키워드(카테고리) 테이블 따로 빼기
- 신청여부 erd 테이블 만들기
- 신청기능 파일구조 짜기
- annotationProcessor vs compileOnly
- hibernate vs jpa
- jpa 안에 hibernate, properties 안에 hibernate
- database랑 datasource, hibernate, jpa 작동원리 확인하기
- connection pool 찾아보기 sprongboot는 설정을 어디서 담당하고 있는지, default 설정이 무조건 있을것
- 로그인 회원가입 기능 만들어놓기

### 4/29
- configurations 안에 compileOnly 뭐하는앤지 알아오기
- hibernate, spring Data JPA 동작과정 정리
- ddl-auto create-drop, update 또는 validate
- jpa 안에 hibernate, properties 안에 hibernate
- 웹의 process와 thread 차이
- connection pool 어디서 만들어짐
- 동시성 제어를 어디서 하는지 주체 확인
- 어노테이션 뭔지 쓴거 다 알아오기
- 스프링 기본 동작과정 확인하기
- 로그인 회원가입 기능 만들어놓기 (컨트롤러, 서비스, 리포지토리)
- 모집 글 작성 CRUD까지

### 5/13
- 락 세개 동작과정 자세히 예제 들어서 알아오기
- HttpSession session 로그인 동작과정 알아오기
- 카테고리 타입 enum값 추가
- 신청 로직 구현해오기
- study랑 category 관계 수정

### 5/20
- Redis 분산락 사용 예시
- 토큰 세션 등 인증방식 알아오기 크게 세 개 있을 것
- OAuth2 동작과정 알아오기
- DAO란? 어디서 쓰여? DTO랑 차이점은?
- @JoinColumn(name = "member_id", nullable = false) 이름 왜 설정함? 이름 자동설정?
- @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Study> studies;
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Likes> likes;
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Comment> comments; 이거 왜 필요함? OneToMany ManyToOne이 뭔지 무슨 역할을 하는지 자세히 알려줘
- @RestController JSON만 가능한지 알아보기
- @RequestBody도 JSON만 가능해?
- 컬트 중간연구보고서 컨펌

### 6/10
- 컬트 활동사진 2회 촬영