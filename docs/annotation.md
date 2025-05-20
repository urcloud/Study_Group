# 어노테이션 정리

## Entity에 쓰인 어노테이션

### @Entity
* 해당 클래스가 데이터베이스 테이블과 매핑되는 엔티티 클래스임을 나타냄
* JPA가 해당 클래스를 엔티티로 인식하고 DB 테이블과 자동으로 매핑해줌
* @Entity가 붙은 클래스를 JPA가 스캔(@SpringBootApplication 내 패키지에서 자동 스캔)


### @Id
* 해당 컬럼을 PK(Primary Key, 기본 키)로 설정


### @Column(nullable = false)
* 컬럼의 조건들을 괄호안에 걸어줄 수 있음
* 위 조건대로면 DB에서 NOT NULL 제약 조건이 걸려 null 값을 저장할 수 없게 됨


### @Table(name = "apply")
* 테이블 어노테이션은 뜻 그대로 테이블과 연결시켜주기 위해 씀
* 보통은 엔티티와 테이블네임을 일치시켜주는 게 아주 일반적인 것으로 알고 있는데, 경우에 따라 그럴 수 없기도 함
* 이럴 때 위 어노테이션을 사용해서 테이블과 엔티티를 매칭시켜줌


### @Getter
* Lombok 라이브러리에서 제공하는 어노테이션
* getter 메서드 자동으로 생성해주는 기능
* 엔티티의 필드를 조회해야할 경우 사용됨


### @NoArgsConstructor(access = AccessLevel.PROTECTED)
* 해당 클래스에 기본 생성자를 만들어줌
* JPA는 기본 스펙상 기본 생성자를 요구함
* @Entity 어노테이션은 내부적으로 기본 생성자를 만들어주기 때문에 @NoArgsConstructor 어노테이션이 붙어있지 않은 경우도 많음
* ORM 기술이라는 것은 기본적으로 영속성 이라는 개념이 있는데, 데이터베이스의 실제 데이터와 그 엔티티가 일관된 상태를 유지하고 있어야한다는 것을 말함
* 그런 이유로 엔티티에는 @Setter 어노테이션을 붙이지 않음
* 그럼에도 값을 넣어주긴 해야되니까 생성자를 씀, 실제로 패턴 상 안전한 방법이기 때문
* 위 어노테이션을 활용해서 기본 생성자를 추가하고, 접근 제한을 걸어서 안전성도 높일 수 있음
* JPA가 받아들일 수 있는 최대 수준의 생성자가 Protected이기 때문에 할 수 있는 선에서 최대한의 접근 제한을 건 것이라고 생각하면 됨


### @AllArgsConstructor
* 전체 필드에 대한 생성자
* ORM에서 Setter을 사용하지 않고, new() 방식으로 객체를 생성하는 것 또한 일관성이 깨질 위험이 있어 사용하지 않음
* 따라서 주로 Builder 패턴을 사용하게 됨
* 빌더 패턴은 기본적으로 빌더 어노테이션이 적용된 전체 필드에 대한 값을 요구하기 때문에 생성자가 반드시 필요함
* @NoArgsConstructor을 사용하면 기본생성자가 만들어지지만, 접근 제한 때문에 에러가 발생함
* 그럼 기존의 클래스는 @NoArgsConstructor가 붙었으니까 생성자가 있다고 판단, 때문에 빌더는 생성자를 만들지 않고 이미 있는 생성자에 접근을 시도하지만 이게 Protected로 막혀있어서 접근을 거절당하고 결국 에러를 띄우게 됨
* 결국 빌더 패턴에서 요구하는 생성자는 전체 필드에 대한 생성자이므로 @AllArgsConstructor를 사용해서 해결
* @Entity는 생성자를 추가하는 코드를 쓰지 않아도 자동으로 기본생성자는 만들어준다고 했었지만, 이미 생성자가 있을 경우에는 만들지 않음
* 따라서 @AllArgsConstructor만 적용하고 나면 기본생성자가 만들어지지 않는 문제가 생겨버림
* @AllArgsConstructor 생성자를 넣어줌으로써 빌더 패턴을 챙길 수 있고, @All 때문에 자동생성 되지 않은 기본 생성자를 @NoArgsConstructor 어노테이션으로 직접 추가해주는 것


