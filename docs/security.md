# Spring Security란?
> 인증Authentication 인가Authorization 에 대한 처리를 위임하는 별도의 프레임 워크
> 
> Spring Security 의 인증 인가는 대부분을 Filter 의 흐름에 따라 처리함


## Spring Security의 동작과정
* DispatcherServlet 에 도달하기 전에 Filter에서 요청을 가로채 인증과 인가 에 대한 처리를 해줌
* 사용자가 로그인 정보 id password를 이용해 로그인 인증 Authentication 요청
* AuthenticationFilter 가 정보를 가로채 UsernamePasswordAuthentication Token 을 생성하여 (Authentication 객체) AuthenticationManager 에게 Authentication 전달
* AuthenticationManager 인터페이스를 거쳐 Autehntication Provider 에게 정보 전달, 등록된 AuthenticationProvider 를 조회하여 인증 요구
* AutenticationProvider는 UserDetailService 를 통해 입력받은 사용자 정보를 DB에서 조회
  * support()메소드가 실행 가능한지 체크
  * authenticate() 메소드를 통해 DB에 저장된 이용자 정보와 입력한 로그인 정보 비교
* DB 이용자 정보 : UserDetailService 의 loadUserByUsername() 을 통해 불러옴
  * 입력 로그인 정보 : 받아온 Authentication 객체 UsernameAuthentication Token
  * 일치하는 경우 Authentication 반환
* AuthenticationManager 는 Authentication 객체를 AutenticationFilter 로 전달
* AuthenticationFilter 는 전달 받은 Authentication 객체를 LoginSuccessHandler로 전송 하고 SecurityContextHolder 에 담기
* 성공시 AuthenticationSucesshandle 실패시 AuthenticationFailureHandle 실행


## SpringSecurity Filter
> 유저네임과 비밀번호를 2 단계 안의 인증 객체로 변환하며, 객체가 생성되면 Spring Security Filter 들은 이 요청을 인증 관리자 (authentication manager)
에게 넘김
* 인증 권한 부여 수행
* 로그인 페이지 표시
* HDT 포지션 내 인증 정보 저장 등
* Authorization Filter - 엔드 유저가 접근하고자 하는 URL에 접근을 제한
* doFilter - 공개 URL인지, 보안 URL 인지 체크 후 접근 허용 혹은 접근 제한


## Authentication Manager
> 실질적인 인증 로직을 관리하는 인터페이스
* 웹 애플리케이션 안에 어떤 인증 제공자 (authentication provider) 가 존재하는지 확인
* 특정 요청에 대해 유요한 인증 제공자가 어떤 것인지 확인하는 것이 인증 관리자의 책임
* 단순히 로그인에 실패해서 응답을 엔드 유저에게 반환하는 것이 아닌 가능한 모든 인증 제공자들을 시도하고 모든 시도에서 실패했을 때 비로소 인증이 실패했다고 응답


## Authencitation Providers
> 실질적인 인증 로직을 정의하는 부분
* 어떤 데이터베이스나, LDAP 서버에서 혹은 권한 부여 서버나 캐시에서 유저 자격 증명을 할 것인지 서술


## UserDetailsManager, UserDetailsService
> 공통 로직을 Sprng Security에서 이미 구현해 놓은 인터페이스와 클래스.아이디, 비밀번호 대조에 사용됨
* Password Encoder 와 같이 쓰임
* 가장 처음에 있는 인터페이스는 UserDetailsService, 행하려는 모든 인증 작업은 브라우저내의 입력된 유저정보, DB내에 있는 저장 정보를 load 하는 것
* loadUsersByUsername 이라는 이름의 단일 추상 메소드를 가진 별도의 인터페이스가 별도로 있음
* UserDetailSevice 를 확장하는 또 다른 인터페이스가 있는데 UserDetailsManager이며 유저 세부정보를 관리


## Security Context
> Spring Security Filter는 생성된 인증 객체를 보안 컨텍스트 안에 저장함
* 인증이 성공적이었는지 유무와, 세션 ID 등을 저장한다. 이미 인증정보가 저장 되어 있다면 재 접속 시에 다시 인증요구를 하지 않음


## SecurityFilterChain
> default Security Configurations 안에는 defaultSecurityFilterChain 클래스가 있는데 HttpSecurity 를 매개변수로 받아 모든 URL을 보호하게 만들고 있음


## 동작과정
1. 사용자 요청
> 요청은 DispatcherServlet에 도달하기 전에 Spring Security FilterChain이 먼저 처리함

2. 인증 필터 작동 (UsernamePasswordAuthenticationFilter)
> 로그인 폼의 ID/PW를 기반으로 UsernamePasswordAuthenticationToken 객체 생성, 이 객체는 아직 인증되지 않은 상태

3. AuthenticationManager에게 인증 요청
> 인증 토큰을 AuthenticationManager 에 전달, 기본 구현체는 ProviderManager, 이 매니저는 등록된 AuthenticationProvider 리스트 중 하나를 골라서 인증을 위임

4. AuthenticationProvider가 인증 수행
> 내부적으로 UserDetailsService를 호출해서 DB나 메모리에서 사용자 정보 조회, 그리고 입력받은 비밀번호와 DB에서 가져온 암호화된 비밀번호를 PasswordEncoder로 비교

5. 인증 성공
> 비밀번호 일치 시, 인증된 Authentication 객체 생성, 이 객체를 SecurityContextHolder 에 저장함 (보안 컨텍스트)

[사용자 요청]
↓
[Spring Security Filter Chain]
↓
[UsernamePasswordAuthenticationFilter]
↓
[AuthenticationManager]
↓
[AuthenticationProvider]
↓
[UserDetailsService.loadUserByUsername]
↓
[비밀번호 일치 확인]
↓
인증 성공 → SecurityContextHolder 저장 → 성공 핸들러 호출
인증 실패 → 실패 핸들러 호출


## AuthenticationProvider란?
* 사용자의 인증을 검증하는 실제 주체로,  아이디와 비밀번호가 맞는지 확인하는 실제 로직을 담당하는 클래스
1. 클라이언트 요청
2. UsernamePasswordAuthenticationFilter 실행
3. 요청에서 username, password 추출 → Authentication 객체 생성 (미인증 상태)
4. AuthenticationManager가 Authentication 객체를 AuthenticationProvider에 전달
5. AuthenticationProvider가 사용자 검증 수행 (DB에서 사용자 조회, 비밀번호 일치 여부 확인)
6. 인증 성공 → 인증된 Authentication 객체 반환 (권한 포함)
7. SecurityContext에 인증 객체 저장 → 로그인 완료
