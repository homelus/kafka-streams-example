package jun.study.kafka.domain;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaConfig {

    private KafkaConfig() {}

    public final static String STRING_TOPIC = "string-topic";
    public final static String STRING_CHANGED_TOPIC = "string-change-topic";

    public final static String ANIMAL_TOPIC = "animal-topic";
    public final static String ANIMAL_AGGS_TOPIC = "animal-aggs-topic";

    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String STRING_SERIALIZER =
            "org.apache.kafka.common.serialization.StringSerializer";
    public final static String STRING_DESERIALIZER =
            "org.apache.kafka.common.serialization.StringDeserializer";
    public final static String LONG_DESERIALIZER =
            "org.apache.kafka.common.serialization.LongDeserializer";

    public final static int PARTITION_SIZE = 1;
    public final static short REPLICATION_FACTOR = 1;

    public static Properties createStreamsProperties(String id) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put("auto.offset.reset", "latest");
        return props;
    }

    public static Properties createConsumerProperties(String valueDeserializer) {
        Properties props = new Properties();

        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        props.put("key.deserializer", STRING_DESERIALIZER);
        props.put("value.deserializer", valueDeserializer);

        props.put("group.id", "jun-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");

        return props;
    }

    public static Properties createProducerProperties() {
        Properties props = new Properties();

        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        props.put("key.serializer", STRING_SERIALIZER);
        props.put("value.serializer", STRING_SERIALIZER);

        return props;
    }

    public static Map<String, Object> createAdminProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
        return props;
    }

}