### @RequiredArgsConstructor
* final은 Java에서 불변성을 보장하기 위해 사용되는데, 클래스에 있는 final 필드를 생성자 매개변수로 포함한 생성자를 자동 생성함


### @SuperBuilder
* 상속 구조에서도 사용 가능한 유연한 빌더 패턴 생성


### @ManyToOne(fetch = FetchType.LAZY)
### @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
### @JoinColumn(name = "member_id", nullable = false)
* fetch = FetchType.LAZY는 지연 로딩(Lazy Loading) 전략을 지정하는 옵션으로, 연관된 엔티티를 필요할 때(실제로 접근할 때) 로딩한다는 의미
* 연관관계가 복잡하거나 데이터가 많을 때, 필요하지 않은 연관 데이터를 미리 다 가져오면 성능 저하 발생하므로 지연 로딩을 쓰면 불필요한 DB 조회를 줄여 성능 최적화 가능
* mappedBy = "member"는 양방향 연관관계에서 주인이 아닌 쪽을 지정하는 속성으로, "member"는 연관된 엔티티의 어떤 필드가 연관관계의 주인인지 나타냄
* 위 경우, 실제 외래 키(member_id)를 가지고 있는 경우 관계의 주인, 주인이 아닌 쪽은 mappedBy = "member"로 member 필드를 참조하여 주인이 아닌 쪽임을 알려줌
* 주인인 쪽은 DB 상태 반영 담당함
* 주인이 아닌 쪽에서는 외래키를 관리하지 않고, 조회용으로만 사용함
* 부모 엔티티의 모든 상태 변화(PERSIST, REMOVE 등)를 연관 자식 엔티티에 자동 전파
* 실수로 자식 엔티티를 따로 저장/삭제하는 일을 방지함


### @RestController
* 스프링 MVC에서 웹 API(REST API)용 컨트롤러를 만들 때 붙이는 어노테이션
* 내부적으로 @Controller + @ResponseBody가 합쳐진 형태
* HTTP 요청을 받아서 처리하고, JSON 같은 데이터를 응답 바디에 바로 실어서 반환해 줌


### @Service
* 비즈니스 로직을 처리하는 서비스 클래스임을 나타내는 어노테이션
* 스프링이 이 클래스를 컴포넌트로 인식해서 DI(의존성 주입)에 사용
* 의존성 주입은 필요한 객체를 직접 생성하지 않고 스프링이 자동으로 만들어서 주입해 주는 것을 뜻함
* 데이터베이스 조회, 계산, 트랜잭션 처리 같은 핵심 비즈니스 로직 구현, 컨트롤러와 레포지토리(DAO) 사이에서 중간 역할 수행
* DAO(Data Access Object) 또는 Repository는 DB와 직접 소통하는 역할 → SQL 쿼리 실행, 엔티티 저장/조회/삭제 담당
* Service는 DAO를 이용해 비즈니스 로직을 처리하는 계층 → 단순 DB 호출 이상의 처리나 트랜잭션 관리, 복잡한 연산 등을 담당 → 클라이언트 요청에 대해 실제 "일"을 수행하는 곳
* 클라이언트가 컨트롤러에 요청 → 컨트롤러가 서비스를 호출 → 서비스가 DAO를 호출 → DAO가 DB와 통신 → 서비스가 결과를 받아 가공 → 컨트롤러에 반환 → 클라이언트에게 응답
* 역할 구분을 명확히 하기 위해, 유지보수성과 가독성을 높임, 트랜잭션 처리나 예외처리 같은 부가기능 적용에 용이함
* 스프링 컨테이너가 @Service 붙은 클래스를 자동으로 인식하여 관리함
* 스프링 부트 프로젝트에서는 기본적으로 컴포넌트 스캔(Component Scan) 을 통해 특정 패키지 내에 있는 클래스들을 찾아서 빈(Bean)으로 등록함
* @Service, @Component, @Repository, @Controller, @RestController 같은 어노테이션이 붙은 클래스들은 스프링 빈으로 등록됨
* 빈으로 등록되면, 스프링 컨테이너가 관리하는 객체가 됨
* 다른 클래스에서 @Autowired 혹은 생성자 주입 방식으로 이 빈을 의존성 주입(DI, Dependency Injection) 받을 수 있음


