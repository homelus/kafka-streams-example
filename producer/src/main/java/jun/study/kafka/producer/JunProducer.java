package jun.study.kafka.producer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static jun.study.kafka.domain.Controller.runAnimal;
import static jun.study.kafka.domain.Controller.runString;

@SpringBootApplication
public class JunProducer {

    public static void main(String[] args) {
        SpringApplication.run(JunProducer.class, args);
    }

    @Bean
    public CommandLineRunner runner(AnimalGenerator animalGenerator,
                                    StringGenerator stringGenerator,
                                    TopicInitializer initializer) {
        initializer.init();
        return args -> {
            runAnimal(null, arg -> animalGenerator.generate());
            runString(null, arg -> stringGenerator.generate());
        };
    }

    @Bean
    public AnimalGenerator animalGenerator() {
        return new AnimalGenerator(producer());
    }

    @Bean
    public StringGenerator stringGenerator() {
        return new StringGenerator(producer());
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
