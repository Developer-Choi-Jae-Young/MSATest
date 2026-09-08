# Spring Cloud Netflix MSA - Eureka Discovery Server (`EurekaServer` 브랜치)

본 브랜치는 **Spring Cloud Netflix MSA (Microservice Architecture)** 프로젝트의 **Service Discovery Server(Eureka Server)** 구축을 담고 있습니다.  
MSA 환경 내 분산된 마이크로서비스들의 네트워크 위치(IP 및 Port)를 중앙에서 동적으로 등록 및 관리하며, Spring Cloud Gateway와 각 마이크로서비스가 서로 서버 정보를 쉽게 조회(Discovery)할 수 있도록 지원합니다.

---

## 📌 메인 레포지토리 및 아키텍처 참조
전체 MSA 시스템 아키텍처(Saga 패턴, Outbox + CDC 구성 등) 및 각 마이크로서비스에 대한 상세 설명은 **[main 브랜치 README](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**를 참조하세요.

---

## 🛠️ 주요 기능 및 특징

- **Spring Cloud Netflix Eureka Server**: 클라이언트 마이크로서비스 등록 센터 역할
- **포트 설정**: `8761` (Eureka 기본 포트)
- **자체 등록 방지 설정**: Eureka Server 본인 자체는 서비스 레지스트리에 등록 및 로드하지 않도록 설정 (`register-with-eureka=false`, `fetch-registry=false`)
- **등록 서비스**:
  - `MsaGateway` (API 게이트웨이)
  - `MSAFirstService` (회원/인증 서비스)
  - `MsaSecondService` (상품/Kafka 서비스)

---

## ⚙️ 주요 환경 설정 (`application.properties`)

```properties
spring.application.name=MSADiscoveryServer

server.port=8761

# Eureka Server 설정 (자기 자신 등록/조회 제외)
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

---

## 🚀 실행 방법

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. Eureka 대시보드 확인
웹 브라우저를 열고 다음 URL에 접속합니다.
```
http://localhost:8761
```
대시보드의 **Instances currently registered with Eureka** 섹션에서 Gateway, FirstService, SecondService 등의 등록 상태를 확인할 수 있습니다.

---

## 🔗 관련 브랜치 안내
- **[main](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**: 전체 MSA 개요 및 트랜잭션/CDC 아키텍처 문서
- **[docker](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/docker)**: Kafka, Kafka Connect, MariaDB 인프라 Docker 환경
- **[gateway](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/gateway)**: Spring Cloud Gateway (라우팅 & JWT 인증)
- **[firstService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/firstService)**: 회원 & 인증 마이크로서비스 (OpenFeign)
- **[secondService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/secondService)**: 상품 마이크로서비스 (Kafka Producer)
