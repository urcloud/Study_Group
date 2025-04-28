## Connection Pool이란?
* 미리 만들어둔 DB 연결 저장소로, 초당 수백건 요청이 들어오더라도 Connection Pool로 처리함
* Pool 패턴은 비용이 많이 드는 리소스를 미리 생성해두고 재사용하는 디자인 패턴
> ex) Thread Pool: Thread 생성/삭제 비용 절감, Connection Pool: DB 연결 생성/삭제 비용 절감
> Thread Pool: 요청 처리를 위한 작업 단위 관리, Connection Pool: 데이터베이스 연결이라는 특수 리소스 관리
* 새로운 데이터베이스 연결을 만드는 과정은 많은 비용이 들고, 해당 과정을 매 요청마다 반복하면 성능이 크게 저하됨

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