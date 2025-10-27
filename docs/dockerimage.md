## 1. 도커 허브 이미지 다운로드
```powershell
docker image pull diamol/ch03-web-ping
```
- diamol/ch03-web-ping 이미지를 도커 허브에서 내려받음
- 도커 이미지는 여러 레이어로 구성되어 있으며, 각각의 레이어는 작은 파일 단위로 내려받음
- 모든 레이어를 내려받아야 이미지 사용 가능

## 2. 컨테이너 실행
```powershell
docker container run -d --name web-ping diamol/ch03-web-ping

docker container logs web-ping
```
- -d 또는 --detach: 백그라운드 실행
- --name: 컨테이너에 이름 지정 가능
- 컨테이너는 환경 변수에 따라 동작을 조정할 수 있음
```powershell
docker container run --env TARGET=google.com diamol/ch03-web-ping
```
- --env 또는 -e: 환경 변수 지정 가능
- 환경 변수 TARGET을 통해 요청 대상 URL 변경 가능

## 3. Dockerfile 작성
- 기본 구조
```dockerfile
FROM diamol/node

ENV TARGET="blog.sixeyed.com"
ENV METHOD="HEAD"
ENV INTERVAL="3000"

WORKDIR /web-ping
COPY app.js .

CMD ["node", "/web-ping/app.js"]
```

- 주요 인스트럭션

| 명령어       | 설명                         |
| --------- | -------------------------- |
| `FROM`    | 기반 이미지 지정 (Node.js 런타임 포함) |
| `ENV`     | 컨테이너 환경 변수 설정              |
| `WORKDIR` | 컨테이너 작업 디렉터리 설정            |
| `COPY`    | 로컬 파일을 이미지에 복사             |
| `CMD`     | 컨테이너 실행 시 기본 명령 지정         |

## 4. 이미지 빌드 및 실행
```powershell
docker image build --tag web-ping .

docker container run -e TARGET=docker.com -e INTERVAL=5000 web-ping
```
- --tag: 이미지 이름 지정
- 마지막 .: 현재 디렉터리를 빌드 컨텍스트로 사용
- 빌드 후 docker image ls로 이미지 확인 가능

## 5. 이미지 레이어와 캐시
- Dockerfile 각 인스트럭션은 이미지 레이어 1:1 매핑
- 레이어는 읽기 전용이며 여러 이미지에서 공유 가능
- 캐시 활용: 
    - 이전 빌드와 동일한 레이어는 재사용 가능
    - 자주 수정되는 인스트럭션은 Dockerfile 뒤쪽에 배치

- 최적화 예시: 

```dockerfile
FROM diamol/node

CMD ["node", "/web-ping/app.js"]

ENV TARGET="blog.sixeyed.com" \
    METHOD="HEAD" \
    INTERVAL="3000"

WORKDIR /web-ping
COPY app.js .
```
- ENV 인스트럭션 통합, 자주 수정되는 COPY app.js는 뒤쪽 배치

## 6. 이미지 용량
- docker image ls는 논리적 용량 표시
- 실제 디스크 사용량 확인:
```powershell
docker system df
```
- 공유 레이어 덕분에 실제 사용 용량은 더 적음

## 7. 이미지 vs 컨테이너 구분
- 이미지: 읽기 전용인 파일 시스템 스냅샷 + 실행에 필요한 메타데이터(환경변수)로, 배포·공유 가능한 패키지
- 컨테이너: 이미지를 실행하여 생성된 실행 중인 인스턴스(프로세스)로, 읽기-쓰기 계층이 추가되어 상태(state)가 생김. 일시적이고(보통 휘발) 프로세스 단위로 동작함

- 비교 표

| 항목       |                                             이미지 (Image) | 컨테이너 (Container)                               |
| -------- | ------------------------------------------------------: | ---------------------------------------------- |
| 역할       |                                           배포·재현 가능한 패키지 | 이미지를 실행한 런타임 인스턴스                              |
| 상태       |                                           불변(immutable) | 가변(stateful) — 변경 가능                           |
| 저장 위치    |                        로컬 이미지 캐시 / 레지스트리 (Docker Hub 등) | 호스트의 컨테이너 런타임 / 프로세스                           |
| 생성/삭제 명령 | `docker image build`, `docker image pull`, `docker rmi` | `docker run`, `docker start/stop`, `docker rm` |
| 데이터 유지   |                                      이미지 자체는 상태 저장하지 않음 | 컨테이너 내부 변경은 휘발(볼륨 사용 시 지속 가능)                  |
| 파일시스템    |                           여러 **레이어(layer)** 의 합성(읽기 전용) | 이미지 위에 **읽기-쓰기 계층**(copy-on-write)이 추가됨        |
| 수정 방법    |                                   새 이미지 빌드 (Dockerfile) | `docker commit`으로 스냅샷 가능하지만 권장 X               |
