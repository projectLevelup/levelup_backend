## 🎮 Level_UP - 원하는 멘토를 선택하고 실력을 키우는 게임 코칭 플랫폼! 🚀

<img width="500px" src="assets/levelup-logo.png">
<br>

## 목차

1. [프로젝트 소개](#-프로젝트-소개)
2. [팀원 소개](#-팀원-소개)
3. [Flow Chart](#-flow-chart)
4. [와이어프레임](#-와이어프레임)
5. [ERD](#-erd)
6. [아키텍처](#-architecture)
7. [기술 스택](#-기술-스택)
8. [프로젝트 주요 기능](#-프로젝트-주요-기능)
9. [성능 개선](#-성능-개선)
10. [트러블 슈팅](#-트러블슈팅)
11. [추가 개선 가능 점](#-추가-개선-가능-점)

<br>

## 🚀 프로젝트 소개

### **🎮게임 장인들과 함께하는 실시간 피드백 & 재능 거래 플랫폼!**

게임을 더 잘하고 싶나요? 이제 게임 커뮤니티에서 각 장르의 장인들과 직접 소통하며 실시간 피드백을 받을 수 있습니다!

우리 플랫폼에서는 **고수들의 노하우를 실시간으로 전수받을 수 있는 거래 시스템**을 제공합니다. 원하는 게임의 장인을 찾아 직접 피드백을 받고, 스킬을 향상시켜 보세요.

**게임을 더 깊이 있게 즐기고, 성장하는 새로운 경험!** 지금 바로 커뮤니티에 참여해보세요.

개발 기간: 2025.02.10 ~ 2025.03.17

<br>

|                                  Level_UP Team Notion                                  |                                                                                      발표 보고서                                                                                      |
| :------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| [Notion 보러가기](https://www.notion.so/teamsparta/9-1962dc3ef51480d5b934d27f143c3c41) | [발표 보고서 보러가기](https://docs.google.com/presentation/d/1QeAYLnKef6MefFW1xK3BqIidN0l4MeFZ/edit?usp=drive_link&ouid=103470562990121621342&rtpof=true&sd=true) |

<br>

## 👤 팀원 소개


|                                                                     김효중                                                                     |                                                                      최대현                                                                       |                                                                                             이경훈                                                                                             |                                                                                   이동건                                                                                   |                                                                   정영균                                                                   |
| :---------------------------------------------------------------------------------------------------------------------------------------------: |:----------------------------------------------------------------------------------------------------------------------------------------------:| :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------------: |
| <a href="https://github.com/rlagywnd4" target="_blank"><img width="100px" src="https://avatars.githubusercontent.com/u/71661011?s=60&v=4"/></a> | <a href="https://github.com/DeaHyun0911" target="_blank"><img width="100px" src="https://avatars.githubusercontent.com/u/107090954?v=4" /></a> |                          <a href="https://github.com/kyung412820" target="_blank"><img width="100px" src="https://avatars.githubusercontent.com/u/71320521?v=4"/> </a>                          |               <a href="https://github.com/LeeDong-gun" target="_blank"> <img width="100px" src="https://avatars.githubusercontent.com/u/186677939?v=4"/></a>               | <a href="https://github.com/lq0920084" target="_blank"><img width="100px" src="https://avatars.githubusercontent.com/u/136417479?v=4"/></a> |
|                                                   [@rlagywnd4](https://github.com/rlagywnd4)                                                   |                                                 [@DeaHyun0911](https://github.com/DeaHyun0911)                                                 |                                                                                   [@kyung412820](https://듯)                                                                                   |                                                               [@LeeDong-gun](https://github.com/LeeDong-gun)                                                               |                                                 [@lq0920084](https://github.com/lq0920084)                                                 |
|                                                           프로젝트 총괄<br/> 커뮤니티                                                           |                                                                     채팅, 배포                                                                     | ElasticSearch를 이용한 인기 검색어 조회 기능<br />자동완성 <br />감성분석과 집계를 통한 Top3 선정 <br /> 클러스터를 이용한 분산 데이터 처리 <br /> 카테고리별 상품 개수 검색, ELK 기반 Log 관리 | Order, Bill, Payments 테이블 관리<br /> 결제흐름 구현 <br /> 재고관리(Redis 분산락, 비관적락) <br /> 중복결제 생성 개선(Redis Listener TTL발생) <br /> 결제승인 재시도 기능 |                                                               스프링 시큐리티                                                               |

<br>


<br>

## 🎯 **Flow Chart**

![Architecture](./assets/플로우차트.drawio.png)

상품: 특정 게임의 실력을 향상시키기 위해 제공되는 1:1 또는 그룹 단위의 교육 서비스

## 📝 **와이어프레임**

![wireframe.png](./assets/wireframe1.png)
![wireframe2.png](./assets/wireframe2.png)

## 💬 **ERD**

![final project erd](https://github.com/user-attachments/assets/31b26eec-6820-4ae2-8d3d-e630341827b3)

## 🏆 **Architecture**

#### [모놀로틱 프로젝트 아키텍처]

![Architecture](./assets/아키텍처.png)

#### [MSA 구상 아키텍처]

![Architecture](./assets/아키텍처MSA.png)

<br>

## 📚 **기술 스택**

### Frontend

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://www.tcpschool.com/html/html5_intro_intro" target="_blank"><img style="margin: 10px" width="60px" src="https://profilinator.rishav.dev/skills-assets/html5-original-wordmark.svg" alt="React"  /></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://www.w3schools.com/css/" target="_blank"><img style="margin: 10px" width="60px" src="https://profilinator.rishav.dev/skills-assets/css3-original-wordmark.svg" alt="CSS3"/></a>
    </td>

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://redis.io/" target="_blank"><img style="margin: 10px" width="60px" src="https://profilinator.rishav.dev/skills-assets/redis-original-wordmark.svg" alt="Redis"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://https://spring.io/projects/spring-security" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/spring-security.png" alt="Spring security"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://stomp-js.github.io/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/stomp.png" alt="stomp"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://stomp-js.github.io/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/websocket.png" alt="websocket"/></a>
    </td>
   <td width="80px" height="60px">
      <a href="https://gradle.org/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/gradle.png" alt="gradle"/></a>
    </td>
  </tr>
  <tr align='center'>
    <td>Redis</td>
    <td>Spring<br/>security</td>
    <td>Stomp</td>
    <td>Websocket</td>
    <td>Gradle</td>
  </tr>
</table>

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://logback.qos.ch/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/logback.png" alt="Logback"/></a>
    </td>
   <td width="80px" height="60px">
      <a href="https://www.slf4j.org/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/slf4j.png" alt="Slf4j"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://oauth.net/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/Oauth.png" alt="OAuth"/></a>
    </td>
   <td width="80px" height="60px">
      <a href="http://www.jasypt.org/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/jwt.png" alt="jwt"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://www.rabbitmq.com/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/rabbitmq.png" alt="jwt"/></a>
    </td>
  </tr>
  <tr align='center'>
    <td>Logback</td>
    <td>Slf4j</td>
    <td>OAuth 2.0</td>
    <td>Jwt</td>
    <td>RabbitMQ</td>
  </tr>
</table>

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://www.elastic.co/kr/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/elasticsearch.png" alt="elasticsearch"/></a>
    </td>
   <td width="80px" height="60px">
      <a href="https://www.elastic.co/kr/kibana" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/kibana.png" alt="kibana"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://www.elastic.co/kr/logstash" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/logstash.png" alt="logstash"/></a>
    </td>
  </tr>
  <tr align='center'>
    <td>elasticsearch</td>
    <td>kibana</td>
    <td>logstash</td>
  </tr>
</table>

<br/>

### DevOps

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://aws.amazon.com/" target="_blank"><img style="margin: 10px" width="60px" src="https://profilinator.rishav.dev/skills-assets/amazonwebservices-original-wordmark.svg" alt="AWS"/></a> 
    </td>
    <td width="80px" height="60px">
      <a href="https://www.docker.com/" target="_blank"><img style="margin: 10px" width="60px" src="https://profilinator.rishav.dev/skills-assets/docker-original-wordmark.svg" alt="Docker"/></a> 
    </td>
  </tr>
  <tr align='center'>
    <td>AWS</td>
    <td>Docker</td>
  </tr>
</table>

<br/>

### Tools

<table>
  <tr>
    <td width="80px" height="60px">
      <a href="https://www.notion.so/" target="_blank"><img style="margin: 10px" width="60px" src="https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png" alt="notion"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://github.com/" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/github.png" alt="Github"/></a>
    </td>
    <td width="80px" height="60px">
      <a href="https://slack.com/intl/ko-kr" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/slack.png" alt="Slack"/></a>  
    </td>
    <td width="80px" height="60px">
      <a href="https://github.com/features/actions" target="_blank"><img style="margin: 10px" width="60px" src="./assets/logo/github_actions.png" alt="GithubActions"/></a>  
    </td>
  </tr>
  <tr align='center'>
    <td>Notion</td>
    <td>Github</td>
    <td>Slack</td>
    <td>GithubActions</td>
  </tr>
</table>

<br/>

<div id="3"></div>

<br>

# 🎯 프로젝트 주요 기능

## 1. JWT 및 스프링 시큐리티 / OAuth 2.0 소셜 로그인

<details>
<summary></summary>

- JWT 및 Spring Security 설정을 통해 인증 및 인가 로직 구현
- OAuth 2.0을 사용하여 소셜 로그인 기능 구현

#### 주요 기능

- **일반 로그인**: email과 비밀번호를 통한 일반 로그인 기능
- **소셜 로그인**: 구글, 네이버 로그인을 통한 소셜 로그인 기능
- **자동 로그인**: 리프레시 토큰을 통한 자동 로그인 기능

#### 로그인 시스템 구성

- **Spring Security**: 스프링 시큐리티를 통한 로그인 시스템 기본 구성
- **JWT**: 액세스 토큰과 리프레시 토큰 발급을 위한 JWT
- **OAUTH2**: 소셜 로그인을 위한 OAUTH2 인증 시스템
- **MySQL**: 회원 정보 저장을 위한 RDB.

</details>

<br>

## 2. ElasticSearch를 활용한 검색 서비스

<details>
<summary></summary>

ElasticSearch는 대용량 데이터를 실시간으로 검색하고 분석할 수 있는 분산형 검색 엔진입니다.
본 서비스에서는 ElasticSearch를 활용하여 **빠르고 정확한 검색 기능**을 제공합니다.

#### 주요 기능

- **키워드 검색**: 상품명, 게임 장르, 설명(Contents) 등을 기반으로 검색 가능
- **자동 완성(Auto-Suggest)**: 입력 중인 검색어에 대한 추천어 제공
- **필터링 및 정렬**: 가격, 인기순, 최신 등록일 등의 필터 및 정렬 기능
- **리뷰 감성 분석**: 검색 결과에 포함된 리뷰의 감성 점수를 분석하여 긍정적/부정적 리뷰 제공

#### 검색 성능 최적화

- **n-gram 토크나이저 적용**: 한국어 및 영어 검색을 위한 형태소 분석기 적용
- **검색 인덱스 튜닝**: 불필요한 필드 제외 및 검색 성능 향상을 위한 캐싱 적용
- **ElasticSearch Query DSL 활용**: 다중 필드 검색 및 적용

</details>

<br>

## 3. 모니터링 서비스

<details>
<summary></summary>

서비스의 원활한 운영을 위해 **실시간 모니터링 시스템**을 구축하여 장애 예방 및 성능 개선을 지원합니다.

#### 주요 모니터링 항목

- **로그 모니터링(Log Monitoring)**: Logstash & Filebeat를 활용하여 서버 및 애플리케이션 로그 수집
- **모니터링 시각화**: Kibana를 활용한 시스템 로그, 데이터 관리 시각화
- **ElasticSearch 검색 성능 모니터링**: 검색 응답 시간 및 인덱스 크기 모니터링

#### 모니터링 시스템 구성

- **Elastic Stack(ELK)**: Elasticsearch + Logstash + Kibana를 이용한 로그 분석
- **Fleet Server**: 서버 및 애플리케이션 성능 시각화

</details>

<br>

## 4. 결제 서비스

<details>
<summary></summary>

### 주요기능

- **간편신속 결제** : 카드, 간편결제, 계좌이체 토스페이 지원
- **안전하고 신속한연동** : 토스 개발자 문서를 활용하여 빠르게API 연동 가능
- **가상 테스트** : 실 결제 없이 테스트 가능
- **알림 서비스** : 결제 관련 알림 전송
- **환불 서비스** : 결제 취소 및 환불기능 지원
- **편리한 승인** : 결제 승인 실패 시 재시도 전략 적용
- **정합성** : 안정적인 주문 처리
- **악질유저 방지** : 재고 관리에 대한 악성유저 방지

## 결제서비스 시스템 구성

- **Redis Listener** :Redis TTL발생 10분이내 결제되지않으면 HardDelete, 유저간 결제 관련 알림
- **TossPayments API** : 토스페이먼츠 외부 API를 호출 하여 승인 및 취소

</details>

## 5. 커뮤니티 서비스

<details>
<summary></summary>
좋아하는 게임에 대한 글을 올리고, 다른 게이머들의 이야기를 볼 수 있는 공간

### 주요 기능

- 게시글 / 댓글 기능
- 게시글 검색 기능

</details>

<br>

# 🔧 **성능 개선**

## 1. **Elasticsearch**: 엘라스틱 서치 사용 이유와 검색 속도 개선

- Mysql로 기존의 30만 이상의 데이터에서 특정 단어가 포함된 데이터를 조회시 속도가 조금 느리다는 판단을 함(4.932초)
- 속도의 개선을 위해서 캐시를 적용하거나 페이징을 통해 카테고리화를 수행하여 속도를 올려봄
- **속도를 개선하다보니 많은 현업의 몇 천만 데이터를 관리하기 위해서는 새로운 해답이 필요하다 생각하게 됨**
- 레디스를 찾다가 엘라스틱 서치라는 것을 알게되어 적용 시작
- **Mysql과 다른 역인덱스 구조가 검색의 속도를 비약적으로 빠르게 해준다는 것을 학습**, 적용함
- Mysql에서 일부를 처시할 경우 5초가 걸렸지만 엘라스틱 서치의 힌트와 캐시를 적용한 후엔 0.2초가 걸리게 바뀜
- 최종적으로 엘라스틱 서치를 적용하여 검색 속도를 향상

![image](https://github.com/user-attachments/assets/3b93d3f6-675f-492a-ba3c-88381c7cbb84)
![image](https://github.com/user-attachments/assets/9ec327a9-e366-4f5d-8c62-cd49329768b9)

[엘라스틱 서치 속도 개선]
![Elasticsearch 성능 비교](https://github.com/user-attachments/assets/0ee0141b-38c5-4f6b-84be-54a31de92d47)


| **검색 방법**                   | **설명**                    | **실행 속도 (ms)** |
| ------------------------------- | --------------------------- | ------------------ |
| `getPopularKeywords()`          | 기본적인 검색어 집계        | **39ms**           |
| `getPopularKeywordsOptimized()` | 실행 힌트 적용 (`Map` 방식) | **33ms**           |
| `getPopularKeywordsFastest()`   | 실행 힌트 + 쿼리 캐싱 적용  | **17ms**           |

- **최적화 결과**
  - 기본 검색 대비 **최대 2.3배 속도 향상**
  - `executionHint(TermsAggregationExecutionHint.Map)` 적용 시 **15% 속도 개선**
  - `requestCache(true)` 적용 후 **50% 추가 속도 개선**
  - 캐싱된 검색어 데이터를 활용하면 **0.1초 이내** 응답 가능

<br>
<br>

## 2. **Redis TTL** : 주문 후 10 분 결제 누락 시 악성재고관리 방지

![img.png](assets/Pendding_img.png)
위 상황은 주문을 만들었지만 결제를 진행하지않고 PENDDING 상태로 유지중.
![img.png](assets/PenddingAmount_img.png)
주문 생성이 되면 재고 감소가 이루어진상황.

### 1. **개요**

주문이 생성되었으나 결제가 진행되지 않으면 PENDING 상태로 유지됨.
이때 재고 감소는 이미 적용된 상태.
결제 없이 일정 시간이 지나면 주문을 자동 삭제하여 악성 재고를 방지.

### 2. Redis TTL 적용 방식

TTL 설정
주문이 생성되면 Redis에 TTL(10분) 설정.
TTL이 설정된 주문은 10분 내 상태 변경이 없으면 자동 삭제.
Redis Listener 활용
TTL이 만료되면 삭제 이벤트를 감지하여 로그 기록.
주문 삭제 시 재고를 원상 복구하여 악성 재고 방지.

![img.png](assets/TTL.png)

- Redis Listener 활용
- 재고 감소가 이루어질 부분 분산락 적용
- TTL을 발생 시킨 후 만료되어 삭제 될때 로깅
  ![img.png](assets/TTLCreate.png)
  TTL 발생을 로직으로 적용시켜 발생하면 레디스에 TTL데이터가 생성이됩니다.
  10분 이내로 상태 변경이 일어나면 TTL은 삭제 되고 변경이 되지않는다면
  생성 되었던 Order는 HardDelete가 이루어집니다.

<br>

## 3.커뮤니티 검색 속도 개선

### 1. 초기 상태 (MySQL + JPA)

- **테스트 환경**: `nGrinder`
  - 사용자 198명
  - 1초에 조회 1개씩 요청
  - 테스트 시간: 5분
- **테스트 결과**:
  - 총 실행 횟수: 3,600번
  - 성공: 3,598번
  - 실패: 2건
  - 평균 테스트 시간: **14,133.28ms** (매우 비효율적)
  - TPS 그래프의 편차가 심함
- **문제 분석**:
  - DB와의 통신에서 지연이 발생할 가능성이 높음
  - JPA를 사용하면서 최적화가 덜 되었을 가능성이 있음
- **진단 방법**:
  - `p6spy`를 활용하여 SQL 로그 분석
  - `COUNT` 쿼리에서 **700ms**가 소요되는 문제 발견
  - JPA의 페이지네이션 과정에서 발생하는 비효율적인 쿼리 확인
  - 아래 사진 : 타임스탬프 | sql 실행시간 | 실행된 sql작업 | 사용된 DB 커넥션 ID|DB 연결 정보
  - ![sqlLogCount.png](./assets/sqlLogCount.png)

![communityTestMysql.png](./assets/communityTestMysql.png)

### 2. QueryDSL을 활용한 최적화

- **개선 조치**:
  - JPA 페이지네이션을 QueryDSL로 변경하여 최적화 진행
- **테스트 환경 (동일한 조건으로 진행)**:
  - 테스트 시간: 5분
- **테스트 결과**:
  - 총 실행 횟수: 42,024번
  - 성공: 36,613번
  - 실패: 5,411건
  - 평균 테스트 시간: **246.18ms**
- **개선 점**:
  - 실행 횟수가 증가하고 성공 횟수도 증가
  - 하지만 평균 테스트 시간이 여전히 높음

![communityTestDsl.png](./assets/communityTestDsl.png)

### 3. 엘라스틱 서치 도입

- **개선 조치**:
  - 검색 기능을 MySQL에서 **엘라스틱 서치**로 변경
- **테스트 환경 (동일한 조건으로 진행)**:
  - 테스트 시간: 5분
- **테스트 결과**:
  - 총 실행 횟수: 57,058번
  - 성공: 40,912번
  - 실패: 16,146건
  - 평균 테스트 시간: **15.39ms**
- **분석**:
  - 에러가 크게 증가했지만, **TPS 그래프가 일정 시간마다 0으로 떨어지는 현상**이 있었음
  - 엘라스틱 서치 자체의 문제라기보다는 다른 병목이 원인일 가능성이 있음
- **개선 점**:
  - 평균 테스트 시간이 15.39ms로 크게 감소하여 **성능이 대폭 개선됨**

![elasticsearchTest.png](./assets/elasticsearchTest.png)

### 4. 추가 개선 및 필요한 자료

- **추가 분석할 점**:
  - TPS가 0으로 떨어지는 원인 분석
  - 에러 로그 분석을 통해 구체적인 장애 원인 확인

<br>

<br>

# 🔒 **트러블슈팅**

## 1. **엘라스틱 서치의 형태소 분석**

### 문제 상황

- 엘라스틱 서치에서 검색 자동 완성을 프로그래밍했지만, 일부 검색어를 인식하지 못하는 문제가 발생했다.

### 문제 원인

- 실제 nori는 일반적으로 생각하는 완벽한 형태소 분석을 수행해 주지 않는다.

```json
{
  "tokens": [
    {
      "token": "용",
      "start_offset": 0,
      "end_offset": 1,
      "type": "word",
      "position": 0
    },
    {
      "token": "왕식",
      "start_offset": 1,
      "end_offset": 3,
      "type": "word",
      "position": 1
    },
    {
      "token": "용광로",
      "start_offset": 4,
      "end_offset": 7,
      "type": "word",
      "position": 2
    }
  ]
}
```

- 이는 nori가 공백, 사전 단어를 기준으로 최선의 형태소 토큰화를 수행하기 때문이다.
- 실상 신조어는 고사하고 외래어와 대명사를 구분하지 못하는 경우도 다수 포착이 되었다.

### 해결 방법

- 해결 방법은 크게 3가지가 있었다.
- 1. 형태소 분석을 수행하는 AI를 파인 튜닝을 통해서 자체적으로 생산한다.
- 2. 돈을 내고 형태소 분석 유료 API를 사용한다.
- 3. 다른 형태소 분석기를 추가한다.
- 1,2번의 경우 비용의 문제와 시간의 문제로 현 프로젝트에서는 좋은 대안이 될 수 없어, 다른 형태의 형태소 분석기를 추가했다.
- 선택지는 크게 N-gram과 완전 일치 검색이 있었는데, 완전 일치 검색은 자동 완성이라 볼 수 없기에 제외되었다.
- 실상 N-gram, Edge-N-gram 정도가 선택지였고, 자동 완성을 위해 Edge-N-gram을, 부분 검색을 위해 의미를 기준으로 나눌 수 있는 Nori를 사용하기로 결정했다.

<br>

## 2. CustomOAuth2UserService에서 발생한 Exception이 상위로 던져지지 않는 문제

- CustomOAuth2UserService에서 발생한 로그인 실패 관련 커스텀 Exception들이 상위로 넘어가지 못해서 postman과 웹페이지로 표시가 되지 않는 문제가 발생하였다.
  ![Image](https://github.com/user-attachments/assets/27838a73-ede1-4c6c-924e-5b96bfe5319a)
- 어떤 이유로 로그인에 실패했는지 명확하게 사용자에게 알려주기 위해서는 반드시 이 커스텀 Exception이 상위로 던져져야만 한다.
- 이 문제를 해결하기 위해 **어디에서 넘어가지 못했는지 알아보다가 **OAuth2LoginAuthenticationFilter**에서는 AuthenticationException을 상속받은 Exception만 상위로 넘겨줄 수 있다는 것을 알게 되었다.**
- AuthenticationException을 상속받은 Exception의 메시지부분에 메시지를 넣어주고, Exception을 받는 핸들러부분에서 메시지를 꺼내 리턴해주면 해결될것이라 판단하였다.
- 해당 방식으로 구조를 변경한 후 정상적으로 익셉션이 상위로 리턴됨을 확인하였다.
  ![Image](https://github.com/user-attachments/assets/71b2ef8d-f6a0-4478-a917-22865edb5326)

<br>

## 3. PageableExecutionUtils를 활용한  count쿼리 최적화

### 문제 상황

프로젝트에서 페이지네이션을 적용하기 위해 `PageImpl`을 사용하고 있었다. 하지만 `PageImpl`을 사용하면 기본적으로 전체 데이터 개수를 구하기 위해 count쿼리가 실행되는데. 이로 인해 성능 저하가 발생했다.
예를 들어, 한 페이지에 10개의 데이터를 불러올 때, 총 21개의 데이터가 있다면 아래와 같은 방식으로 쿼리가 실행 되었다.

- **데이터 조회 쿼리 :** `SELECT * FROM bill WHERE ... LIMIT 10 OFFSET 0`
- **카운트 쿼리 :** `SELECT count(*) FROM bill WHERE …`

불필요한 count 쿼리가 매번 실행되면서 성능 저하가 발생했다.

![img.png](assets/Query1.png)

### 문제 원인

- `PageImpl`을 사용할 경우, 기본적으로 전체 데이터 개수를 가져오기 위해 count 쿼리를 실행한다.
- 일부 경우에는 정확한 총 개수를 알 필요 없이, 다음 페이지가 존재하는지만 확인하면된다.
- count쿼리가 실행 될 경우, 데이터가 많아질수록 성능 저하가 발생할 가능성이 높다.

### 해결 방법

`PageableExecutionUtils.getPage()` 를 활용하여 count 쿼리를 최적화 하였다.

**기존코드 (`PageImpl` 사용)**

```
JPAQuery<Long> totalCount = queryFactory
.select(billEntity.count())
.from(billEntity)
.where(
billEntity.tutor.id.eq(tutorId),
billEntity.tutor.isDeleted.eq(false)
);

return new PageImpl<>(results, pageable, totalCount.fetchOne());
```

- `totalCount.fetchOne()`를 통해 count쿼리를 직접 실행함 → 성능 저하 발생.

**수정 코드 (`PageableExecutionUtils` 적용)**

```
JPAQuery<Long> totalCount = queryFactory
    .select(billEntity.count())
    .from(billEntity)
    .where(
        billEntity.tutor.id.eq(tutorId),
        billEntity.tutor.isDeleted.eq(false)
    );

return PageableExecutionUtils.getPage(results, pageable, totalCount::fetchOne);
```

- `PageableExcutionUtils.getPage()`를 사용하여 count 쿼리 실행을 지연
- 필요할 때만 count쿼리가 실행 되므로 불필요한 성능 저하 방지

### 결과 및 효과

- **Count 쿼리 제거** : `PageableExcutionUtils.getPage()` 를 적용한 후, Count쿼리가 실행되지 않음.
  ![img.png](assets/Query2.png)
- **성능 개선** : 불필요한 쿼리 제거로 페이지네이션의 성능이 향상됨.
- **데이터 개수 최적화** : 21개의 데이터가 있을 경우 10+10+1개가 아닌, 10+ 10개만 불러오도록 개선됨.

### 결론

`PageableExcutionUtils.getPage()` 를 활용하여 count쿼리 실생을 줄이면 성능을 최적화할 수 있다. 특히, 전체개수를 정확히 알 필요가 없는 경우에는 count 쿼리를 지연 실행하거나 생략하는 것이 성능 개선에 큰 도움이 된다.

<br>

## 4. RabbitMQ 메세지 변환 오류 트러블슈팅

### 문제 상황

- 기존 Redis pub/sub 방식에서 RabbitMQ로 변경하면서 메세지 유실 문제 발생
- RabbitMQ에서 Long 타입 메세지를 전송했지만, 리스너에서 `Map<String, Object>` 타입으로 받아 변환 과정에서 오류 발생
  ![img.png](assets/parse.png)

### 기존코드

```
public void handleBillPaid(Map<String, Object> message)
```

- `Map<String, Object>` 형태로 메세지를 받아 처리
- JSON 변환 과정에서 `ClassCastException` 발생 가능

### 해결 방법

1. **DTO 사용하여 명확한 데이터 구조 정의
   기존** `Map<String, Object>` 대신, DTO 클래스를 생성하여 메시지를 받을 수 있도록 변경
2. **MessageConverter 설정 추가**
   RabbitMQ 설정 파일에서 Jackson 기반, JSON 변환을 위한 `messageConverter`등록

```
@Bean
public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

3. 리스너에서 @Payload 사용하여 DTO 맵팽
   RabbitMQ 리스너 메서드에서 `@Payload`를 활용해 JSON 데이터를 DTO로 직접 변환

```
@RabbitListener(queues = "bill.paid.queue")
public void handleBillPaid(@Payload PubBillDto dto) {
  
}
```

### 기존코드 (`PageImpl` 사용)

```
JPAQuery<Long> totalCount = queryFactory
    .select(billEntity.count())
    .from(billEntity)
    .where(
        billEntity.tutor.id.eq(tutorId),
        billEntity.tutor.isDeleted.eq(false)
    );

return new PageImpl<>(results, pageable, totalCount.fetchOne());
```

- `totalCount.fetchOne()`를 통해 count쿼리를 직접 실행함 → 성능 저하 발생.

### 개선된 코드

```
public void publishBillStatusChange(BillEntity bill) {
    String routingKey = getRoutingKey(bill.getStatus());

    PubBillDto billDto = new PubBillDto();
    billDto.setBillId(bill.getId());
    billDto.setTutorId(bill.getTutorId());
    billDto.setStudentId(bill.getStudentId());
    billDto.setStatus(bill.getStatus());

    log.info("변경된 bill 상태: {}", billDto);
    rabbitTemplate.convertAndSend(exchange, routingKey, billDto);
}
```

- `Map<String, Object>` 가 아닌 `pubBillDto` 객체를 RabbitMQ로 전송
- `Jackson2JsonMessageConverter` 를 사용하여 DTO를 JSON으로 변환

### 결론

- DTO 사용으로 데이터 구조 명확화
- JSON 변환 오류 방지
- RabbitMQ 메세지 처리 안전성 증가

메세지를 더 구조적으로 관리할 수 있고, 데이터 변환 과정에서 발생하는 오류를 줄일 수 있음.

<br>

## 5. 오류, 성공 메시지 통합 컨벤션 적용 도중 필터 오류메시지 컨벤션 적용 불가 문제 트러블 슈팅

### 문제 상황

- 필터영역에서 발생한 오류메시지는 GlobalExceptionHandler가 오류를 캐치하는 DispatcherServlet을 통과하기 전 단계이기 때문에 GlobalExceptionHandler에서 이 오류를 처리해줄 수 없음.

### 스프링 MVC 필터에서 메시지 컨벤션 해결 방법

1. 기존 오류 메시지, 성공 메시지 컨벤션과 완전하게 동일한 형태의 오류 메시지와 성공 메시지를 String값으로 선언한 필터리스폰 클래스 생성.
2. 발생한 오류 메시지와 성공 메시지를 해당 클래스를 통해 직접 HttpServletResponse의 getWriter메소드의 write로 메시지를 HttpServletResponse에 작성.
3. 해당 HttpServletResponse 객체를 리턴하는 것으로 동일한 컨벤션을 지키는 오류 메시지, 성공 메시지를 그대로 출력 가능.

### 스프링 클라우드 API 게이트웨이에서 메시지 컨벤션 해결 방법

1. 기존 오류 메시지, 성공 메시지 컨벤션과 완전하게 동일한 형태의 오류 메시지와 성공 메시지를 Map 형태로 선언한 필터리스폰 클래스 생성.
2. 발생한 오류 메시지와 성공 메시지를 해당 클래스를 통해 ByteStream으로 변환한 뒤, DataStream으로 감싸고, Mono로 한번 더 감싸서 반환.
3. 반환된 Mono를 게이트웨이 필터를 통해 리턴하는 것으로 동일한 컨벤션을 지키는 오류 메시지, 성공 메시지를 그대로 출력 가능.

### 두 스프링 필터에서 리턴방식에 차이가 발생한 이유

- 스프링 MVC는 블로킹 방식의 구조를 가지고 있어 단 한 개의 스레드에서 오류가 발생하면 다른 작업이 진행되는 일 없이 바로 리턴되지만, 스프링 클라우드 API 게이트웨이는 논블로킹 방식의 WebFlux를 사용하고 있어서 오류가 발생해도 다른 필터의 검증 작업은 그대로 진행되기 때문에, 모든 필터가 검증이 끝난 뒤에 오류를 리턴하는 pub, sub 구조를 채택하고 있기 때문에 Mono 와 DataStream을 사용해 논블로킹 방식을 유지하며, 오류를 리턴하는 방식을 사용한다.

### 결론

- 발생한 오류가 어느 위치에서 발생하는지, 사용하는 프레임워크가 어떤 처리방식을 채택했는지에 따라, 메시지 컨벤션 방법이 크게 달라질 수 있다.
- 그러므로, 어느 위치에서 리턴되는지 파악하는 것은 매우 중요하다.

<br>

## 6. Kafka 트러블슈팅: Exactly-Once에서 At-Least-Once로 변경하여 데이터 정합성 문제 해결

### 1. 문제 상황

Kafka에서 **Exactly-Once(EO) 처리**를 사용하던 중 성능 문제 또는 운영 복잡성 증가로 인해 **At-Least-Once(ALO)**로 변경해야 하는 상황이 발생했습니다. 하지만 변경 후 중복 메시지 발생 또는 데이터 유실 문제가 발생하여 데이터 정합성이 깨질 가능성이 있습니다.

### 2. 문제 원인

**Exactly-Once에서 At-Least-Once로 변경할 경우 발생하는 주요 문제점**

- **중복 메시지 발생**: ALO는 최소 한 번 메시지가 전송되므로 중복 메시지가 발생할 가능성이 높음
- **자동 Offset Commit 사용 시 데이터 유실 가능성**: 메시지가 정상적으로 처리되지 않았음에도 Offset이 커밋될 수 있음
- **Idempotence 비활성화로 인한 중복 전송 문제**

### 3. 해결 방법

### 3.1 멱등한 메시지 처리 로직 구현

ALO에서는 메시지가 중복 수신될 수 있기 때문에, **멱등성을 보장하는 방식**으로 로직을 구성해야 한다.

- 동일한 입력 값으로 로직을 여러 번 실행해도 결과가 달라지지 않도록 구현
- 예: 한 번 취소한 주문을 다시 취소하는 것은 동일한 상태를 유지하므로 멱등성을 만족함
- **그러나 모든 비즈니스 로직에서 멱등성을 보장하기는 어려우므로, 추가적인 중복 방지 처리가 필요**

#### 3.2 중복 메시지 필터링 로직 구현

중복 메시지를 방지하기 위해 **비즈니스 로직 실행과 메시지 기록을 하나의 트랜잭션으로 묶는 방식**을 사용할 수 있다.

##### **중복 메시지 필터링을 위한 redis 활용**

1) Redis의 SET을 활용하여 메시지 ID저장
2) 메시지 처리 시, 해당 메시지 ID존재 여부 확인
3) 새로운 메시지만 처리
4) TTL을 설정하여 일정 시간이 지나면 자동 삭

```sql
CREATE TABLE PROCESSED_MESSAGE (
    message_id VARCHAR(255) PRIMARY KEY,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

##### **중복 방지 로직 적용한 Consumer 예제**

```java
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("my-topic"));

Jedis redisClient = new Jedis("localhost", 6379);

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
  
    for (ConsumerRecord<String, String> record : records) {
        String messageId = record.key();
    
        if (isMessageProcessed(redisClient, messageId)) {
            continue;
        }
    
        process(record);
        markMessageAsProcessed(redisClient, messageId);
    }
  
    consumer.commitAsync((offsets, exception) -> {
        if (exception != null) {
            consumer.commitSync();
        }
    });
}
redisClient.close();
```

##### **중복 메시지 확인 및 기록 함수**

```java
private boolean isMessageProcessed(Jedis redisClient, String messageId) {
    return redisClient.sismember("processed_messages", messageId);
}

private void markMessageAsProcessed(Jedis redisClient, String messageId) {
    redisClient.sadd("processed_messages", messageId);
    redisClient.expire("processed_messages", 86400);
}
```

<br>

# 📈 **추가 개선 가능 점**

## 1.  **엘라스틱 서치 개발의 개선**

- 샤드 수가 너무 많으면 오버헤드가 증가하고, 너무 적으면 데이터 검색 속도가 저하된다. 이를 유념하여 조정이 필요하다.
- 데이터의 사용 빈도에 따라 핫(Hot), 웜(Warm), 콜드(Cold) 노드를 구성하여 리소스를 효율적으로 사용해야한다.
- 여러 클러스터로 나누어 데이터를 검색하거나 복자하여 대규모 환경에서도 안정적인 성능을 유지할 수 있도록 개발해야한다.
- 백업을 위한 스냅샷을 정기적으로 생성하도록 설정해야한다.

<br>