### @RequestBody
* HTTP 요청 본문(body) 에 있는 데이터를 Java 객체로 변환해서 매핑해 줌
* 일반적으로 JSON 형식의 데이터를 객체로 받을 때 사용함, 내부적으로 Jackson(ObjectMapper)을 사용해 JSON → 객체로 변환


### @PathVariable
* URL 경로 중 일부를 변수로 받을 때 사용
* RESTful API에서 자주 사용됨, URL 경로에 있는 {} 부분의 값을 매핑해 줌


### @RequestParam
* 쿼리 파라미터(요청 주소 뒤에 붙는 ?key=value)를 받을 때 사용
* 파라미터가 URL에 포함됨, 기본형이나 간단한 값 받을 때 유용


### @Param
* JPA의 @Query에서 쿼리 파라미터로 값을 바인딩할 때 사용함
* @Repository 안에서 SQL 쿼리에 파라미터 넘길 때 주로 사용


### @Query
* Spring Data JPA에서 직접 JPQL(Java Persistence Query Language)이나 네이티브 SQL 쿼리를 작성해서 실행하고 싶을 때 사용하는 어노테이션
* 복잡한 조건이나 자동으로 만들어주는 메서드 이름으로 표현하기 어려운 쿼리를 직접 지정할 수 있게 도와줌
* Spring Data JPA에서는 메서드 이름만으로도 쿼리를 자동 생성해주지만, 복잡한 쿼리나 명확하게 지정하고 싶은 경우에는 직접 쿼리를 작성할 수 있음


### @SpringBootApplication
* 스프링 부트 애플리케이션의 진입점(Main 클래스)에 붙는 어노테이션
* @SpringBootConfiguration, @EnableAutoConfiguration, @ComponentScan 세 개의 어노테이션을 합쳐 놓은 메타 어노테이션
* 스프링의 설정 파일 역할을 한다는 의미 (기존의 @Configuration), 자동 설정을 활성화 – 스프링이 클래스패스를 보고 자동으로 Bean 등록 등 구성, 현재 패키지 하위에 있는 @Component, @Service, @Repository, @Controller 등을 자동으로 스캔해서 Bean으로 등록하는 어노테이션 세 개
* 스프링 부트를 실행할 수 있도록 필요한 설정을 모두 포함하기 때문에 메인 클래스에 붙이면 그 아래 패키지의 모든 컴포넌트를 자동 등록하고 실행 준비를 해 줌
* @SpringBootApplication → @ComponentScan 포함되어 있어서 메인 클래스가 속한 패키지와 그 하위 패키지를 스캔해서 다음 어노테이션이 붙은 클래스를 찾음
* @Component, @Service, @Repository, @Controller, @RestController, @Configuration 등
* 찾은 클래스들을 Spring Bean으로 등록함
* 등록된 클래스들은 스프링 컨테이너 (ApplicationContext) 에 올라감
* 이 말은 스프링이 직접 객체를 생성해서 관리한다는 뜻
* Bean = 스프링이 관리하는 객체
* 자바에서 객체는 new 키워드로 만들지만, Spring에서는 직접 new로 만들지 않고, 스프링이 @Service, @Component 등을 통해 자동으로 객체를 만들고 필요한 곳에 주입(DI) 해줌
* 이렇게 스프링이 만들어서 관리하는 객체가 바로 Bean
* Bean 등록 = 스프링이 "이 클래스 인스턴스는 내가 관리할게!" 하고 컨테이너에 올리는 것
* 이렇게 등록된 클래스는 어디에서든 의존성 주입(@Autowired, 생성자 등)으로 가져다 쓸 수 있음
* main() 실행 → @SpringBootApplication → @ComponentScan (자동 스캔) → @Service, @Controller 등 어노테이션 가진 클래스 찾기
→ 스프링 컨테이너에 Bean 등록 → 필요한 곳에 Bean 주입 (DI) → 애플리케이션 실행 완료
* 스프링이 객체 생성 + 관리해주면 필요한 곳에 자동으로 주입해줌 → 코드 간결 + 유지보수 쉬움 + 테스트 용이
* ex) MemberService가 @Service로 Bean 등록돼 있으면 MemberController에 자동으로 생성자 주입됨 우리는 new 안 써도 객체가 알아서 연결됨!
→ 이것이 스프링의 의존성 주입(DI)


