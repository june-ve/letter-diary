# 💌 LetterDiary — 마음을 전하는 하루 한 장, 교환일기 서비스

> 기록하고 공유하고 이어지는 감정의 흐름.
>
>
> LetterDiary는 두 사용자가 함께 하나의 일기장을 공유하며 편지처럼 하루를 주고받는 **온라인 교환일기 서비스**입니다.
>

---

## 🌸 1. 서비스 개요 (Introduction)

LetterDiary는 “**서로의 하루를 기록하며 이어가는 감정의 교류**”를 목표로 만든 **1:1 교환 일기장 플랫폼**입니다.

사용자는 이메일을 통해 상대에게 교환일기장을 초대하고, 하루에 하나의 편지처럼 글을 주고받을 수 있습니다.

서비스의 핵심은 다음 두 가지에 집중합니다.

### ✔ 감정적 경험

- 종이 일기장을 펼친 듯한 **따뜻한 UI**
- 편지를 주고받는 듯한 인터랙션
- 메시지 작성 시 상대방에게 자동 이메일 알림 전송

### ✔ 기록의 의미

- 날짜별/순차적 메시지 조회
- 교환일기를 하나의 `.txt` 파일로 **다운로드**하여 간직
- 교환일기 목록은 가장 마지막 대화의 최신 순으로 정렬

LetterDiary는 단순 채팅 앱이 아니라,

**서로의 하루와 감정을 기록하며 이어가는 관계성 중심 플랫폼**입니다.

---

## 🏗 2. 기술 스택

### **Backend**

- Java 21
- Spring Boot 3.5.7
- Spring MVC / Spring Security 6
- JWT 인증
- Spring Data JPA
- Event Publisher + Listener 기반 비동기 이메일 알림
- MySQL 8.0
- Gradle

### **Frontend**

- Vanilla JS
- HTML / CSS
- Thymeleaf 템플릿 기반 서버사이드 렌더링
- Fetch API 기반 REST 통신
- 자체 UI/UX 설계 (일기장 디자인, 책상 UI 등)

---

## 🧩 3. 아키텍처 패키지 구조 (Architecture Overview)

LetterDiary는 **Layered Architecture** 기반으로 구성되어 있습니다.

```
com.juneve.letterdiary
 ├── config          # 공통 설정
 ├── controller      # REST API & Thymeleaf View Controller
 ├── dto             # Request/Response DTO
 │    ├── request
 │    └── response
 ├── entity          # JPA 엔티티
 ├── event           # ApplicationEvent + Listener 기반 비동기 처리
 ├── exception       # GlobalExceptionHandler, Custom Exceptions
 ├── repository      # Spring Data JPA Repository
 ├── security        # JWT 필터, SecurityConfig
 ├── service         # 도메인 비즈니스 로직
 └── validator       # 서비스 간 공유되는 유효성 검증 로직

```

### ✔ 특징

- 각 레이어는 명확한 책임을 가짐 (SRP 준수)
- 서비스 레이어에서 도메인 규칙을 처리하고,

  컨트롤러는 요청/응답만 담당하도록 분리

- 이벤트 패키지에서 메시지 작성 → 이메일 발송을 비동기로 처리
- validator 패키지에서 공통 검증 로직을 통합 관리 (중복 제거)

---

## ✨ 4. 주요 기능 (Features)

### 📌 4-1. 인증 / 회원가입 · 로그인

- 이메일 기반 회원가입
- 패스워드 BCrypt 암호화
- 로그인 시 JWT 발급
- 프론트는 token을 `localStorage`에 저장
- 모든 API는 JWT 기반 인증필요

### 📌 4-2. 교환일기 생성

- 상대 이메일을 입력하고 초대
- 두 사용자 조합(userA – userB) 당 **딱 하나의 일기장**만 생성 가능 (중복 방지)
- 일기장 제목은 자동 생성

  → `"혜준 & 혜선의 교환일기"`


### 📌 4-3. 책상 화면 (Diary Desk)

