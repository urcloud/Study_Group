# JPA란?
* JPA는 객체-관계 매핑(ORM)을 위한 표준 명세인 인터페이스
* Java Persistence API의 약자로, 자바 애플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스와 어노테이션의 표준 집합을 정의함
* JPA는 라이브러리가 아닌 인터페이스
* 자바 어플리케이션에서 관계형 데이터베이스를 어떻게 사용해야 하는지를 정의하는 한 방법이고, 단순한 명세이기 때문에 구현이 없음
* 다양한 ORM 프레임워크 (Hibernate, EclipseLink, OpenJPA 등)에서 구현할 수 있는 공통 API를 제공함

## Hibernate란?
* JPA의 구현체로, 인터페이스를 직접 구현한 라이브러리
* JPA와 Hibernate의 관꼐는 자바의 interface와 해당 interface를 구현한 class와 같은 관계
* Hibernate는 JPA의 모든 기능을 지원하며 객체와 관계형 데이터베이스 간의 매핑을 자동으로 처리해서 개발자가 일일이 SQL쿼리를 작성하지 않도록 도와줌
* JPA를 사용하기 위해서 반드시 Hibernate만을 사용할 필요는 없음 (다른 JPA 구현체를 사용해도 됨)
* Hibernate가 SQL을 직접 사용하지 않는다고 해서 JDBC API를 사용하지 않는 것은 아님
* Hibernate가 지원하는 메소드 내부에서는 JDBC API가 동작하고 있으며, 단지 개발자가 직접 SQL을 작성하지 않을 뿐
* JPA의 핵심인 EntityManagerFactory , EntityManager , EntityTransaction 을 Hibernate에서는 각각 SessionFactory , Session , Transaction 으로 상속받고 각각 Impl로 구현하고 있음

## Spring Data JPA란?
* JPA 기반 애플리케이션 개발을 보다 간편하게 만드는 라이브러리
* DB에 접근할 때 Repository를 정의해 사용하는데, 이 Repository가 Spring Data JPA의 핵심
* Spring Data JPA는 Spring에서 제공하는 모듈 중 하나로, JPA 위에 추가적인 기능을 제공해 JPA 기반 애플리케이션 개발을 보다 간편하게 만드는 라이브러리
* Repository라는 인터페이스를 제공함으로써 이루어짐
* Repository 인터페이스에 정해진 규칙대로 메서드를 입력하면, Spring이 알아서 해당 메서드 이름에 적합한 쿼리를 날리는 구현체를 만들어서 Bean으로 등록해줌
> ex) findAll, findById 등
* Spring Data JPA를 사용하지 않고 JPA만 사용하려면 EntityManager을 사용해 데이터베이스 기능을 사용해야함
* EntityManager은 영속성 컨텍스트가 관리하는 엔티티 인스턴스의 집합

## 영속성 컨텍스트
* 영속성 컨텍스트는 JPA에서 엔티티를 관리하기 위해서 만들어져있는 어떤 공간
* 비영속은 영속성 컨텍스트와 무관한 일반적인 자바 객체인 상태, 영속은 영속성 컨텍스트에 주입 또는 관리되고 있는 상태
* 준영속은 영속화 되었다가 분리된 경우, 삭제는 영속성 컨텍스트에서 아예 삭제된 상태
* JPA에서 영속성 컨텍스트는 쉽게 말해서 DB와 시스템 간의 중개자 역할을 함

## 영속성 컨텍스트를 쓰는 이유
* 캐싱(1차 캐시)
> 데이터를 조회하는 기능을 처리할 때, DB에서 바로 조회하는 것이 아니라 영속성 컨텍스트에서 먼저 조회
* 영속된 엔티티의 동일성 보장 (1차 캐시의 연장선)
> 영속되어 있는 동일한 id를 갖는 엔티티를 2번 조회한다고 가정했을 때, 조회된 각각의 객체는 동일한 주소값을 가짐. 정확히 동일한 객체
* 쓰기 지연
> 트랜잭션을 commit하는 순간에 영속성 컨텍스트의 엔티티에 대한 쿼리를 DB에 실행
* 변경 감지(Dirty checking)
> 영속된 엔티티를 수정하면, 트랜잭션이 commit되는 순간 변경 쿼리가 DB에 실행됨

