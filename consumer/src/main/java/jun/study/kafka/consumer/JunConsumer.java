package jun.study.kafka.consumer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class JunConsumer {

    public static void main(String[] args) {
        SpringApplication.run(JunConsumer.class, args);
    }

    @Bean
    public CommandLineRunner runner(Consumer<String, String> consumer) {
        consumer.subscribe(Arrays.asList(KafkaConfig.CHANGED_TOPIC));
        return args -> {
            while (true) {
                final ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofSeconds(2));
                final Iterable<ConsumerRecord<String, String>> results = records.records(KafkaConfig.CHANGED_TOPIC);
                for (ConsumerRecord<String, String> result : results) {
                    System.out.println(result.key() + " : " + result.value());
                }
            }
        };
    }

    @Bean
    public Consumer<String, String> createConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties());
    }

}