- 사용자가 참여 중인 교환일기 목록 나열
- 최신 메시지가 포함된 순서대로 정렬
- 일기장 미리보기(snippet) 제공
- "+" 버튼으로 새 일기장 생성

### 📌 4-4. 교환일기 뷰 페이지 (Diary Thread Page)

종이 일기장을 펼친 듯한 UI로 구성되어 있습니다.

#### 좌측 페이지

- 메시지 내용 1장씩 조회
- 이전 / 다음 버튼으로 페이지 이동
- 최신 메시지가 가장 먼저 출력됨

#### 우측 페이지

- 새 메시지 작성
- 메시지 전송 시 event 발행 → 상대방에게 이메일 알림

### 📌 4-5. 메시지 작성 & 이메일 알림

#### 메시지 작성 흐름

1. `DiaryMessageService.writeMessage()` 호출
2. 메시지 저장
3. **MessageCreatedEvent 비동기 발행**
4. MessageEventListener가 이메일 전송

#### Event Payload (record)

```java
public record MessageCreatedEvent(
        String threadTitle,
        String targetEmail,
        String previewContent
) {}
```

#### 이메일 내용

```
[LetterDiary] 💌 새 교환일기 알림

새로운 교환일기가 도착했습니다!

일기장: 혜준 & 혜선의 교환일기

내용 미리보기:
오늘은 우리 다같이 양재천 근처에서 맛있는 브런치를 먹...

서비스에서 확인해주세요 😊
```

비동기 구조로 메시지 저장 속도는 유지하면서 확장성도 확보.

### 📌 4-6. TXT 다운로드 기능

- 전체 대화를 하나의 텍스트 파일로 저장
- 파일명:

    ```
    혜준&혜선의교환일기_20251118.txt
    ```

- Content-Disposition + UTF-8 filename 지원
- 작성 날짜는 `2025-11-18 16:33` 형식으로 표시

- 예시:

    ```
    📖 혜준 & 혜선의 교환일기
    =====================
    
    📆 2025-11-18 16:31
    👤 혜준
    --------------------------------------
    오늘은 너무 힘들었지만, 너한테 말할 수 있어 다행이야.
    
    📆 2025-11-18 17:20
    👤 혜선
    --------------------------------------
    힘들었겠다. 오늘 일 있었던 이야기 해줄래?
    
    ```


---

## 🛠️ 5. 아키텍처 및 코드 설계 개선

### 5-1. 이벤트 기반 아키텍처를 통한 비동기 알림 시스템 설계

- 이메일 전송 지연이 메인 트랜잭션에 영향을 주지 않도록 비동기화 필요
- 단순 @Async 호출 방식보다 **Event 기반 구조**가 결합도·확장성 측면에서 더 우수
- `DiaryMessageService`는 **저장 + 이벤트 발행**만 담당
- `EventListener`가 이벤트를 비동기로 처리하여 알림 전송
- 결과: SRP 준수, 느슨한 결합, 향후 다양한 알림 채널 확장 가능

---

### 5-2. 비즈니스 로직의 응집도 향상을 위한 검증(Validator) 분리

- 초기엔 존재 확인, 권한 검증 등이 Service에 혼재되어 코드가 비대해짐
- 검증을 두 레벨로 분리하여 해결
    - **Repository default 메서드** → 단순 존재 여부 확인
    - **Validator 컴포넌트** → 권한, 중복 체크 등 복합 비즈니스 검증
- 결과: Service는 흐름 제어에 집중, 검증 로직의 재사용성 증가

---

### 5-3. Spring Data JPA 예외 래핑과 트러블슈팅

- Repository가 던진 예외가 JPA 프록시에 의해 `DataAccessException`으로 래핑되어 500 발생
- `NoSuchElementException`을 채택하여 의도한 400 응답으로 매핑
- `GlobalExceptionHandler`로 일관된 에러 처리 구조 확립

---

### 🔗 비동기 아키텍처, 코드 응집도, JPA 예외 처리 과정 등 프로젝트 고도화 여정을 자세히 확인하세요.

