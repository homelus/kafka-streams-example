package jun.study.kafka.producer;

import jun.study.kafka.config.KafkaConfig;
import jun.study.kafka.producer.generator.Generator;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static jun.study.kafka.config.Controller.run;

@SpringBootApplication
public class ProducerBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(ProducerBootStrap.class, args);
    }

    @Bean
    public CommandLineRunner runner(final List<Generator> generators,
                                    TopicInitializer initializer) {
        initializer.init();
        return args -> {
            for (Generator generator : generators) {
                run(generator.runningConfig(), generator::generate);
            }
        };
    }

    @Bean
    public TopicInitializer initializer() {
        return new TopicInitializer(adminClient());
    }

    @Bean(destroyMethod = "close")
    public Producer<String, String> producer() {
        return new KafkaProducer<>(KafkaConfig.createProducerProperties());
    }

    @Bean(destroyMethod = "close")
    public AdminClient adminClient() {
        return AdminClient.create(KafkaConfig.createAdminProperties());
    }

}
