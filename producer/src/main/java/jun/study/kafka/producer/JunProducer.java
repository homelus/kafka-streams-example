package jun.study.kafka.producer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootApplication
public class JunProducer {

    public static void main(String[] args) {
        SpringApplication.run(JunProducer.class, args);
    }

    @Bean
    public CommandLineRunner runner(Producer<String, String> producer, TopicInitializer initializer) throws ExecutionException, InterruptedException {
        initializer.init();
        return args -> IntStream.range(0, 500)
                .forEach(i -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        String message = "kafka-test-" + i;
                        final Future<RecordMetadata> response =
                                producer.send(new ProducerRecord<>(KafkaConfig.TOPIC,
                                        String.valueOf(i),
                                        message));
                        final RecordMetadata recordMetadata = response.get();
                        System.out.println("message: " + message + " topic(): " + recordMetadata.topic() + " " +
                                "partition():" +
                                " " + recordMetadata.partition() + " " +
                                "offset(): " + recordMetadata.offset());
                    } catch (InterruptedException | ExecutionException ignored) {}
                });
    }

    @Bean
    public TopicInitializer initializer() {
        return new TopicInitializer(adminClient());
    }

    @Bean
    public Producer<String, String> producer() {
        return new KafkaProducer<>(KafkaConfig.createProducerProperties());
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(KafkaConfig.createAdminProperties());
    }

}
