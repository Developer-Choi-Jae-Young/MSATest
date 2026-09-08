# Spring Cloud Netflix MSA - API Gateway (`gateway` 브랜치)

본 브랜치는 **Spring Cloud Netflix MSA (Microservice Architecture)** 프로젝트의 **API Gateway (`MsaGateway`)** 구현체입니다.  
클라이언트의 단일 진입점(Single Entry Point) 역할을 수행하며, Eureka Service Discovery 기반 동적 라우팅(Load Balancing), 공통 필터 처리(Logging, Custom Filter), 그리고 **JWT 인증 검증 필터(`AuthorizationHeaderFilter`)**를 제공합니다.

---

## 📌 메인 레포지토리 및 아키텍처 참조
전체 MSA 시스템 아키텍처(Saga 패턴, Outbox + CDC 구성 등) 및 각 마이크로서비스에 대한 상세 설명은 **[main 브랜치 README](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**를 참조하세요.

---

## 🛠️ 주요 기능 및 기술 스택

- **기술 스택**: Java 17, Spring Boot 3.4.0, Spring Cloud Gateway, Eureka Client, JJWT (JWT 처리)
- **포트 설정**: `8000` (API Gateway 단일 진입 포트)
- **Service Discovery 연동**: Eureka Server(`http://localhost:8761/eureka`)에 자신을 등록하며, 서비스 이름(`lb://MSAFIRSTSERVICE`, `lb://MSASECONDSERVICE`)으로 가용 인스턴스를 자동 로드 밸런싱 라우팅
- **필터 체인 구성**:
  - `GlobalFilter`: 모든 요청/응답에 적용되는 전역 로깅 및 헤더 필터
  - `CustomFilter`: 각 서비스 라우트별 커스텀 사전/사후 처리 필터
  - `LoggingFilter`: 상세 요청/응답 로그 출력 필터
  - `AuthorizationHeaderFilter`: HTTP Header의 `Authorization: Bearer <JWT>` 토큰 유효성 검증

---

## 🛣️ 라우팅 및 필터 규칙 (`application.properties`)

```properties
spring.application.name=MsaGateway
server.port=8000

# Eureka Client
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka

# 1. First Service - 로그인 (POST /first-service/login)
spring.cloud.gateway.routes[0].id=first-service
spring.cloud.gateway.routes[0].uri=lb://MSAFIRSTSERVICE
spring.cloud.gateway.routes[0].predicates[0]=Path=/first-service/login
spring.cloud.gateway.routes[0].predicates[1]=Method=POST

# 2. First Service - 회원가입 (POST /first-service/users)
spring.cloud.gateway.routes[1].id=first-service
spring.cloud.gateway.routes[1].uri=lb://MSAFIRSTSERVICE
spring.cloud.gateway.routes[1].predicates[0]=Path=/first-service/users
spring.cloud.gateway.routes[1].predicates[1]=Method=POST

# 3. First Service - 회원 및 상품 조회 (GET /first-service/**) -> AuthorizationHeaderFilter 적용 (JWT 필수)
spring.cloud.gateway.routes[2].id=first-service
spring.cloud.gateway.routes[2].uri=lb://MSAFIRSTSERVICE
spring.cloud.gateway.routes[2].predicates[0]=Path=/first-service/**
spring.cloud.gateway.routes[2].predicates[1]=Method=GET
spring.cloud.gateway.routes[2].filters[2]=AuthorizationHeaderFilter

# 4. Second Service 라우팅 (/second-service/**)
spring.cloud.gateway.routes[3].id=second-service
spring.cloud.gateway.routes[3].uri=lb://MSASECONDSERVICE
spring.cloud.gateway.routes[3].predicates[0]=Path=/second-service/**

# 전역 필터 (GlobalFilter)
spring.cloud.gateway.default-filters[0].name=GlobalFilter
```

---

## 🚀 실행 방법

### 1. 사전 조건
- `EurekaServer` 브랜치의 Discovery Server (`localhost:8761`) 실행 완료
- `firstService`, `secondService` 등의 마이크로서비스가 Eureka에 등록되어 있어야 라우팅 정상 동작

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. API 요청 테스트 (Gateway 포트 8000 사용)
- 회원가입: `POST http://localhost:8000/first-service/users`
- 로그인: `POST http://localhost:8000/first-service/login`
- 인증 필요 API (JWT 필요): `GET http://localhost:8000/first-service/users/{userId}` (Header `Authorization: Bearer <TOKEN>`)

---

## 🔗 관련 브랜치 안내
- **[main](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**: 전체 MSA 개요 및 트랜잭션/CDC 아키텍처 문서
- **[docker](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/docker)**: Kafka, Kafka Connect, MariaDB 인프라 Docker 환경
- **[EurekaServer](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/EurekaServer)**: Eureka Service Discovery Server
- **[firstService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/firstService)**: 회원 & 인증 마이크로서비스 (OpenFeign)
- **[secondService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/secondService)**: 상품 마이크로서비스 (Kafka Producer)
