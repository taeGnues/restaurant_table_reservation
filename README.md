# Restaurant Table Reservation Service
식당 테이블을 예약하는 서비스입니다.

## 목차
- [ 사용된 기술 ](#사용된-기술)
- [ 주요 기능 ](#주요-기능)
- [ ERD ](#erd)
- [ API 명세서 ](#api-명세서-postman)
- [ Folder Structure ](#folder-structure)
- [ Structure ](#structure)

## ✨Description
- - -
### 사용된 기술
```
Language : Java JDK 17
Framework : Spring Boot 3.3.0
Build : Gradle
Database : MySQL

기타
인증/인가 구현 : Spring Security & JWT
데이터 유효성 검증 : Java Bean Validation

Unit Test & Integration Test : JUnit5
API 테스트 : POSTMAN
문서화 및 형상 관리 도구 : Notion, Git
```

### 주요 기능
| 기능                                  | 권한     | 구현현황 |
|:------------------------------------|--------|:----:|
| 고객 회원가입 및 로그인                       | 비회원    |  O   |
| 점장 회원가입 및 로그인                       | 비회원    |  O   |
| 상점 검색 및 상세 검색                       | 고객     |  O   |
| 예약 등록                               | 고객     |  O   |
| (개인) 예약 조회                          | 고객     |  O   |
| 예약 이용 후 리뷰 등록 및 수정                  | 고객     |  O   |
| 상점 등록/조회/수정/제거                      | 점장     |  O   |
| 예약 수정(예약 후 손님이 10분전에 도착해서 도착 확인 가능) | 고객     |  O   |
| 매장에 등록된 모든 예약 조회                    | 점장     |  O   |
| 리뷰 삭제                               | 고객, 점장 |  O   |

### ERD
![erd](./erd.png)

### API 명세서 (POSTMAN)
https://documenter.getpostman.com/view/27906012/2sA3QzbUhU

### Folder Structure
- `src`: 메인 로직
  `src`에는 주요 로직들이 있으며, 컨트롤러, 서비스 등을 담고 있다.
- `common`: 메인 로직은 아니지만 `src` 에서 필요한 부차적인 파일들을 모아놓은 폴더
    - `constant`: 상수와 관련된 내용
    - `entity`: 공통 Entity 관리 폴더
    - `exceptions`: 예외처리 관리 폴더
    - `config`: 설정 파일 관련 폴더
    - `secrurity`: Jwt 인증 및 인가 관련 폴더
    
  
- src 폴더 구조
> persist - model - service - web

- `model` : 해당 컨트롤러, 서비스, 레포지토리 레이어에서 사용할 DTO 및 VO 모음.
- `persist` : 해당 도메인에서 사용되는 엔티티(entity) 및 레포지토리 모음
- `service`: 서비스의 모음. 비즈니스 로직 처리, 논리적 Validation
- `web`: 컨트롤러의 모음. Request를 처리하고 Response 해주는 곳. (Service에 넘겨주고 다시 받아온 결과값을 형식화), 형식적 Validation

## ✨Structure
```text
api-server-spring-boot
  > build
  > gradle
  > src.main.java.com.zerobase.tablereservation
    > common
      > config
        | AppConfig.java
        | SecurityConfig // Web Security 관련 설정 (CORS 설정 포함)
      > constant // 상수 보관 패키지
        | Auth
        | Authority
        | BaseResult
      > entity
        | BaseEntity.java // create, update 발생 시 Entity에 공통적으로 정의되는 변수를 정의한 BaseEntity
      > exceptions
        | BaseException.java // Controller, Service에서 Response 용으로 공통적으로 사용 될 익셉션 클래스
        | ExceptionCode.java // Exception에 대한 메세지 모음
        | GlobalExceptionHandler.java // ExceptionHandler를 활용하여 정의해놓은 예외처리를 통합 관리하는 클래스
      > security
        | JwtAuthenticationFilter.java // Jwt 토큰 인증 클래스
        | JwtTokenProvider.java // Jwt 토큰 발급 및 처리 클래스
    > src
      > model // DTO 및 VO 모음
        | ReservationDTO // Reservation 관한 DTO
        ..... 
      > persist // 엔티티 및 레포지토리 모음
        > entity
          | Customer.java
          | ....
        | CustomerRepository.java
        | ....
      > service // 서비스 모음
        | AuthService.java // 인증 및 인가 담당 클래스
        | ReservationManagerService.java
        | ... 
      > web // 컨트롤러 모음
        | AuthController // 회원가입 및 로그인 컨트롤러  
        | ReservationController 
        | ....
    | RestaurantTableReservationApplication // SpringBootApplication 서버 시작 지점
  > resources
    | application.yml // Database 연동을 위한 설정 값 세팅 및 Port 정의 파일
build.gradle // gradle 빌드시에 필요한 dependency 설정하는 곳
.gitignore // git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳
README.md
```