## EntityManager이란?
* JPA를 사용하기 위해서는 Database 구조와 매핑된 JPA Entity들을 먼저 생성하게 됨
* 또한 모든 JPA의 동작은 이 Entity들을 기준으로 돌아가게 되는데, 이 Entity들을 관리하는 역할을 하는 것이 EntityManager
* JPA는 한 요청 당 하나의 EntityManager을 사용하고, 각 EntityManager들은 정해진 영속성 컨텍스트를 참조하게 됨
* 이렇게 만들어진 EntityManager로 데이터를 다루려면 가장 먼저 Entity가 영속화 되어 있어야 함
> ex) 요청 - EntityManager 생성 - Entity들을 영속성 컨텍스트에 생성하고 Entity 영속화 - EntityManager가 영속성 컨텍스트 기반으로 CRUD 요청 처리

## Database, Datasource, Hibernate, JPA 상호작용
* 코드에서 Repository, Entity 등을 작성하면 JPA가 Repository 요청을 인터페이스로 받음
* 내부에서 Hibernate가 Entity를 SQL로 변환해서 DataSource를 통해 Database로 쿼리를 날림
* Database가 결과를 주면 다시 객체로 변환해서 리턴
* 코드 (JPA) → SQL (Hibernate) → DB 연결 (Datasource) → Database
* Database는 MySQL 등 진짜 데이터가 있는 곳, Datasource는 DB 연결을 관리하는 객체
* Hibernate는 객체(Entity)를 SQL로 변환하고 SQL 결과를 객체로 다시 바꾸는 라이브러리, JPA는 Java 코드로 DB를 다룰 수 있게 만든 API 표준
* Spring Boot Starter (spring-boot-starter-data-jpa)를 쓰면 DataSource는 자동 생성, Hibernate는 JPA 구현체로 자동 설정, JPA (EntityManagerFactory)도 자동 설정

## 좋아요한 사람들 배열로 집어넣을 수 있는지?
* JPA 엔티티 설계에서는 배열로  여러 members를 직접 study 안에 저장하는 식으로 매핑할 수 없음
* RDB(관계형 데이터베이스)는 배열 저장을 권장하지 않고 다대다 관계가 있을 때 Likes처럼 따로 중간 테이블로 풀어줘야 함 (정규화된 설계)
* 배열 저장 방식은 NoSQL에서 주로 씀
* 저장 공간 최적화 때문에 위 같은 고민중이라면 인프라, 운영 등으로 해결하는게 맞음. 요즘 DB는 좋아요 수천만 건 저장하는 데 문제가 없음 (Redis 캐시 활용)

## DAO란?
* DB에 직접 접근하여 데이터를 조회, 삽입, 수정, 삭제(CRUD) 하는 객체
* JPA에서는 DAO를 Repository라는 이름으로 추상화하고 있음

## DAO vs DTO
* DTO는 데이터를 한 곳에서 다른 곳으로 옮기기 위한 객체
* 주로 Controller ↔ Service ↔ View 사이에서 값 전달 용도로 사용함
* DAO는 DB에 CRUD를 수행하는 기능 및 DB 접근 전담, DTO는 데이터를 포장해서 전달 및 데이터 전달 전담

## @JoinColumn(name = "") 이름 명시하는 이유
* Hibernate는 기본적으로 <필드명>_<참조하는 엔티티의 기본 키 이름> 규칙으로 컬럼 이름을 자동으로 만듦
* 명확성과 가독성 향상 때문에 이름을 항상 명시함
* 개발자 입장에서 DB 컬럼명이 정확히 뭔지 명확하게 알 수 있고, 자동 생성에 의존하지 않고, 읽는 순간 컬럼명이 보이기 때문에 이름을 명시함

## @OneToMany 에서 private List<엔티티>로 선언하는 이유
* JPA는 양방향 관계를 만들 수 있음
* 예를 들어 Member 입장에서 comments, likes, studies 등 내가 가진 것들을 전부 List로 들고 있게 함
* 나중에 member.getComments(), member.getLikes() 이런 식으로 조회할 수 있도록 설계한 것

## @RestController, @RequestBody는 JSON만 가능한지?
기본은 JSON이나, jackson-dataformat-xml 등 의존성을 추가하면 Spring의 메시지 컨버터(MessageConverter)가 지원하는 형식이면 뭐든 받을 수 있음


## ManyToMany 어노테이션이 중간테이블을 만드는지?
* @ManyToMany는 중간 테이블을 무조건 생성함 
* 직접 엔티티를 만들지 않으면, JPA가 알아서 테이블을 생성하고 관리
* @ManyToMany는 실무에서 거의 권장되지 않음
* 중간 테이블에 추가 컬럼을 넣을 수 없고 관계 변경 추적이 어렵고 Cascade, orphanRemoval 같은 세밀한 연관관계 관리가 제한적임
* @ManyToMany 어노테이션을 사용하면 JPA가 자동으로 중간 테이블을 생성해서 관리함

