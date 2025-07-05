# HttpSession이란?
* HttpSession은 서버가 클라이언트(브라우저)별로 정보를 유지하기 위한 저장소
* 브라우저가 로그인 요청을 보내면 서버는 HttpSession을 생성하거나 기존 세션을 재사용해서 정보를 저장함
* 이 세션은 클라이언트가 로그아웃하거나 세션이 만료되기 전까지 유지됨
* 서버 메모리에 저장됨
* 클라이언트는 세션을 식별하기 위해 JSESSIONID 쿠키를 서버로부터 받아 브라우저에 저장함

## 동작과정
* 클라이언트 → 서버: 로그인 정보를 담은 POST 요청 전송
* 컨트롤러 실행
* 서비스 실행
* 서버는 클라이언트에게 LoginResponse와 함께 JSESSIONID 쿠키를 내려줌
* 클라이언트는 이후 요청부터 JSESSIONID=ABC123XYZ 쿠키를 포함하여 서버에 전송
* 서버는 이 JSESSIONID를 이용해서 세션을 식별하고, 그 세션에 있는 userId 값을 사용
* session.setAttribute("userId", member.getId()) 이 구문이 로그인 정보(사용자 ID)를 세션에 저장하는 것
* 클라이언트가 로그인 성공 후 받는 JSESSIONID 쿠키는 서버의 이 세션 객체와 매핑됨
* 로그인 이후 사용자는 JSESSIONID를 통해 세션에 접근하고, 서버는 그 세션에 저장된 정보를 바탕으로 로그인 상태를 유지함
* 클라이언트가 쿠키에 JSESSIONID를 넣어 요청하면, 서버는 해당 세션을 찾고, 그 안에 저장된 userId로 사용자를 식별
* 핵심은 서버에서 session.setAttribute("userId", member.getId()); 로 서버 세션에 로그인 상태 정보를 저장하지만, 이 세션 객체 자체를 클라이언트에 직접 보내는 게 아니라, 클라이언트와 서버가 세션 ID(cookie) 로 상태를 관리한다는 점
* 로그인 시 서버에서 세션 생성 & 세션 ID 발급함
* session.setAttribute("userId", member.getId());는 서버 측 메모리(또는 분산 세션 저장소)에 로그인 정보를 저장하는 것이고,
* 이때 클라이언트에게는 서버가 자동으로 생성한 세션 ID가 담긴 쿠키(JSESSIONID 등)를 HTTP 응답 헤더에 실어서 전달함
* 클라이언트가 쿠키를 저장하고, 이후 요청마다 자동으로 전송
* 클라이언트(브라우저)는 이 쿠키를 저장했다가, 다음부터 서버에 요청할 때마다 쿠키를 같이 보냄
* 서버는 들어오는 요청의 쿠키 세션 ID로 세션을 조회
* 서버는 요청에 담긴 세션 ID 쿠키를 보고 해당 세션에 저장된 "userId" 값을 꺼내서 로그인 상태를 확인함

## 인증 종류 및 설명
### 세션
서버가 로그인 정보를 서버 메모리나 DB에 저장하고, 클라이언트는 세션 ID만 쿠키로 보냄
* 로그인 성공 → 서버가 세션 ID 생성 후 저장
* 클라이언트에게 세션 ID를 쿠키로 전달
* 이후 요청마다 세션 ID 쿠키를 함께 전송
* 서버는 그 ID로 로그인 상태 확인

### 토큰
로그인 성공 시 토큰(JWT 등)을 발급하여 클라이언트가 직접 들고 다님, 서버는 상태를 기억하지 않음
* 로그인 성공 → JWT 토큰 생성 (서버 서명 포함)
* 클라이언트는 이 토큰을 로컬 스토리지나 쿠키에 저장
* 요청 시 Authorization: Bearer <token> 으로 전송
* 서버는 토큰을 검증만 하고 처리

### OAuth
구글, 네이버, 카카오 같은 외부 인증 제공자에게 인증을 위임하는 방식
* 사용자가 "카카오로 로그인" 클릭
* 카카오 로그인 → 인가 코드 발급
* 서버는 인가 코드를 카카오에 보내서 액세스 토큰 발급
* 이 토큰으로 사용자 정보 조회 및 로그인 처리

