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

@ManyToOne(fetch = FetchType.LAZY)
@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
>
> 
> 
> 
> 
> 

@JoinColumn(name = "member_id", nullable = false)
>
> 
> 
> 
> 
> 
> 

@RestController
>
> 
> 
> 
> 
>

@Service
>
> 
> 
> 
> 
> 

@RequestBody
@PathVariable


@Query


@EnableScheduling

@SpringBootApplication


@ExceptionHandler(Exception.class)

### @Slf4j
* 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음
* 로깅 레벨은 (많은 로깅) trace > warn > info > debug > error (적은 로깅) 순

@Priority(Integer.MAX_VALUE)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Configuration
@EnableWebSecurity
@Bean
@Transactional


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