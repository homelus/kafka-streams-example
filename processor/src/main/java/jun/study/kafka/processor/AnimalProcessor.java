package jun.study.kafka.processor;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;

public class AnimalProcessor extends BaseProcessor {

    public void process() {

        StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> streams = builder.stream(KafkaConfig.ANIMAL_TOPIC);
                streams.peek(this::log)
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                        .filter(this::isNotPig)
                        .groupBy((key, word) -> word)
                        .count(Materialized.as("count-store"))
                .toStream().to(KafkaConfig.ANIMAL_AGGS_TOPIC,
                        Produced.with(Serdes.String(), Serdes.Long()));

        new KafkaStreams(build(builder),
                KafkaConfig.createStreamsProperties("animal-application")).start();
    }

    private boolean isNotPig(String key, String animal) {
        return !animal.equalsIgnoreCase("pig");
    }

}
