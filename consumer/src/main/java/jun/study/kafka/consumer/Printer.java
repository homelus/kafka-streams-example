package jun.study.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Collections;

public class Printer {

    public void printString(Consumer<String, String> strConsumer, String topic) {
        strConsumer.subscribe(Collections.singletonList(topic));
        System.out.println("print String Topic");
        while (true) {
            ConsumerRecords<String, String> records = strConsumer.poll(Duration.ofSeconds(2));
            final Iterable<ConsumerRecord<String, String>> strings =
                    records.records(topic);
            for (ConsumerRecord<String, String> result : strings) {
                System.out.println("[String] " + result.key() + " : " + result.value());
            }
        }
    }

    public void printAgg(Consumer<String, Long> longConsumer, String topic) {
        longConsumer.subscribe(Collections.singletonList(topic));
        System.out.println("print " + topic + " agg topic");
        boolean cutting = true;
        while (true) {
            ConsumerRecords<String, Long> records = longConsumer.poll(Duration.ofSeconds(2));
            final Iterable<ConsumerRecord<String, Long>> animals =
                    records.records(topic);

            for (ConsumerRecord<String, Long> animal : animals) {
                if (cutting) {
                    System.out.println("----------------------------------------------");
                    cutting = false;
                }
                System.out.println("[Agg] " + animal.key() + " : " + animal.value());
            }

            cutting = true;
        }
    }

}
