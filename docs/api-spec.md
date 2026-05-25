# LNC 교육 플랫폼 백엔드 API 명세서

## 기본 정보

| 항목 | 값 |
|------|-----|
| Base URL | `http://localhost:8080` |
| 응답 형식 | `application/json` |
| 인증 방식 | JWT Bearer Token |

---

## 공통 응답 형식

모든 API는 아래 형식으로 응답합니다.

```json
{
  "success": true,
  "data": { ... },
  "message": "메시지",
  "timestamp": "2026-05-25 10:30:00"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `success` | Boolean | 요청 성공 여부 |
| `data` | Object / null | 응답 데이터 |
| `message` | String / null | 결과 메시지 |
| `timestamp` | String | 응답 시각 (`yyyy-MM-dd HH:mm:ss`) |

### 에러 응답 예시

```json
{
  "success": false,
  "data": null,
  "message": "에러 메시지",
  "timestamp": "2026-05-25 10:30:00"
}
```

| HTTP 상태 | 원인 |
|-----------|------|
| `400` | 유효성 검사 실패, 중복 값 |
| `401` | 인증 실패 (토큰 없음 / 만료) |
| `403` | 권한 없음 (작성자 불일치, ADMIN 전용 API) |
| `404` | 리소스 없음 |
| `500` | 서버 오류 |

---

## 인증이 필요 없는 API

---

## 인증 (Auth)

### 1. 회원가입

**`POST /api/auth/signup`**

**요청 바디**

```json
{
  "loginId": "student01",
  "password": "password123",
  "name": "홍길동",
  "studentId": "20230001"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `loginId` | String | O | 로그인 아이디 (최대 50자, unique) |
| `password` | String | O | 비밀번호 (BCrypt 암호화 저장) |
| `name` | String | O | 이름 (최대 50자) |
| `studentId` | String | O | 학번 (최대 20자, unique) |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "loginId": "student01",
    "name": "홍길동",
    "studentId": "20230001",
    "role": "USER"
  },
  "message": "Operation successful",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `400` | 필드 누락 / loginId 또는 studentId 중복 |

---

### 2. 로그인

**`POST /api/auth/login`**

**요청 바디**

```json
{
  "loginId": "student01",
  "password": "password123"
}
```

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "loginId": "student01",
      "name": "홍길동",
      "studentId": "20230001",
      "role": "USER"
    }
  },
  "message": "Operation successful",
  "timestamp": "2026-05-25 10:30:00"
}
```

| 토큰 | 만료 시간 |
|------|-----------|
| `accessToken` | 1시간 (3600초) |
| `refreshToken` | 30일 (2592000초) |

**역할(role) 종류**

| 값 | 설명 |
|----|------|
| `GUEST` | 게스트 |
| `USER` | 일반 사용자 (가입 시 기본값) |
| `MEMBER` | 정회원 |
| `ADMIN` | 관리자 |

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `400` | 필드 누락 |
| `401` | 아이디 또는 비밀번호 불일치 |

---

## 인증이 필요한 API

> 모든 요청 헤더에 아래를 포함해야 합니다.
>
> ```
> Authorization: Bearer {accessToken}
> ```

---

## 사용자 (User)

### 3. 내 정보 조회

**`GET /api/users/me`** — 인증 필요

**요청 파라미터**: 없음

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "loginId": "student01",
    "name": "홍길동",
    "studentId": "20230001",
    "role": "USER"
  },
  "message": "Operation successful",
  "timestamp": "2026-05-25 10:30:00"
}
```

---

## 회고 (Reflection)

### 4. 회고 작성

**`POST /api/reflections`** — 인증 필요

**요청 바디**

```json
{
  "title": "첫 번째 봉사 회고",
  "content": "오늘 봉사활동을 하면서..."
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | O | 제목 |
| `content` | String | O | 내용 |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "첫 번째 봉사 회고",
    "content": "오늘 봉사활동을 하면서...",
    "author": {
      "loginId": "student01",
      "name": "홍길동",
      "studentId": "20230001",
      "role": "USER"
    },
    "createdAt": "2026-05-25T10:30:00",
    "updatedAt": "2026-05-25T10:30:00"
  },
  "message": "Reflection created successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

---

### 5. 회고 단건 조회

**`GET /api/reflections/{id}`** — 인증 필요

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 회고 ID |

**성공 응답 `200`**: 회고 작성과 동일한 응답 형식

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `404` | 해당 ID의 회고 없음 |

---

### 6. 회고 전체 조회

**`GET /api/reflections`** — 인증 필요

**요청 파라미터**: 없음

**성공 응답 `200`**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "첫 번째 봉사 회고",
      "content": "...",
      "author": { "..." },
      "createdAt": "2026-05-25T10:30:00",
      "updatedAt": "2026-05-25T10:30:00"
    }
  ],
  "message": "Operation successful",
  "timestamp": "2026-05-25 10:30:00"
}
```

---

### 7. 회고 수정

**`PUT /api/reflections/{id}`** — 인증 필요 (작성자 본인만)

**요청 바디**: 회고 작성과 동일

**성공 응답 `200`**: 수정된 회고 (회고 작성과 동일한 형식)

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | 작성자 본인이 아닌 경우 |
| `404` | 해당 ID의 회고 없음 |

---

### 8. 회고 삭제

**`DELETE /api/reflections/{id}`** — 인증 필요 (작성자 본인만)

**성공 응답 `200`**

