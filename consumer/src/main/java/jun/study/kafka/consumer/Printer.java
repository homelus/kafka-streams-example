package jun.study.kafka.consumer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;

public class Printer {

    public void printString(Consumer<String, String> strConsumer) {
        System.out.println("print String Topic");
        while (true) {
            ConsumerRecords<String, String> records = strConsumer.poll(Duration.ofSeconds(2));
            final Iterable<ConsumerRecord<String, String>> strings =
                    records.records(KafkaConfig.STRING_CHANGED_TOPIC);
            for (ConsumerRecord<String, String> result : strings) {
                System.out.println("[String] " + result.key() + " : " + result.value());
            }
        }
    }

    public void printAnimals(Consumer<String, Long> longConsumer) {
        System.out.println("print animal aggs topic");
        boolean cutting = true;
        while (true) {
            ConsumerRecords<String, Long> records = longConsumer.poll(Duration.ofSeconds(2));
            final Iterable<ConsumerRecord<String, Long>> animals =
                    records.records(KafkaConfig.ANIMAL_AGGS_TOPIC);

            for (ConsumerRecord<String, Long> animal : animals) {
                if (cutting) {
                    System.out.println("----------------------------------------------");
                    cutting = false;
                }
                System.out.println("[Animals] " + animal.key() + " : " + animal.value());
            }

            cutting = true;
        }
    }

}
