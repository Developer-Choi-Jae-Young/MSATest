# Spring Cloud Netflix MSA - First Service (User & Auth Service) (`firstService` 브랜치)

본 브랜치는 **Spring Cloud Netflix MSA (Microservice Architecture)** 프로젝트의 **First Service (회원 관리 & 인증 서비스)** 구현체입니다.  
사용자 회원가입, 로그인(Spring Security + JWT), 그리고 **Spring Cloud OpenFeign**을 이용한 다른 마이크로서비스(`MsaSecondService`)와의 동기식 통신을 담당합니다.

---

## 📌 메인 레포지토리 및 아키텍처 참조
전체 MSA 시스템 아키텍처(Saga 패턴, Outbox + CDC 구성 등) 및 각 마이크로서비스에 대한 상세 설명은 **[main 브랜치 README](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**를 참조하세요.

---

## 🛠️ 주요 기능 및 기술 스택

- **기술 스택**: Java 17, Spring Boot 3.4.0, Spring Security, Spring Cloud OpenFeign, Spring Data JPA, Eureka Client, MariaDB / H2, JWT
- **Service Discovery**: Eureka Client로 Eureka Server(`http://localhost:8761/eureka`)에 등록 (`MSAFirstService`)
- **포트 설정**: `server.port=0` (동적 포트 할당을 통한 Scale-Out 지원)
- **보안 및 인증**:
  - `AuthenticationFilter`: 로그인 처리 및 JWT 토큰 생성/발급
  - `SecurityConfig`: 엔드포인트 접근 권한 및 필터 체인 구성
- **서비스 간 통신 (OpenFeign)**:
  - `SecondServiceClient`: `MSASECONDSERVICE`를 호출하여 사용자별 상품 목록을 조회
  - `FeignErrorDecoder`: Feign Client 요청 시 에러 처리 디코더

---

## ⚙️ 주요 환경 설정 (`application.properties`)

```properties
spring.application.name=MSAFirstService
server.port=0

# Eureka Client 설정
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.instance-id=${spring.application.name}:${spring.application.instance_id:${random.value}}}

# MariaDB 연동 (docker 브랜치 인프라)
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:4433/mydb
spring.datasource.username=root
spring.datasource.password=test

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

# JWT 설정
token.expiration_time=86400000
token.secret=46930c6e2098076c2ffdc8f1de5d7095debe589eea5b0f43819599a3749d88e26fff284659a26f4baecffc1e7ba574d24d4da5164bf2cf21670ce983d58eb91c
```

---

## 📡 API 엔드포인트 (Gateway `/first-service` 라우팅 기준)

| HTTP Method | API Endpoint | 설명 | 비고 |
| :--- | :--- | :--- | :--- |
| `POST` | `/first-service/users` | 회원가입 (`RequestUser`) | DB 유저 저장 |
| `POST` | `/first-service/login` | 로그인 및 JWT 토큰 발급 (`RequestLogin`) | Response Header에 JWT 반환 |
| `GET` | `/first-service/users/{userId}` | 회원 정보 및 상품 목록 통합 조회 | Feign Client로 `SecondService` 호출 |
| `GET` | `/first-service/welcome` | 웰컴 메시지 확인 | 테스트용 |
| `GET` | `/first-service/check` | 실행 중인 인스턴스 Port 확인 | Load Balancing 테스트 |

---

## 🚀 실행 방법

### 1. 사전 조건
- `docker` 브랜치의 MariaDB (`localhost:4433`) 실행 완료
- `EurekaServer` 브랜치의 Discovery Server (`localhost:8761`) 실행 완료

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

---

## 🔗 관련 브랜치 안내
- **[main](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**: 전체 MSA 개요 및 트랜잭션/CDC 아키텍처 문서
- **[docker](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/docker)**: Kafka, Kafka Connect, MariaDB 인프라 Docker 환경
- **[EurekaServer](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/EurekaServer)**: Eureka Service Discovery Server
- **[gateway](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/gateway)**: Spring Cloud Gateway (라우팅 & JWT 인증)
- **[secondService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/secondService)**: 상품 마이크로서비스 (Kafka Producer)
