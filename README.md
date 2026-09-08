# Spring Cloud Netflix MSA - Second Service (Item & Kafka Service) (`secondService` 브랜치)

본 브랜치는 **Spring Cloud Netflix MSA (Microservice Architecture)** 프로젝트의 **Second Service (상품/데이터 관리 & Kafka Producer 서비스)** 구현체입니다.  
사용자별 상품 데이터 저장 및 관리 기능을 제공하며, **Spring Kafka**를 활용하여 데이터 등록 시 Kafka 메시지 브로커(`item` Topic)로 이벤트를 발행하는 비동기 메시징 역할을 수행합니다.

---

## 📌 메인 레포지토리 및 아키텍처 참조
전체 MSA 시스템 아키텍처(Saga 패턴, Outbox + CDC 구성 등) 및 각 마이크로서비스에 대한 상세 설명은 **[main 브랜치 README](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**를 참조하세요.

---

## 🛠️ 주요 기능 및 기술 스택

- **기술 스택**: Java 17, Spring Boot 3.4.0, Spring Kafka, Spring Data JPA, Eureka Client, MariaDB
- **Service Discovery**: Eureka Client로 Eureka Server(`http://localhost:8761/eureka`)에 등록 (`MsaSecondService`)
- **포트 설정**: `server.port=0` (동적 포트 할당)
- **Kafka Producer 연동**:
  - `KafkaProducerConfig`: Kafka Producer 설정
  - `ItemProducer`: Kafka Connect 호환 Schema/Payload 구조(`KafkaItemDto`) 생성 및 `item` Topic으로 이벤트 메세지 전송
- **First Service 연동**: First Service의 OpenFeign Client(`SecondServiceClient`) 요청 수신 엔드포인트 제공 (`GET /data/{userId}`)

---

## ⚙️ 주요 환경 설정 (`application.properties`)

```properties
spring.application.name=MsaSecondService
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
```

---

## 📡 API 엔드포인트 (Gateway `/second-service` 라우팅 기준)

| HTTP Method | API Endpoint | 설명 | 비고 |
| :--- | :--- | :--- | :--- |
| `POST` | `/second-service/data/{userId}` | 특정 사용자의 상품 생성 | Kafka Producer로 `item` 토픽 이벤트 메시지 발행 |
| `GET` | `/second-service/data/{userId}` | 특정 사용자의 상품 목록 조회 | First Service FeignClient 연동 엔드포인트 |
| `GET` | `/second-service/welcome` | 웰컴 메시지 확인 | 테스트용 |
| `GET` | `/second-service/check` | 실행 중인 인스턴스 Port 확인 | Load Balancing 테스트 |

---

## 🚀 실행 방법

### 1. 사전 조건
- `docker` 브랜치의 Zookeeper, Kafka, MariaDB (`localhost:4433`) 실행 완료
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
- **[firstService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/firstService)**: 회원 & 인증 마이크로서비스 (OpenFeign)
