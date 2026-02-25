<h1 align="center">

Curelingo - Backend

</h1>

<p align="center">
  <b>외국인을 위한 AI 응급 의료 가이드 서비스</b><br>
  <i>AI-powered Emergency Medical Guide for Foreigners in Korea</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white"/>
  <img src="https://img.shields.io/badge/Gemini_AI-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
</p>

---

## 📋 프로젝트 소개

**Curelingo**는 한국에서 의료 서비스가 필요한 외국인들을 위한 AI 기반 응급 의료 가이드 서비스입니다.

언어 장벽으로 인해 적절한 의료 서비스를 받기 어려운 외국인들이 **AI 챗봇**을 통해 증상을 상담하고, **위치 기반**으로 주변 응급실과 병원을 찾을 수 있습니다. 또한 **실시간 병상 현황**을 확인하고 **AI가 최적의 응급실을 추천**해줍니다.

<br>

## ✨ 주요 기능

### 1. AI 의료 챗봇
- Gemini AI 기반 의료 상담 챗봇
- 증상 분석 및 적절한 진료과 추천
- 응급 상황 판단 및 응급실 방문 권고
- **다국어 지원** (한국어/영어 자동 감지)

### 2. 주변 응급실 검색
- GPS 기반 주변 응급실 검색
- 실시간 응급실 가용 병상 현황 조회
- 거리순 정렬 및 상세 정보 제공

### 3. AI 응급실 추천
- 거리와 가용 병상을 종합 분석
- Gemini AI가 최적의 응급실 추천
- 추천 이유와 함께 결과 제공

### 4. 진료과별 병원 검색
- 13개 진료과별 주변 병원 검색
- 운영 시간 기반 필터링
- 병원 상세 정보 및 위치 제공

### 5. 다국어 지원
- Google Translate API 연동
- 한국어 ↔ 영어 실시간 번역
- 사용자 언어 자동 감지 및 응답

<br>

## 🏥 지원 진료과

| 진료과 | Department |
|--------|------------|
| 내과 | Internal Medicine |
| 소아청소년과 | Pediatrics |
| 피부과 | Dermatology |
| 정형외과 | Orthopedics |
| 안과 | Ophthalmology |
| 이비인후과 | ENT |
| 산부인과 | Gynecology |
| 정신건강의학과 | Psychiatry |
| 외과 | General Surgery |
| 비뇨의학과 | Urology |
| 치과 | Dentistry |
| 응급의학과 | Emergency Medicine |
| 가정의학과 | Family Medicine |

<br>

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.4.5 |
| **Database** | MongoDB, H2 |
| **AI** | Google Gemini API |
| **Translation** | Google Translate API |
| **Location** | Uber H3 (Hexagonal Indexing) |
| **API Docs** | Swagger (SpringDoc OpenAPI) |
| **DevOps** | Docker, Docker Compose, Nginx |
| **SSL** | Let's Encrypt (Certbot) |
| **Code Quality** | SonarQube |

<br>

## 📡 API Endpoints

### Gemini AI
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/gemini/chatbot` | AI 의료 챗봇 대화 |
| `GET` | `/api/gemini/recommend-emergency` | AI 응급실 추천 |

### Emergency (응급실)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/emergency/nearby` | 주변 응급실 검색 |
| `GET` | `/api/emergency/beds` | 응급실 병상 현황 조회 |

### Clinic (병원)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/clinic/department` | 진료과별 병원 검색 |

<br>

## 🚀 실행 방법

### 사전 요구사항
- Java 17+
- Docker & Docker Compose
- MongoDB

### 환경 변수 설정
```bash
# .env 파일 생성
SPRING_PROFILES_ACTIVE=dev
GEMINI_API_KEY=your_gemini_api_key
GOOGLE_TRANSLATION_API_KEY=your_google_translate_api_key
MONGO_INITDB_ROOT_USERNAME=admin
MONGO_INITDB_ROOT_PASSWORD=password
EGEN_API_KEY=your_egen_api_key
```

### 개발 환경 실행
```bash
# 저장소 클론
git clone https://github.com/Unithon-INU/2025_UNITHON_TEAM_5_BE.git
cd 2025_UNITHON_TEAM_5_BE

# Docker Compose로 실행 (개발)
docker-compose -f docker-compose.dev.yaml up -d

# 또는 Gradle로 직접 실행
./gradlew bootRun
```

### 프로덕션 환경 실행
```bash
# SSL 인증서 초기화
chmod +x init-letsencrypt.sh
./init-letsencrypt.sh

# Docker Compose로 실행 (프로덕션)
docker-compose -f docker-compose.prod.yaml up -d
```

<br>

## 📁 프로젝트 구조

```
src/main/java/com/curelingo/curelingo/
├── config/                 # 설정 (CORS, RestClient, OpenAPI)
├── gemini/                 # Gemini AI 연동
│   ├── chatbot/           # AI 챗봇 서비스
│   ├── emergencyadvisor/  # AI 응급실 추천
│   └── prompt/            # 프롬프트 빌더
├── emergencyhospital/      # 응급실 검색 서비스
├── clinic/                 # 병원 검색 서비스
├── egen/                   # 공공데이터 API 연동
├── mongodb/                # MongoDB 저장소
├── location/               # 위치 서비스 (H3)
└── translation/            # 번역 서비스
```

<br>

## 🌐 배포 환경

- **Domain**: `api.cure-lingo.com`
- **SSL**: Let's Encrypt 자동 갱신
- **Reverse Proxy**: Nginx
- **Container**: Docker

<br>

## 📖 API 문서

서버 실행 후 Swagger UI에서 API 문서를 확인할 수 있습니다:
- 개발: `http://localhost:8080/swagger-ui.html`
- 프로덕션: `https://api.cure-lingo.com/swagger-ui.html`

<br>

## 👥 팀원

| 역할 | 이름 | GitHub |
|------|------|--------|
| Backend | - | - |
| Backend | - | - |
| Backend | - | - |

<br>

## 📄 License

This project is licensed under the MIT License.
