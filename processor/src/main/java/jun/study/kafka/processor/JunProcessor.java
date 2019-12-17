package jun.study.kafka.processor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static jun.study.kafka.domain.Controller.runAnimal;
import static jun.study.kafka.domain.Controller.runString;

@SpringBootApplication
public class JunProcessor {

    public static void main(String[] args) {
        SpringApplication.run(JunProcessor.class, args);
    }

    @Bean
    public CommandLineRunner runner(AnimalProcessor animalProcessor, StringProcessor stringProcessor) {
        return args -> {
            runAnimal(null, arg -> animalProcessor.process());
            runString(null, arg ->stringProcessor.process());
        };
    }

    @Bean
    public AnimalProcessor animalProcessor() {
        return new AnimalProcessor();
    }

    @Bean
    public StringProcessor stringProcessor() {
        return new StringProcessor();
    }

}
