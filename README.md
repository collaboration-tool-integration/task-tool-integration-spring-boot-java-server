# 업무 협업 툴 통합 관리 솔루션

업무와 관련하여 사용하는 툴 (Jira, Monday, Google Calendar...) 각각에서 생성/활용 중인 데이터를 하나로 통합하여 손쉽게 관리하기 위한 솔루션

## Project Base Architecture

- Language: Java 17
- Build Tool: Gradle 8.7
- Framework: Spring Boot 3.2.4
- Database: PostgreSQL

## Dependencies

- Spring Boot (https://spring.io/projects/spring-boot)
- Spring Batch (https://spring.io/projects/spring-batch)
- Spring Data JPA (https://spring.io/projects/spring-data-jpa)
- PostgreSQL JDBC Driver (https://jdbc.postgresql.org/)
- Spring Web (https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- Spring Webflux (https://docs.spring.io/spring-framework/reference/web/webflux.html)
- Lombok (https://projectlombok.org/)
- Jackson (https://github.com/FasterXML/jackson)
- Quartz (https://www.quartz-scheduler.org/)
- GraphQL Spring Web Client (https://github.com/graphql-java-kickstart/graphql-spring-webclient)
- Atlassian ADF (Atlassian Document Format) Builder (https://bitbucket.org/atlassian/adf-builder-java/wiki/Home)

## How to Use

### Jira

1. 아래 3개의 환경변수를 등록한다.

- JIRA_SITE_URL (Jira Cloud Site URL)
- JIRA_AUTH_EMAIL (Jira Cloud API 토큰 발급 사용자 이메일)
- JIRA_API_TOKEN (Jira Cloud API Token)

2. Jira 조직 전체 정보를 DB에 저장할 수 있도록 `GET /jira/`를 1회 호출한다.

- 추후에 전체 갱신을 원한다면 동일 API 호출을 통해 갱신 가능
- 중복된 정보는 자동으로 필터링되어 저장되지 않음

3. Jira 조직 설정에서 `POST /webhook`로 Webhook 등록을 진행한다.

- 해당 Webhook을 통하여 신규 등록 및 기존 데이터에 대한 갱신이 이루어짐
- 유저, 이슈, 댓글 관련된 모든 이벤트를 수신하도록 등록
- 만약 local에서 진행을 원할 경우, `ngrok`와 같은 Tunneling 서비스 사용 필요

4. 아래 Endpoint들로 협업 관련 지표 조회

- 댓글 기반 평균 응답시간 조회
  - `GET /response-time?projectId=?&responseTimeUnit=?&targetDate=?`
  - Query
    - `projectId`: 조회 대상 DB 내부 Project ID
    - `responseTimeUnit`: 응답 시간을 계산할 범위
      - ALL (전체), MONTHLY (월별), WEEKLY (주별), DAILY (일별)
    - `targetDate`: 기준 날짜 (ex: `2024-05-27`)
- 작업 데드라인 미준수 항목 갯수 조회
  - `GET /deadline-exceeded?projectId=?&includeParentIssue=?`
  - Query
    - `projectId`: 조회 대상 DB 내부 Project ID
    - `includeParentIssue`: 최상위 이슈 포함 여부 (true/false)

### Monday

- 연동 정보 등록
  - POST /monday/register
- 사용자별 평균 1차 응답 시간 분석 API
  - GET /monday/respones-time
- 사용자별 데드라인 미준수 작업 개수 분석 API
  - GET /expired-item
- 사용자별 댓글, 업데이트 변경 횟수 분석 API
  - GET /number-of-changes
- 실시간 웹훅 처리
  - 업데이트, 댓글 생성 및 수정 실시간 반영
- 작업, 유저, 보드 1일 주기 데이터 동기화 (배치)
- 사용자별 평균 1차 응답 시간 한달, 일주일, 하루 간격 계산 및 DB 저장 (배치)

## Package Structure

- src: 소스코드 폴더
  - main: 메인 패키지
    - resources: 리소스
      - application.yml: 애플리케이션 관련 전반적인 기본 설정 정보
    - java: Java 클래스
      - com.pbl.tasktoolintegration: 솔루션 백엔드 메인 패키지
        - jira: Jira 연동 관련 패키지
          - common: Jira 관련 공통 사용 클래스를 모아둔 패키지
            - config: Jira 관련 각종 설정 클래스를 모아둔 패키지
              - converter: Jira 관련 컨버팅 클래스를 모아둔 패키지
            - ADFDeserializer: Atlassian Document Format을 Jackson에서 Deserializing 하기 위한 로직을 작성한 클래스
            - AsyncConfig: Async Thread 설정 클래스
            - JiraConfiguration: Jira 관련 각종 Configuration 및 Bean 등록을 위한 클래스
            - RequestLoggingFilterConfig: 서버로 들어오는 Request 로깅을 위한 필터 Configuration 클래스
            - WebConfig: Spring Web MVC Configuration 클래스
          - entity: Jira 관련 DB Entity 패키지
          - model: Jira 관련 모델 패키지
            - dto: Jira 관련 Data Transfer Object 패키지
            - request: Jira 관련 REST API 호출 시 사용하는 Request Body Object 패키지
          - repository: Jira 관련 Spring Data JPA Repository
          - JiraConstants: Jira 관련 상수 모음 클래스
          - JiraController: Jira 관련 API Endpoint를 설정하는 클래스
          - JiraService: Jira 관련 비즈니스 로직을 작성하는 클래스
        - monday: Monday 연동 관련 패키지
          - config: Monday 관련 각종 설정 클래스를 모아둔 패키지
            - ObjectMapperConfig: Jackson ObjectMapper Configuration 클래스
            - WebClientConfig: Monday 관련 API 호출을 위한 WebClient Configuration 클래스
          - entity: Monday 관련 DB Entity 패키지
          - exception: Monday 관련 Exception 패키지
          - model: Monday 관련 Model 패키지
            - dto: Monday 관련 Data Transfer Object 패키지
            - mondayProperty: Monday 관련 Property Model 패키지
            - request: Monday 관련 REST API 호출 시 사용하는 Request Body Object 패키지
            - response: Monday 관련 REST API 호출 시 사용하는 Response Body Object 패키지
          - repository: Monday 관련 Spring Data JPA Repository
          - util: Monday 관련 Util 패키지
          - MondayQuery: Monday REST API를 호출하기 위한 GraphQL 쿼리 클래스
          - MondayController: Monday 관련 API Endpoint를 설정하는 클래스
          - MondayService: Monday 관련 비즈니스 로직을 작성하는 클래스
          - MondayScheduler: Monday 관련 스케쥴링 등록 설정 클래스
          - TaskToolIntegrationApplication.java: 솔루션 백엔드 서버 Startpoint
  - test: 테스트 관련 패키지
- build.gradle: Gradle에서 Build를 수행하기 위한 설정 및 각종 Dependencies 정보를 작성한 파일
- settings.gradle: Gradle 관련 기본 설정
