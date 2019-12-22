# Kafka Streams 예제 작성 및 정리

## 활용 예제 
    1. 문자열 처리
    2. 빈도 수 처리
    3. 단어 집계 
    4. 상품 처리

## 환경 구성

#### Kafka 2.3.0 버전 설치 및 환경 세팅
기본 설치 (localhost) (포트: 9092)

#### 멀티 모듈 구조
- domain: **설정**
- producer, processor, consumer: **예제**

## :grey_question: What is Kafka Streams?

> `지속적`으로 `유입`되고 `나가는` 데이터에 대한 분석이나 질의를 수행하는 과정
> 데이터가 분석 시스템이나 프로그램에 도달하자마자 처리를 하기 때문에 스트림 프로세싱은 실시간 분석이라고 불린다.

#### 대비되는 개념: 배치(정적 데이터) 처리

- 배치에 비교한 장점
    - 이벤트에 즉각적으로 반응
    - 지속적인 데이터를 분석하는 것에 최적화

#### 상태 기반 & 무상태 스트림 처리

- 상태 기반 : 이전 스트림을 처리한 결과를 참조하는 경우
- 무상태 스트림 : 이전 처리와 관계없이 현재 스트림을 기준으로 처리하는 경우

## :hammer: Core Concepts
[Kafka Core Concepts 원문](https://kafka.apache.org/24/documentation/streams/core-concepts#streams_topology)

`Kafka Streams` 는 카프카에 `저장된 데이터`를 **처리**하고 **분석**하기 위한 클라이언트 라이브러리이다.

### highlights

- `간편`하고 `가벼운` 클라이언트 라이브러리
- 자신을 제외한 `외부 종속에 대한 의존성이 없음`
- `신뢰성이 높은 내부 저장소`를 지원한다.
- 카프카 브로커나 클라이언트에 장애가 생겨도 각 데이터가 `1번만 처리되도록 보장`
- `한 번에 한 레코드만 처리`하여 신속성 제공
- `고수준의 DSL` 과 `저수준의 API` 제공

### Stream Processing Topology

- streams : 끊임없이 전달되는 데이터 세트
- stream processing application : 하나 이상의 토폴로지에서 처리되는 로직(연결된 그래프)
- stream processor : 하나의 노드. 입력 스트림으로 데이터를 받아 변환한 다음 다시 연결된 프로세서로 보내는 역할

- **Source Processor** : 위쪽으로 연결된 프로세서가 없는 프로세서 (토픽에서 레코드를 조회)
- **Sink Processor** : 아래쪽에 프로세서가 없는 프로세서  (특정 토픽에 저장)

### Time

#### 스트림 처리의 중요한 요소는 `시간`에 대한 개념입니다.
- 예를 들어 windowing 과 같은 작업은 시간 경계를 기준으로 정의됩니다.
- event-time 과 ingestion-time 의 선택은 Kafka 설정을 따릅니다. (Kafka 0.10.x 부터 timestamps 값은 자동적으로 메세지에 내장됩니다.)

- Event Time : 이벤트가 **발생**한 시점 
    - 예: 자동차의 GPS 센서에서 지리적 위치가 변경된다면 GPS 센서에서 위치 변경을 캡처한 시간을 의미합니다.
- Processing Time : 이벤트 또는 데이터가 **Stream Processing 애플리케이션에 의해 처리되는 시점**(소비되는 시점)
    - 예: 차량 센서에서 전달받은 위치 데이터를 읽고 처리하여 대시보드에 제공한다면 `이벤트 시간 뒤` 분석 애플리케이션의 처리 시간을 의미합니다.
- Ingestion time : **Kafka Broker** 에 의해 토픽 파티션에서 이벤트 혹은 데이터가 **저장되는 시점**(데이터가 생성될 때가 아니라 브로커가 데이터를 추가할 때)
    - 예: 데이터가 처리되지 않으면 processing 시간 개념은 없지만 ingestion 시간 개념은 존재합니다.

### Aggregations
- 하나의 데이터를 입력 스트림/테이블에서 가져와서 `여러개의 입력 데이터`를 `하나의 결과 데이터`로 조합하여 새로운 테이블을 만들어냅니다.
    - 예: counting, sum

- Kafka Stream DSL 에서 입력 스트림은 KStream 과 KTable 일 수 있지만 결과 스트림은 항상 KTable 입니다.

### Windoing
- aggregations 혹은 joins 와 같은 **상태값을 가지는 연산**에서 `같은 키를 가진 데이터`를 어떻게 제어하는 지에 대한 방법을 제공합니다.
- Windowing 처리는 Kafka Stream DSL 에서 사용할 수 있습니다.
    - 입력받은 `유예 기간` 을 이용해 데이터를 일정 기간 기다릴 수 있습니다.
    - 유예기간이 지난 데이터는 삭제되며 해당 window 에서 처리되지 않습니다.
    
### Duality of Streams and Tables
- Stream 과 Table 의 밀접한 관계를 의미하는데 stream 은 table 로 보여질 수 있고 table 은 stream 으로 보여질 수 있다.

### States
> 스트림 처리에서 상태가 필요하지 않다면, 메시지 처리는 다른 메시지에 독립적이다.
> 하지만 상태를 유지하는 것은 매력적인 스트림 처리 애플리케이션을 위한 많은 가능성을 열어둘 수 있다.<br>
> (스트림을 join 한다거나 그룹핑하거나 집계할 수 있다)

- Kafka Streams 는 데이터를 저장하고 질의하기 위한 `상태 저장소(state stores)`를 제공한다.
- 상태 저장소는 영구적인 키/값 저장소, 매모리 내의 해시 맵 또는 편리한 기타 구조일 수 있다.
- local state stores 를 통해 강한 내구성과 자동화된 복구 기능을 제공한다.

### Processing Guarantees
> 스트림 처리에서 가장 빈번한 질문중 하나는 "이 시스템이 중간에 일부 오류가 발생하더라도 각 데이터가 한번만 처리되도록 보장합니까?" 이다.

- 0.11.0.0 이전 버전에서 최소한 한번의 전달 보장만을 제공하여 그러한 점을 보장하지 못하였다.
    - 실제로 정확히 한번만 처리한다고 주장하는 경우 복제본이 생성된다고 보장할 수 없었다.
- 0.11.0.0 배포에서 이러한 문제가 해결되었다. [참고](https://kafka.apache.org/documentation/#semantics)
- 이를 설정하기 위해 `processing.guarantee` 를 **exactly_once** 로 설정하면 된다.(기본값은 **at_least_once**) 

### Out-of-Order Handling
> guarantee 를 제외한 또 다른 이슈는 "예외적인(out of order) 데이터를 어떻게 처리할 것인가" 이다.
- [내용 참고](https://kafka.apache.org/24/documentation/streams/core-concepts#streams_out_of_ordering)

## :hotel: Architecture
[Architecture 참고](https://kafka.apache.org/24/documentation/streams/architecture)
Kafka Streams 는 Kafka producer 와 consumer 라이브러리들 위에서 만들어 지고 병렬 처리, 분산 처리, 내결함성, 단순한 운영을 제공하여
운영 애플리케이션 개발을 단순화 합니다.

### Stream Partitions and Tasks
카프카의 메시징 계층은 메시지의 저장과 운반하고 처리하기 위해 데이터를 분산합니다.
카프카 토픽의 분산(partition)을 기반으로한 병렬 처리 모델의 논리적인 단위로 **partition**  과 **tasks** 의 개념을 사용합니다.

- Kafka Streams 는 입력 스트림의 partitions 에 기반하여 고정된 수의 tasks 를 만들고, 각 task 는 입력스트림으로부터 partition 목록을 할당 받습니다. (partition 에서 tasks 로의 할당은 변하지 않습니다)
- Tasks 는 할당된 partitions 를 기반으로 processor topology 를 인스턴스로 생성합니다.(결과적으로 스트림은 독립적으로 동시에 처리할 수 있습니다.)
    - 최대의 병렬처리는 partition 크기에 따라 결정됩니다. (예를들어 5개의 파티션이 있다면 최대 5개의 인스턴스로 처리 가능)

> Kafka Streams 는 `Resource manager` 가 아니고 모든 곳에서 스트림 처리를 **실행**하는 라이브러리임을 이해해야 합니다.

하나의 인스턴스에서 실패가 발생한다면 모든 할당된 tasks 는 자동으로 재시작되고 동일한 스트림 파티션을 계속 소비할 것 입니다.

### Threading Model

- 사용자에게 라이브러리에서 병렬처리를 사용 가능하도록 만드는 `threads` 갯수를 설정할 수 있게 해준다.
- 각 쓰레드는 한 개 이상의 tasks 를 독립적으로 프로세서 토폴로지를 이용해 처리할 수 있다.

> **간단한 병렬처리**<br>
> 여러개의 쓰레드를 사용하는 경우 주로 토폴로지를 복제하고 Kafka 파티션의 다른 하위 집합을 처리하여 효과적으로 병렬화 할 수 있습니다.
> (쓰레드 간의 공유가 없으므로 쓰레드 조정이 필요하지 않습니다)

### Local State Stores

- state stores 는 스트림 처리 애플리케이션에서 데이터를 **저장**하고 **질의**하는데 사용됩니다.
- `Kafka Stream DSL` 은 stateful operator(join(), aggregate(), windowing system)를 호출할 때 **자동**으로 `state stores` 를 만들고 관리합니다.
- 모든 stream task 는 **데이터를 저장하고 질의할 수 있는 API**로 접근할 수 있는 한개 이상의 `local state stores` 를 내장합니다.

### Fault Tolerance



## :heavy_check_mark: 의미 요소
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
    

