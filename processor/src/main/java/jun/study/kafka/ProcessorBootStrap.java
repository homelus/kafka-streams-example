package jun.study.kafka;

import jun.study.kafka.model.Production;
import jun.study.kafka.processor.support.Processor;
import jun.study.kafka.repository.ProductionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static jun.study.kafka.config.Controller.run;
import static jun.study.kafka.config.RunningConfig.PRODUCTION;

@SpringBootApplication
public class ProcessorBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorBootStrap.class, args);
    }

    @Bean
    public CommandLineRunner runner(ProductionRepository repository,
            final List<Processor> processors) {
        initProduction(repository);
        return args -> {
            for (Processor processor : processors) {
                run(processor.runningConfig(), processor::process);
            }
        };
    }

    public void initProduction(ProductionRepository repository) {
        if (PRODUCTION.isRun()) {
            for (int i = 0; i < 1000; i++) {
                Production production = new Production();
                production.setName("test-user-" + i);
                production.setDesc("test-case-" + i);
                repository.save(production);
            }
        }
    }

}
