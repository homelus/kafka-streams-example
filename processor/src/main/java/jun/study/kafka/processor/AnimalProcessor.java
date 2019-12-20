package jun.study.kafka.processor;

import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.processor.support.SingleProcessor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Service;

import static jun.study.kafka.config.RunningConfig.ANIMAL;

@Service
public class AnimalProcessor extends SingleProcessor {

    @Override
    public void streamProcess(KStream<String, String> streams) {
                streams.selectKey((key, value) -> value)
                        .filter(this::isNotPig)
                        .mapValues((ValueMapper<String, String>) String::toLowerCase)
                            .groupBy((key, word) -> word)
                            .count(Materialized.as("count-store"))
                        .toStream()
                        .to(ANIMAL.desTopic(),
                        Produced.with(Serdes.String(), Serdes.Long()));
    }

    @Override
    public RunningConfig runningConfig() {
        return ANIMAL;
    }

    private boolean isNotPig(String key, String animal) {
        return !key.equalsIgnoreCase("pig");
    }

}
