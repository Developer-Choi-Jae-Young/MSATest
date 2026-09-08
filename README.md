# Spring Cloud Netflix MSA - Infrastructure & Docker (`docker` 브랜치)

본 브랜치는 **Spring Cloud Netflix MSA (Microservice Architecture)** 프로젝트의 인프라 환경(Zookeeper, Kafka, Kafka Connect, MariaDB)을 Docker Container 기반으로 구성한 환경입니다.  
전체 MSA 시스템의 데이터베이스 및 이벤트 기반 데이터 스트리밍(Event-Driven Streaming) 인프라 역할을 담당합니다.

---

## 📌 메인 레포지토리 및 아키텍처 참조
전체 MSA 시스템 아키텍처(Saga 패턴, Outbox + CDC 구성 등) 및 각 마이크로서비스에 대한 상세 설명은 **[main 브랜치 README](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**를 참조하세요.

---

## 🛠️ 주요 구성 요소

| 서비스명 | 포트 (Port) | 설명 |
| :--- | :--- | :--- |
| **Zookeeper** | `2181` | Kafka 클러스터의 코디네이션 및 메타데이터 관리 |
| **Kafka Broker** | `9092`, `29092`, `9101` | 서비스 간 비동기 메시징 및 이벤트 브로커 |
| **Kafka Connect** | `8083` | DB 및 타 시스템 간 데이터 자동 동기화 (`/jars` 내 JDBC 드라이버 포함) |
| **MariaDB** | `4433` -> `3306` | MSA 마이크로서비스 통합 데이터베이스 (`mydb`) |

---

## 📂 파일 구조 및 설명

- `docker-compose.yml`: Zookeeper, Kafka, Kafka Connect, MariaDB 컨테이너 실행 정의 파일
- `jars/`: Kafka Connect에서 MariaDB, MySQL, PostgreSQL, Oracle, MSSQL 등에 연결할 때 사용하는 JDBC 드라이버 모음 (`/etc/kafka-connect/jars` 마운트)
- `source-connect.txt`: Kafka Source Connector 등록 REST API 요청 예시 (`item-source-connect`)
- `sink-connect.txt`: Kafka Sink Connector 등록 REST API 요청 예시 (`item-sink-connect`)

---

## 🚀 실행 방법

### 1. Docker 컨테이너 실행
```bash
docker-compose up -d
```

### 2. 컨테이너 상태 확인
```bash
docker-compose ps
```

### 3. Kafka Connect Connector 등록

#### Source Connector 등록 (MariaDB `item` 테이블 -> Kafka Topic `my_topic_item`)
```bash
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d '{
    "name" : "item-source-connect",
    "config" : {
        "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
        "connection.url":"jdbc:mariadb://mariadb:3306/mydb",
        "connection.user":"root",
        "connection.password":"test",
        "mode": "incrementing",
        "incrementing.column.name" : "id",
        "table.whitelist":"item",
        "topic.prefix" : "my_topic_",
        "tasks.max" : "1"
    }
  }'
```

#### Sink Connector 등록 (Kafka Topic `item` -> MariaDB `item` 테이블)
```bash
curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d '{
    "name":"item-sink-connect",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:mariadb://mariadb:3306/mydb",
        "connection.user":"root",
        "connection.password":"test",
        "auto.create":"true",
        "auto.evolve":"true",
        "delete.enabled":"false",
        "tasks.max":"1",
        "topics":"item"
    }
  }'
```

---

## 🔗 관련 브랜치 안내
- **[main](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/main)**: 전체 MSA 개요 및 트랜잭션/CDC 아키텍처 문서
- **[EurekaServer](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/EurekaServer)**: Eureka Service Discovery Server
- **[gateway](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/gateway)**: Spring Cloud Gateway (라우팅 & JWT 인증)
- **[firstService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/firstService)**: 회원 & 인증 마이크로서비스 (OpenFeign)
- **[secondService](https://github.com/Developer-Choi-Jae-Young/MSATest/tree/secondService)**: 상품 마이크로서비스 (Kafka Producer)
