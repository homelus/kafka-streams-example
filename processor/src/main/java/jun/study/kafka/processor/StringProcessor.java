package jun.study.kafka.processor;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;

public class StringProcessor extends BaseProcessor {

    public void process() {
        StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> stream = builder.stream(KafkaConfig.STRING_TOPIC);
        stream.peek(this::log)
                .mapValues((ValueMapper<String, String>) String::toUpperCase)
                .mapValues(s -> s.replaceAll("TEST", "REAL"))
                .to(KafkaConfig.STRING_CHANGED_TOPIC);
        final KafkaStreams kafkaStreams = new KafkaStreams(build(builder),
                KafkaConfig.createStreamsProperties("string-application"));

        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

    }

}