```json
{
  "success": true,
  "data": null,
  "message": "Reflection deleted successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | 작성자 본인이 아닌 경우 |
| `404` | 해당 ID의 회고 없음 |

---

## 게시글 (Post)

### 9. 게시글 작성

**`POST /api/posts`** — 인증 필요

**요청 바디**

```json
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "category": "자유",
  "isAnonymous": false
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | O | 제목 (최대 255자) |
| `content` | String | O | 내용 |
| `category` | String | X | 카테고리 (최대 50자) |
| `isAnonymous` | Boolean | X | 익명 여부 (기본값: `false`) |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "게시글 제목",
    "content": "게시글 내용",
    "category": "자유",
    "isAnonymous": false,
    "author": {
      "loginId": "student01",
      "name": "홍길동",
      "studentId": "20230001",
      "role": "USER"
    },
    "commentCount": 0,
    "createdAt": "2026-05-25T10:30:00",
    "updatedAt": "2026-05-25T10:30:00"
  },
  "message": "Post created successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

---

### 10. 게시글 목록 조회

**`GET /api/posts`** — 인증 필요

**요청 파라미터**: 없음

**성공 응답 `200`**: 게시글 배열 (게시글 작성 응답과 동일한 형식)

---

### 11. 게시글 단건 조회

**`GET /api/posts/{id}`** — 인증 필요

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 게시글 ID |

**성공 응답 `200`**: 게시글 작성 응답과 동일한 형식

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `404` | 해당 ID의 게시글 없음 |

---

### 12. 게시글 수정

**`PUT /api/posts/{id}`** — 인증 필요 (작성자 본인만)

**요청 바디**: 게시글 작성과 동일

**성공 응답 `200`**: 수정된 게시글 (게시글 작성 응답과 동일한 형식)

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | 작성자 본인이 아닌 경우 |
| `404` | 해당 ID의 게시글 없음 |

---

### 13. 게시글 삭제

**`DELETE /api/posts/{id}`** — 인증 필요 (작성자 본인만)

**성공 응답 `200`**

```json
{
  "success": true,
  "data": null,
  "message": "Post deleted successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | 작성자 본인이 아닌 경우 |
| `404` | 해당 ID의 게시글 없음 |

---

## 댓글 (Comment)

### 14. 댓글 작성

**`POST /api/posts/{postId}/comments`** — 인증 필요

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `postId` | Long | 게시글 ID |

**요청 바디**

```json
{
  "content": "댓글 내용"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `content` | String | O | 댓글 내용 |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "content": "댓글 내용",
    "author": {
      "loginId": "student01",
      "name": "홍길동",
      "studentId": "20230001",
      "role": "USER"
    },
    "postId": 1,
    "createdAt": "2026-05-25T10:30:00",
    "updatedAt": "2026-05-25T10:30:00"
  },
  "message": "Comment created successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `404` | 해당 게시글 없음 |

---

### 15. 댓글 목록 조회

**`GET /api/posts/{postId}/comments`** — 인증 필요

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `postId` | Long | 게시글 ID |

**성공 응답 `200`**: 댓글 배열 (댓글 작성 응답과 동일한 형식)

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `404` | 해당 게시글 없음 |

---

### 16. 댓글 삭제

**`DELETE /api/comments/{id}`** — 인증 필요 (작성자 본인만)

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 댓글 ID |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": null,
  "message": "Comment deleted successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | 작성자 본인이 아닌 경우 |
| `404` | 해당 댓글 없음 |

---

## 공지사항 (Notice)

> ADMIN 전용 API는 `role = ADMIN`인 유저의 토큰이 필요합니다.

### 17. 공지 작성

**`POST /api/notices`** — ADMIN 권한 필요

**요청 바디**

```json
{
  "title": "공지사항 제목",
  "content": "공지사항 내용"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | O | 제목 |
| `content` | String | O | 내용 |

**성공 응답 `200`**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "공지사항 제목",
    "content": "공지사항 내용",
    "author": {
      "loginId": "admin01",
      "name": "관리자",
      "studentId": "00000000",
      "role": "ADMIN"
    },
    "createdAt": "2026-05-25T10:30:00",
    "updatedAt": "2026-05-25T10:30:00"
  },
  "message": "Notice created successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | ADMIN 권한 없음 |

---

### 18. 공지 목록 조회

**`GET /api/notices`** — 인증 필요

**성공 응답 `200`**: 공지 배열 (공지 작성 응답과 동일한 형식)

---

### 19. 공지 단건 조회

**`GET /api/notices/{id}`** — 인증 필요

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 공지 ID |

**성공 응답 `200`**: 공지 작성 응답과 동일한 형식

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `404` | 해당 ID의 공지 없음 |

---

### 20. 공지 수정

**`PUT /api/notices/{id}`** — ADMIN 권한 필요

**요청 바디**: 공지 작성과 동일

**성공 응답 `200`**: 수정된 공지 (공지 작성 응답과 동일한 형식)

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | ADMIN 권한 없음 |
| `404` | 해당 ID의 공지 없음 |

---

### 21. 공지 삭제

**`DELETE /api/notices/{id}`** — ADMIN 권한 필요

**성공 응답 `200`**

```json
{
  "success": true,
  "data": null,
  "message": "Notice deleted successfully",
  "timestamp": "2026-05-25 10:30:00"
}
```

**실패 응답**

| 상태 코드 | 원인 |
|-----------|------|
| `403` | ADMIN 권한 없음 |
| `404` | 해당 ID의 공지 없음 |

---

## 구현 현황 요약

| 도메인 | Entity | Repository | Service | Controller | 상태 |
|--------|--------|------------|---------|------------|------|
| 인증 | O | O | O | O | ✅ 완료 |
| 사용자 | O | O | - | O | ✅ 완료 |
| 회고 | O | O | O | O | ✅ 완료 |
| 게시글 | O | O | O | O | ✅ 완료 |
| 댓글 | O | O | O | O | ✅ 완료 |
| 공지사항 | O | O | O | O | ✅ 완료 |
| 자료(Resource) | O | O | - | - | ❌ 미구현 |