### @EnableScheduling
* 스프링에서 스케줄링(예약 실행) 기능을 사용하기 위해 활성화하는 어노테이션으로, @Scheduled 어노테이션을 사용하려면 반드시 필요
* 어떤 작업을 주기적으로 실행하거나, 특정 시간마다 실행하고 싶을 때 스케줄링 기능을 켜야 함
* 사실 배치 처리나 정기 작업이 필요한 경우에는 반드시 필요하지만 REST API 중심의 웹 애플리케이션인 경우면 굳이 필요하지는 않음


### @RestControllerAdvice
* @ControllerAdvice + @ResponseBody의 조합
* 전역(Global) 예외 처리 클래스임을 나타냄
* 모든 @RestController에서 발생하는 예외를 이 클래스에서 처리할 수 있게 해줌
* @ResponseBody를 포함하고 있어 JSON 형태로 응답을 반환함
* 여러 컨트롤러에서 공통된 예외 처리 로직을 한 곳에서 관리하고 싶을 때 사용


### @ExceptionHandler(Exception.class)
* 이 어노테이션은 해당 메서드가 특정 예외를 처리하는 예외 처리기(Exception Handler)임을 의미함
* Exception.class는 모든 예외의 최상위 타입이기 때문에, 이 메서드는 모든 종류의 예외를 catch-all 방식으로 처리
* Spring이 컨트롤러에서 예외가 발생했을 때 다음 순서로 예외 처리자를 찾고, @ExceptionHandler(특정 예외) 메서드가 @Controller 또는 @ControllerAdvice에 있는지 확인 후 가장 먼저 매칭되는 메서드가 호출되어 응답을 반환함
* 없으면 DefaultErrorAttributes 같은 기본 에러 응답 처리기로 넘어감


### @Priority(Integer.MAX_VALUE)
* @Priority는 Bean의 우선순위를 지정
* 여러 개의 @RestControllerAdvice가 존재할 때, 어떤 순서로 적용할지를 Spring이 판단할 수 있게 함
* 숫자가 작을수록 우선순위가 높고, 클수록 낮음
* 예를 들어 GlobalExceptionHandler는 최후의 방어선 역할을 하기 때문에 @Priority(Integer.MAX_VALUE)로 설정
* 더 특화된 예외 핸들러에 기회를 먼저 주고, 끝까지 처리되지 않은 예외만 GlobalExceptionHandler가 처리하도록 구성


### @Slf4j
* 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음
* 로깅 레벨은 (많은 로깅) trace > warn > info > debug > error (적은 로깅) 순


### @MappedSuperclass
* 공통 필드를 갖는 부모 클래스에 붙여서, 자식 엔티티들이 해당 필드를 상속받아 테이블 컬럼으로 사용할 수 있도록 하는 어노테이션
* BaseEntity 자체는 테이블로 생성되지 않으면서 createdAt, updatedAt 같은 필드가 자식 클래스에 컬럼으로 추가될 수 있도록 함


### @EntityListeners(AuditingEntityListener.class)
* JPA의 Auditing 기능을 활성화해서, @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy 등을 사용할 수 있게 해주는 어노테이션
* 이 기능을 사용하기 위해선 @EnableJpaAuditing도 설정 클래스에 선언해야 함
* @CreatedDate	엔티티가 처음 저장될 때 시간 자동 입력, @LastModifiedDate	엔티티가 수정될 때마다 시간 자동 업데이트, @CreatedBy	생성자 ID 자동 입력 (스프링 시큐리티 연동 가능), @LastModifiedBy	수정자 ID 자동 입력


### @Configuration
* 스프링에서 설정(설정 클래스)을 정의할 때 사용하는 어노테이션
* 자바 코드로 스프링 빈(Bean)을 등록하고 설정할 수 있게 해줌
* @Configuration이 붙은 클래스는 스프링 컨테이너가 빈 팩토리 역할을 하도록 인식해서, 그 안에 정의된 @Bean 메서드들을 실행하고 반환된 객체들을 빈으로 등록함


