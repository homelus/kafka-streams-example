# study-kafka

카프카 예제 작성 및 테스트

## 환경 구성

#### Kafka 2.3.0 버전 설치 및 환경 세팅
기본 설치 (포트: 9092)

#### 멀티 모듈 구조
- domain: **설정**
- producer, processor, consumer: **예제**

## 카프카 스트림 

> `지속적`으로 `유입`되고 `나가는` 데이터에 대한 분석이나 질의를 수행하는 과정
> 데이터가 분석 시스템이나 프로그램에 도달하자마자 처리를 하기 때문에 스트림 프로세싱은 실시간 분석이라고 불린다.

#### 대비되는 개념: 배치(정적 데이터) 처리

- 배치에 비교한 장점
    - 이벤트에 즉각적으로 반응
    - 지속적인 데이터를 분석하는 것에 최적화

#### 상태 기반 & 무상태 스트림 처리

- 상태 기반 : 이전 스트림을 처리한 결과를 참조하는 경우
- 무상태 스트림 : 이전 처리와 관계없이 현재 스트림을 기준으로 처리하는 경우

### 카프카 스트림즈의 특징 & 개념

- 간단하고 가벼운 클라이언트 라이브러리
- 시스템이나 카프카에 대한 의존성이 없음
- 카프카 브로커나 클라이언트에 장애가 생겨도 스트림에 대해 1번만 처리 보장
- 한번에 한 레코드만 처리
- DSL 과 저수준의 API 제공

#### Stream Processing Topology
[Topology 참고](https://kafka.apache.org/24/documentation/streams/core-concepts#streams_topology)

- streams : 끊임없이 전달되는 데이터 세트
- stream processing application : 하나 이상의 토폴로지에서 처리되는 로직(연결된 그래프)
- stream processor : 하나의 노드. 입력 스트림으로 데이터를 받아 변환한 다음 다시 연결된 프로세서로 보내는 역할

- **Source Processor** : 위쪽으로 연결된 프로세서가 없는 프로세서 (토픽에서 레코드를 조회)
- **Sink Processor** : 아래쪽에 프로세서가 없는 프로세서  (특정 토픽에 저장)

### 카프카 스트림 아키텍처
[Architecture 참고](https://kafka.apache.org/24/documentation/streams/architecture)

#### 스트림과 토픽의 관계
- 각 스트림 파티션은 토픽 파티션에 저장된 정렬된 메시지
- 스트림의 데이터 레코드는 카프카 해당 토픽의 메시지 (키 + 값)
- 데이터 레코드의 키를 통해 다음 스트림으로 전달

#### 태스크
- 입력 스트림의 **파티션 개수만큼** Task 를 생성
- 각 `Task` 에 `토픽의 파티션`들이 할당(한번 정해지면 파티션의 변화가 생기지 않는 한 변하지 않음)

#### 쓰레드 모델
- 사용자가 스레드의 개수를 지정할 수 있음
- 1개의 쓰레드는 1개 이상의 Task 를 처리할 수 있음

> 더 많은 쓰레드를 띄우거나 인스턴스를 생성하여 효과적으로 병렬처리를 할 수 있음

#### 의미 요소
[Duality of Streams and Table 참고](https://docs.confluent.io/3.1.0/streams/concepts.html#duality-of-streams-and-tables)

- KStream
    - 키/값 쌍의 스트림 추상화, 각 정보는 독립적으로 이벤트와 연결된다.
        - 예를들어 X 사용자가 A1, A2 아이템을 구매했다면 두개의 데이터(Key:A1, Key:A2)가 스트림에 들어온다.
    - 메시지를 소비할 1개 이상의 토픽과 연결된다.
    - KTable 이 KStream 으로 변화될 수 있다.
   
- KTable
    - 기본키 기반의 테이블의 변화에 대한 로그 스트림 추상체이다.
    - 데이터들은 기본키를 기반으로 수정된다.
    - 키 값 기반의 테이블이라고 볼 수 있다.
    - 집계로 사용할 수 있다.
    
### 예제 

1. String 처리
2. 빈도 수 처리
3. 단어 집계 
4. 상품 처리

![Example Kafka Streams 구조](/documents/images/example-logic.png)
