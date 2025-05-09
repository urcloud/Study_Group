## Connection Pool이란?
* 미리 만들어둔 DB 연결 저장소로, 초당 수백건 요청이 들어오더라도 Connection Pool로 처리함
* Pool 패턴은 비용이 많이 드는 리소스를 미리 생성해두고 재사용하는 디자인 패턴
> ex) Thread Pool: Thread 생성/삭제 비용 절감, Connection Pool: DB 연결 생성/삭제 비용 절감
> Thread Pool: 요청 처리를 위한 작업 단위 관리, Connection Pool: 데이터베이스 연결이라는 특수 리소스 관리
* 새로운 데이터베이스 연결을 만드는 과정은 많은 비용이 들고, 해당 과정을 매 요청마다 반복하면 성능이 크게 저하됨
* Connection Pool은 애플리케이션이 시작될 때, 설정에 맞춰 자동으로 생성됨
> 프레임워크 또는 라이브러리가 자동으로 생성하는데, Spring Boot에서는 DataSource가 커넥션 풀을 관리하는 객체
>
> DataSource 객체는 커넥션 풀의 설정 정보를 기반으로 커넥션 풀을 초기화하고 관리함
> 
> 이 객체는 Spring의 Bean으로 등록되어 ApplicationContext에 의해 관리됨

## HikariCP
* 스프링 부트 2.0부터는 HikariCP가 기본 Connection Pool로 채택됨
* HikariCP의 주요 설정값과 기본값
```yaml
spring:
  datasource:
    hikari:
      # 최대 연결 수
      maximum-pool-size: 10
      # 최소한 풀 안에 유지할 Connection 수
      minimum-idle: 10
      # 10분간 사용 안 하면 Connection 종료
      idle-timeout: 600000
      # Connection 빌리는데 30초 넘으면 실패
      connection-timeout: 30000
      # 연결의 최대 수명 (ms)
      max-lifetime: 1800000
```

## Thread와 Connection
* Thread는 프로그램 안에서 작업을 실행하는 흐름으로, 하나의 프로그램 안에서도 동시에 여러 작업을 할 수 있게 각각의 작업 흐름을 스레드라고 부름
> ex) 컴퓨터에서 음악을 들으면서 동시에 인터넷 브라우저 킴: 음악 재생하는 흐름 (스레드 1개), 웹페이지 불러오는 흐름 (스레드 1개) 별개의 스레드가 일함
* Thread Pool과 Connection Pool은 서로 균형이 중요
* Thread Pool이 Connection Pool보다 너무 크면 많은 스레드가 적은 수의 커넥션을 기다림 - 스레드들이 커넥션을 기다리며 대기 상태 - 결과적으로 응답 지연 발생
* Connection Pool이 Thread Pool보다 너무 크면 사용하지 않는 커넥션이 많아짐 - 데이터베이스 서버의 리소스 낭비 - 불필요한 메모리 사용
* 모든 스레드가 동시에 DB 작업을 하는 것이 아니므로, Connection Pool은 더 작게 설정하는 것이 균형잡힌 성정

## 요청 흐름
* 코드 → Repository 호출 - Hibernate가 Entity를 SQL로 변환 - Hibernate가 DataSource에서 Connection을 "빌림" - Connection을 통해 Database에 SQL 실행 - 결과를 가져옴 - Connection을 "반납" (닫지 않고 다시 풀에 넣음)
* [Client] → [서버] → [스레드 생성] → [Service 호출] → [Repository 호출] → [JPA 호출] → [Hibernate가 SQL 생성] → [Connection Pool에서 커넥션 빌림] → [DB에 SQL 전송] → [결과 반환] → [Response 작성] → [Client로 응답]
* 클라이언트가 서버에 요청을 보내면 HTTP 요청이 Spring Boot 서버(Tomcat 등)에 들어오고, 서버는 요청마다 스레드 하나를 할당
* 스레드는 Controller → Service → Repository 로 내려가면서 로직을 수행함
* Repository는 @Repository 또는 JpaRepository 같은 인터페이스를 통해 JPA를 호출, 이때 JPA는 직접 SQL을 쓰지 않고 Hibernate에 맡김
* JPA의 요청을 받아서 Hibernate가 동작하고, Hibernate가 진짜 SQL로 변환
* Hibernate가 DB에 접근할 때 Connection이 필요해서 Hibernate는 바로 Datasource (=Connection Pool) 에게서 DB 연결 빌림
* 이때 Connection Pool은 미리 만들어둔 Connection 중에서 Idle 상태로 놀고 있던 커넥션 하나를 꺼내서 Hibernate에게 줌
* Hibernate는 빌린 Connection을 통해 방금 만든 SQL을 DB에 날리고, 결과를 받아오면 객체(Entity)로 변환하거나 필요한 데이터를 가공해서 JPA로 결과를 넘김
* Service → Controller → 클라이언트 응답(Response)을 반환해 결과를 컨트롤러까지 올림
* 사용한 Connection은 Connection Pool에 다시 반납하고 스레드는 요청 처리가 끝났으면 스레드 풀로 돌아가거나 종료

## 웹의 process vs thread
* 프로세스는 실행 중인 프로그램으로, 운영체제에서 프로세스는 메모리, CPU 시간, 입출력 장치 등을 할당받아 독립적으로 실행됨
* 각 프로세스는 자신만의 주소 공간을 가지고 있으며, 다른 프로세스와 메모리를 공유하지 않음
* 스레드는 프로세스 내에서 실행되는 작은 단위로, 하나의 프로세스는 여러 개의 스레드를 포함할 수 있으며, 각 스레드는 프로세스 내에서 자원을 공유함
* 스레드는 경량화된 프로세스로 볼 수 있고, 여러 스레드가 하나의 프로세스 내에서 자원을 공유하면서 병렬 처리 작업을 수행함

## 동시성 제어
* 비관 락 
> 비관 락의 경우, DBMS가 주요 주체임
>
> 비관 락은 트랜잭션 수준에서 관리됨
>
> 데이터베이스가 동시성 문제를 해결하기 위해 특정 데이터를 잠그고, 트랜잭션이 데이터를 처리하는 동안 다른 트랜잭션이 해당 데이터를 수정하지 못하게 함

* 낙관 락
> 낙관 락의 경우, 애플리케이션이 주요 주체임
> 
> 데이터베이스가 아니라 애플리케이션 코드에서 관리됨
> 
> 낙관적 가정에 기반하여 동시성 문제를 해결하며, 여러 트랜잭션이 동시에 데이터를 수정할 확률이 낮다고 보고 잠금 없이 진행하고 데이터 충돌이 발생할 경우에만 해결 방법을 찾음

* 분산 락
> Redis와 같은 분산 시스템이 주요 주체임
> 
> 분산 락은 여러 서버가 있을 때, 공유 자원에 접근하기 위해 분산 시스템을 통해 락을 관리함
> 
> 데이터베이스나 애플리케이션에서 관리하는 락과는 달리, 분산 환경에서 락을 걸고 풀 수 있는 기능을 제공하는 시스템을 사용함