## OAuth2
### 동작과정
인가(클라이언트 애플리케이션이 인증된 사용자의 자원에 접근하기 위한 권한을 부여하는 것) 요청
* 사용자를 구글이나 카카오 로그인 페이지로 리다읽트 함
* Authorization Code 를 리다이렉트 URI로 전달

토큰 요청
* 클라이언트는 전달받은 code를 이용해서 Access Token을 요청

토큰 응답
* Access Token (필요시 Refresh Token도) 을 발급해줌
* refresh token이 있다면 access token이 만료될 때 refresh token을 통해 access token을 재발급 받아 재 로그인 할 필요없게끔 함

자원 접근
* 이 Access Token을 들고 사용자 정보 API에 요청

> 사용자(Resource Owner)는 서비스(client)를 이용하기 위해 로그인 페이지에 접근
> 
> 그럼 서비스(client)는 사용자(Resource Owner)에게 로그인 페이지를 제공하게 됨
> 로그인 페이지에서 사용자는 "페이스북/구글 으로 로그인" 버튼을 누름
> 
> 만일 사용자가 Login with Facebook 버튼을 클릭하게 되면, 특정한 url 이 페이스북 서버쪽으로 보내지게됨
> 
> 브라우저 응답(response) 헤더를 확인하면 다음 url내용을 확인 할 수 있음
```
https://resource.server/?client_id=1&scope=B,C&redirect_uri=https://client/callback
``` 
> 이는, 사용자가 직접 페이스북으로 이동해서 로그인을 입력해야 하는데, 저 링크가 대신 로그인으로 이동 하게끔 도와줌
> 
> redirect_uri 경로를 통해서 Resource Server는 client에게 임시비밀번호인 Authorization code를 제공함
> 
> 클라이언트로부터 보낸 서비스 정보와, 리소스 로그인 서버에 등록된 서비스 정보를 비교함
>
> 확인이 완료되면, Resource Server로 부터 전용 로그인 페이지로 이동하여 사용자에게 보여줌
>
> ID/PW를 적어서 로그인을 하게되면, client가 사용하려는 기능(scope)에 대해 Resource Owner의 동의(승인)을 요청함
> 
> Resource Owner가 Allow 버튼을 누르면 Resource Owner가 권한을 위임했다는 승인이 Resource Server 에 전달됨
> 
> 하지만, 이미 Owner가 Client에게 권한 승인을 했더라도 아직 Server가 허락하지 않음 
> 
> 따라서, Resource Server 도 Client에게 권한 승인을 하기위해 Authorization code를 Redirect URL을 통해 사용자에게 응답하고, 다시 사용자는 그대로 Client에게 다시 보냄
> 
> 이제 Client가 Resource Server에게 직접 url(클라이언드 아이디, 비번, 인증코드 ...등)을 보냄
> 
> 그럼 Resource Server는 Client가 전달한 정보들을 비교해서 일치한다면, Access Token을 발급하고 이제 필요없어진 Authorization code는 지움
> 
> 그렇게 토큰을 받은 Client는 사용자에게 최종적으로 로그인이 완료되었다고 응답
> 
> OAuth의 목적은 최종적으로 Access Token을 발급하는 것
> 
> Access Token이 기간이 만료되어 401에러가 나면, Refresh Token을 통해 Access Token을 재발급함


## 멀티파트 폼데이터
```html
<form action="/upload" method="POST" enctype="multipart/form-data">
  <input type="file" name="image" />
  <button type="submit">업로드</button>
</form>
```
* 이미지 파일을 문자로 생성하여 HTTP request body에 담아 서버로 전송하는 방법
* 폼 데이터(Form Data)를 여러 파트로 나누어 전송
* 파일 업로드 시 본인이 찍은 사진을 올리는 form의 경우 사진을 위한 설명인 input과 사진 파일을 위한 input이 2개가 들어가는데, 두 input 간에 content-type이 전혀 다름
* 두 종류의 데이터가 하나의 Http Request Body에 들어가야 하는데, 한 body에서 이 2 종류의 데이터를 구분해서 넣어주는 방법이 필요해져서 multipart가 등장함
* 이미지를 multi로 잘라서 보내는 것