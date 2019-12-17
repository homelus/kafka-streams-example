package jun.study.kafka.consumer;

import jun.study.kafka.domain.Controller;
import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class JunConsumer {

    public static void main(String[] args) {
        SpringApplication.run(JunConsumer.class, args);
    }

    @Bean
    public CommandLineRunner runner(@Qualifier("stringConsumer") Consumer<String, String> strConsumer,
                                    @Qualifier("longConsumer") Consumer<String, Long> longConsumer,
                                    Printer printer) {

        strConsumer.subscribe(Collections.singletonList(KafkaConfig.STRING_CHANGED_TOPIC));
        longConsumer.subscribe(Collections.singletonList(KafkaConfig.ANIMAL_AGGS_TOPIC));
        return args -> {
            Controller.runString(strConsumer, printer::printString);
            Controller.runAnimal(longConsumer, printer::printAnimals);
        };
    }

    @Bean
    public Printer printer() {
        return new Printer();
    }

    @Bean
    @Qualifier("stringConsumer")
    public Consumer<String, String> createStringConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.STRING_DESERIALIZER));
    }

    @Bean
    @Qualifier("longConsumer")
    public Consumer<String, Long> createLongConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.LONG_DESERIALIZER));
    }



}