### @Bean
* 스프링 컨테이너에 직접 빈(Bean) 객체를 등록할 때 사용하는 어노테이션
* 주로 @Configuration이 붙은 설정 클래스 내에서 메서드에 붙여서, 해당 메서드가 반환하는 객체를 빈으로 등록함
* 메서드가 실행되어 반환된 객체를 스프링이 관리하는 싱글톤 빈으로 등록함
* 싱글톤 빈이란 스프링 컨테이너 내에서 해당 타입의 객체가 하나만 생성되어 공유되는 것
* 스프링 컨테이너가 애플리케이션 실행 시점에 빈을 생성해서 내부에 보관, 이후 의존성 주입 요청이 오면 같은 빈 인스턴스를 반환, 즉, 클래스 타입별로 하나의 인스턴스가 전역으로 사용됨
* 만약 싱글톤 빈이 아니라면 요청할 때마다 새로운 MemberService 객체가 생성되겠지만, 싱글톤 빈이라면 하나만 생성되어 재사용됨
* 등록된 빈은 의존성 주입(DI) 대상이 됨
* 필요하면 @Bean 메서드에 다양한 속성을 넣어 빈 생성 방식을 조절할 수도 있음


### @EnableWebSecurity
* 스프링 시큐리티(Security) 기능을 웹 애플리케이션에 활성화시키는 어노테이션
* 이 어노테이션이 붙은 클래스에서 보통 보안 설정(필터 체인, 권한, 인증 방법 등)을 커스터마이징 함
* @EnableWebSecurity를 사용하면 스프링 시큐리티가 자동으로 웹 보안을 적용하도록 설정되고, 개발자가 직접 SecurityFilterChain 빈을 만들어 보안 정책을 세밀하게 제어할 수 있음


### @Transactional
* 트랜잭션(transaction) 처리를 위해 사용하는 어노테이션
* 메서드 또는 클래스에 붙여서 해당 범위 내에서 실행되는 DB 작업들을 하나의 트랜잭션 단위로 묶어줌
* @Transactional이 붙은 메서드가 실행될 때 스프링은 트랜잭션을 시작하고, 메서드가 성공적으로 끝나면 트랜잭션을 커밋(commit)하고, 예외가 발생하면 트랜잭션을 롤백(rollback)함
* 클래스에 붙이면 그 클래스 내 모든 public 메서드에 자동으로 적용됨
* 선언적 트랜잭션 관리 방식으로, 직접 트랜잭션 코드를 작성하지 않아도 됨
* 내부적으로는 프록시 패턴을 사용해 트랜잭션 관리를 적용함
* 프록시 패턴은 "대리자" 패턴이라고도 하며, 실제 객체에 접근할 때 그 앞에서 대리 객체(프록시)가 중간에 끼어서 처리하는 디자인 패턴
* 클라이언트가 실제 객체 대신 프록시 객체를 호출, 프록시가 내부에서 실제 객체를 호출하기 전에 필요한 작업 수행, 실제 객체를 호출하고 결과를 반환
* @Transactional, @Async, @Cacheable 같은 어노테이션이 붙은 메서드를 호출할 때 스프링은 해당 빈을 프록시 객체로 감싸서, 메서드 호출 시 트랜잭션 시작, 커밋, 예외 처리 등 추가 작업을 수행함, 즉, 실제 비즈니스 로직 수행 전후로 필요한 기능을 부가

```plaintext
클라이언트 --> [프록시 객체] --> [실제 객체]
                    ↑
        트랜잭션 시작 / 커밋 / 롤백 등 부가 기능 수행
  ```


### @PostMapping
### @GetMapping
### @DeleteMapping
### @PutMapping
* Http 메시지는 클라이언트와 서버 사이에서 데이터가 교환되는 방식을 의미하며, Response와 Request의 두 가지 유형이 존재함
* 요청과 응답은 Start line, Http headers, body 3개의 정보로 하나의 메시지를 구성함
* Start line에는 HTTP method(GET, POST, PUT, DELETE), URL 등의 정보가 포함되어 있음
* Body에는 요청 데이터가 포함되어 있음
* 위 어노테이션들은 이런 정보를 컨트롤 하기 위해 사용됨
* POST 요청 중 특정 URL에 해당되는 요청을 받는 것으로 정의함