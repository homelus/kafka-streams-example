package jun.study.kafka.consumer;

import jun.study.kafka.config.Controller;
import jun.study.kafka.config.KafkaConfig;
import jun.study.kafka.config.RunningConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static jun.study.kafka.config.RunningConfig.*;

@SpringBootApplication
public class ConsumerBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerBootStrap.class, args);
    }

    @Bean
    public CommandLineRunner runner(@Qualifier("stringConsumer") Consumer<String, String> strConsumer,
                                    @Qualifier("animalConsumer") Consumer<String, Long> animalConsumer,
                                    @Qualifier("wordConsumer") Consumer<String, Long> wordConsumer,
                                    @Qualifier("productionConsumer") Consumer<String, Long> productionConsumer,
                                    Printer printer) {
        return args -> {
            Controller.run(STRING, strConsumer, RunningConfig.STRING.desTopic(), printer::printString);
            Controller.run(ANIMAL, animalConsumer, ANIMAL.desTopic(), printer::printAgg);
            Controller.run(WORD, wordConsumer, WORD.desTopic(), printer::printAgg);
            Controller.run(PRODUCTION, productionConsumer, PRODUCTION.desTopic(), printer::printString);
        };
    }

    @Bean
    public Printer printer() {
        return new Printer();
    }

    @Bean(destroyMethod = "close")
    @Qualifier("stringConsumer")
    public Consumer<String, String> stringConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.STRING_DESERIALIZER));
    }

    @Bean(destroyMethod = "close")
    @Qualifier("animalConsumer")
    public Consumer<String, Long> animalConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.LONG_DESERIALIZER));
    }

    @Bean(destroyMethod = "close")
    @Qualifier("wordConsumer")
    public Consumer<String, Long> wordConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.LONG_DESERIALIZER));
    }

    @Bean(destroyMethod = "close")
    @Qualifier("productionConsumer")
    public Consumer<String, String> productionConsumer() {
        return new KafkaConsumer<>(KafkaConfig.createConsumerProperties(KafkaConfig.STRING_DESERIALIZER));
    }

}
