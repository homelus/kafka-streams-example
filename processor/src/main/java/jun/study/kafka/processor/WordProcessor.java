package jun.study.kafka.processor;

import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.processor.support.SingleProcessor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static jun.study.kafka.config.RunningConfig.ANIMAL;
import static jun.study.kafka.config.RunningConfig.WORD;

@Service
public class WordProcessor extends SingleProcessor {

    @Override
    public RunningConfig runningConfig() {
        return WORD;
    }

    @Override
    protected void streamProcess(KStream<String, String> stream) {
        stream.mapValues((ValueMapper<String, String>) String::toUpperCase)
                .flatMapValues(values -> Arrays.asList(values.split("\\W+")))
                .groupBy((key, value) -> value)
                .count(Materialized.as("word-counts-store"))
                .toStream().to(ANIMAL.desTopic(),
                    Produced.with(Serdes.String(), Serdes.Long()));
    }

}