👉 [[Spring Boot] 프로젝트 리팩토링 로그: 비동기 아키텍처부터 JPA 예외 트러블슈팅까지](https://velog.io/@juneve/Spring-Boot-프로젝트-리팩토링-로그-비동기-아키텍처부터-JPA-예외-트러블슈팅까지)

---

## 💡 6. 확장 예정 기능 (Next Steps)

- 파일 업로드 기능 (사진/이미지 포함 교환일기)
- 일기장 삭제 기능
- 메시지 검색 기능
- 더 예쁜 이메일 템플릿 적용
- AI 기반 “오늘의 질문” 생성 기능
- PWA 적용 → 모바일 앱처럼 사용 가능
- WebSocket 기반 실시간 편지 도착 알림
- AWS 인프라 배포

---

## 🚀 7. 실행 방법 (How to Run)

본 프로젝트는 **Spring Boot 3.5.7** 기반이며, **Java 21** 환경에서 빌드됩니다.

데이터베이스 및 외부 API(Gmail) 연동을 위해 실행 전 **환경 변수 설정**이 필수적입니다.

### 7-1. 사전 준비 (Prerequisites)

- **Java 21** 이상 설치
- **MySQL** 설치 및 실행
- 데이터베이스 생성:

    ```sql
    CREATE DATABASE letterdiary CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
    ```


### 7-2. 필수 환경 변수 (Environment Variables)

보안을 위해 민감한 정보는 소스 코드가 아닌 환경 변수로 관리합니다. 실행 전 아래 변수들이 반드시 설정되어야 합니다.

| **변수명** | **설명** | **예시 값** |
| --- | --- | --- |
| `GMAIL_USERNAME` | 알림 전송용 Gmail 주소 | `user@gmail.com` |
| `GMAIL_PASSWORD` | Gmail [앱 비밀번호](https://myaccount.google.com/apppasswords) (일반 PW 아님) | `xxxx xxxx xxxx xxxx` |
| `DB_USERNAME` | MySQL 사용자명 | `root` |
| `DB_PASSWORD` | MySQL 비밀번호 | `password` |
| `JWT_SECRET` | JWT 토큰 서명용 비밀키 (임의의 긴 문자열) | `my_secret_key_...` |

---

### 7-3. 프로젝트 빌드 및 실행

편리한 실행을 위해 **IntelliJ IDEA** 사용을 권장하지만, 터미널에서도 실행 가능합니다.

### 방법 A: IntelliJ IDEA 사용 (권장)

가장 간편한 방법입니다. IDE 설정에 환경 변수를 등록하여 실행합니다.

1. 우측 상단의 실행 설정 드롭다운 클릭 → **Edit Configurations...** 선택
2. **Spring Boot** → **LetterDiaryApplication** 선택
3. **Environment variables** 항목의 문서 아이콘(📄) 클릭
4. 위 **필수 환경 변수**들을 `KEY=VALUE` 형태로 모두 입력 후 저장
5. 우측 상단의 **실행 버튼(▶️)** 클릭

### 방법 B: 터미널(Terminal) 사용

터미널에서 실행할 때는 명령어와 함께 환경 변수를 주입해야 합니다.

**1. 프로젝트 빌드**

```bash
./gradlew clean build
```

**2. 애플리케이션 실행**

- **Mac / Linux**

    ```bash
    GMAIL_USERNAME=user@gmail.com \
    GMAIL_PASSWORD=xxxx xxxx xxxx xxxx \
    DB_USERNAME=root \
    DB_PASSWORD=password \
    JWT_SECRET=your_secret_key \
    ./gradlew bootRun
    ```

- **Windows (PowerShell)**

    ```powershell
    $env:GMAIL_USERNAME="user@gmail.com"; $env:GMAIL_PASSWORD="xxxx xxxx xxxx xxxx"; $env:DB_USERNAME="root"; $env:DB_PASSWORD="password"; $env:JWT_SECRET="your_secret_key"; ./gradlew bootRun
    ```


---

실행이 완료되면 브라우저에서 `http://localhost:8080`으로 접속할 수 있습니다.