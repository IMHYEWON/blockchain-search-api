# Blockchain Search API System

## 프로젝트 개요
다중 블록체인 네트워크의 트랜잭션, 주소, 토큰 정보를 통합 검색할 수 있는 API 시스템

## 기술 스택
- **Backend**: Java 11, Spring Boot 2.6.11
- **Search Engine**: Elasticsearch 7.x
- **Database**: MySQL 8.0 (메타데이터)
- **Cache**: Redis
- **Build Tool**: Gradle 7.6.1

## 주요 기능
- 🔍 **통합 검색**: 트랜잭션, 주소, 토큰을 한 번에 검색
- ⚡ **자동완성**: 실시간 자동완성 API
- 🌐 **다중 네트워크**: Ethereum, BSC, Polygon 등 지원
- 📊 **이중 검색 방식**: URL API + Query DSL 방식 모두 지원

## 빠른 시작

### 1. 환경 준비
```bash
# Gradle Wrapper 실행 권한 설정
chmod +x gradlew

# 의존성 설치 및 빌드
./gradlew clean build

# Docker 컨테이너 실행
docker-compose up -d
```

### 2. API 테스트
```bash
# 통합 검색
curl "http://localhost:8080/api/v1/search?q=0x123&network=ethereum"

# 자동완성
curl "http://localhost:8080/api/v1/autocomplete?q=ETH&network=ethereum"
```

## API 엔드포인트

### 검색 API
- `GET /api/v1/search` - 통합 검색
- `GET /api/v1/autocomplete` - 자동완성
- `GET /api/v1/transactions` - 트랜잭션 검색
- `GET /api/v1/addresses/{address}` - 주소 검색
- `GET /api/v1/tokens` - 토큰 검색

### Elasticsearch 관리
- `POST /api/v1/es/url-search` - URL 방식 검색
- `POST /api/v1/es/query-search` - Query DSL 방식 검색
- `GET /api/v1/es/indices` - 인덱스 상태 확인

## 검색 방식

### 1. URL 방식 (REST API)
```
GET /api/v1/es/url-search?q={query}&index={index}&size={size}
```

### 2. Query DSL 방식
```json
POST /api/v1/es/query-search
{
  "index": "transactions",
  "query": {
    "bool": {
      "should": [
        {"match": {"txHash": "0x123"}},
        {"match": {"symbol": "ETH"}}
      ]
    }
  }
}
```

## 프로젝트 구조
```
src/main/java/com/blockchain/search/
├── controller/          # REST API 컨트롤러
├── service/            # 비즈니스 로직
├── repository/         # 데이터 접근 계층
├── model/              # 데이터 모델
├── config/             # 설정 클래스
└── util/               # 유틸리티 클래스
```

## Gradle 명령어

### 기본 명령어
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test

# 의존성 확인
./gradlew dependencies

# 프로젝트 정리
./gradlew clean
```

### Spring Boot 명령어
```bash
# JAR 파일 생성
./gradlew bootJar

# WAR 파일 생성
./gradlew bootWar

# 의존성 트리 확인
./gradlew dependencies --configuration compileClasspath
```

## 개발 환경
- **Port 8080**: Search API
- **Port 9200**: Elasticsearch
- **Port 5601**: Kibana
- **Port 3306**: MySQL
- **Port 6379**: Redis
- **Port 8081**: Redis Commander

## 빌드 결과물
- **JAR 파일**: `build/libs/search-api.jar`
- **소스 JAR**: `build/libs/search-api-sources.jar`
- **Javadoc JAR**: `build/libs/search-api-javadoc.jar`
