# LNC 교육 플랫폼 백엔드

> 회고 중심 봉사활동 플랫폼 백엔드 서버 (Spring Boot 3 + MySQL)

---

## 목차

1. [기술 스택](#기술-스택)
2. [사전 준비 (Docker 설치)](#사전-준비-docker-설치)
3. [프로젝트 클론](#프로젝트-클론)
4. [서버 실행](#서버-실행)
5. [서버 종료](#서버-종료)
6. [API 연동 정보](#api-연동-정보)
7. [자주 묻는 질문](#자주-묻는-질문)

---

## 기술 스택

| 항목 | 버전 |
|------|------|
| Java | 21 |
| Spring Boot | 3.4.3 |
| MySQL | 8.0 |
| 인증 | JWT (Bearer Token) |

---

## 사전 준비 (Docker 설치)

> **Docker를 처음 쓴다면** 아래 순서대로 따라 하세요.

### 1. Docker Desktop 설치

[https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop) 에 접속해서  
본인 OS에 맞는 버전을 다운로드한 뒤 설치합니다.

- **Mac (Apple Silicon)** → `Mac with Apple Chip` 선택
- **Mac (Intel)** → `Mac with Intel Chip` 선택
- **Windows** → `Windows` 선택

### 2. 설치 확인

터미널(또는 PowerShell)을 열고 아래 명령어를 입력하세요.  
버전 번호가 뜨면 설치 성공입니다.

```bash
docker --version
docker compose version
```

---

## 프로젝트 클론

```bash
git clone https://github.com/<레포지토리-주소>.git
cd lnc-edu-platform-be
```

---

## 서버 실행

> 아래 명령어 **하나**로 MySQL + Spring Boot 서버가 동시에 실행됩니다.

```bash
docker compose up --build
```

| 옵션 | 설명 |
|------|------|
| `--build` | 소스 코드가 바뀌었을 때 이미지를 새로 빌드합니다. 처음 실행할 때 반드시 붙이세요. |

> 처음 실행 시 이미지 다운로드 및 빌드 때문에 **5~10분** 정도 걸릴 수 있어요.

### 서버가 켜진 것을 확인하는 방법

터미널에 아래 로그가 뜨면 정상입니다.

```
lnc_app | Started EducationApplication in X.XXX seconds
```

브라우저에서 [http://localhost:8080](http://localhost:8080) 에 접속해서 응답이 오면 성공입니다.

### 백그라운드에서 실행하기 (터미널을 닫아도 계속 동작)

```bash
docker compose up --build -d
```

---

## 서버 종료

```bash
docker compose down
```

> DB 데이터를 완전히 초기화하고 싶을 때

```bash
docker compose down -v
```

---

## API 연동 정보

| 항목 | 값 |
|------|-----|
| Base URL | `http://localhost:8080` |
| 응답 형식 | `application/json` |
| 인증 방식 | `Authorization: Bearer <액세스_토큰>` |

### 공통 응답 형식

```json
{
  "success": true,
  "data": { ... },
  "message": "메시지",
  "timestamp": "2026-05-25 10:30:00"
}
```

### 주요 엔드포인트

| 기능 | 메서드 | URL |
|------|--------|-----|
| 회원가입 | `POST` | `/api/auth/signup` |
| 로그인 | `POST` | `/api/auth/login` |
| 게시글 목록 | `GET` | `/api/posts` |
| 게시글 작성 | `POST` | `/api/posts` |
| 댓글 작성 | `POST` | `/api/posts/{postId}/comments` |
| 공지사항 목록 | `GET` | `/api/notices` |
| 회고 작성 | `POST` | `/api/reflections` |

> 전체 API 명세는 [`docs/api-spec.md`](docs/api-spec.md) 를 참고하세요.

---

## 자주 묻는 질문

**Q. `docker compose` 명령어가 안 돼요.**  
→ Docker Desktop이 실행 중인지 확인하세요. 트레이(상단 메뉴바 또는 작업표시줄)에 고래 아이콘이 있어야 합니다.

**Q. 3306 포트가 이미 사용 중이라고 해요.**  
→ 로컬에 MySQL이 이미 설치되어 있을 수 있습니다. 로컬 MySQL을 종료하거나, `docker-compose.yml`에서 포트를 `"3307:3306"` 으로 바꿔보세요.

**Q. 8080 포트가 이미 사용 중이라고 해요.**  
→ `docker-compose.yml`에서 포트를 `"8081:8080"` 으로 바꾸고 Base URL도 `http://localhost:8081` 로 변경하세요.

**Q. 소스 코드를 수정했는데 반영이 안 돼요.**  
→ `docker compose up --build` 로 다시 빌드해야 합니다.
