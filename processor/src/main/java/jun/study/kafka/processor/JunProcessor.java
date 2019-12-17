package jun.study.kafka.processor;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JunProcessor {

    public static void main(String[] args) {
        SpringApplication.run(JunProcessor.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            StreamsBuilder builder = new StreamsBuilder();
            final KStream<String, String> stream = builder.stream(KafkaConfig.TOPIC);
            stream.peek((k,v) -> System.out.println(k + ":" + v))
                    .mapValues((ValueMapper<String, String>) String::toUpperCase)
                    .to(KafkaConfig.CHANGED_TOPIC);
            new KafkaStreams(builder.build(), KafkaConfig.createStreamsProperties()).start();
        };
    }



}
