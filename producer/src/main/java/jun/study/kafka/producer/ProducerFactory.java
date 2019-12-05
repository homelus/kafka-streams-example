package jun.study.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class ProducerFactory {

    public static Producer<Long, String> getProducer() {
        Properties props = new Properties();
        return new KafkaProducer<Long, String>(props);
    }

}
