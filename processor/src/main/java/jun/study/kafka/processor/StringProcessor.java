package jun.study.kafka.processor;

import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.processor.support.SingleProcessor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Service;

@Service
public class StringProcessor extends SingleProcessor {

    @Override
    protected void streamProcess(KStream<String, String> stream) {
        stream.mapValues((ValueMapper<String, String>) String::toUpperCase)
                .map((key, value) -> new KeyValue<>(key, value + "-" + key))
                .filterNot((k,v) -> (Long.valueOf(k) % 2) == 0)
                .mapValues((ValueMapper<String, Object>) s -> s.replaceAll("TEST", "REAL"))
                .to(RunningConfig.STRING.desTopic());
    }

    @Override
    public RunningConfig runningConfig() {
        return RunningConfig.STRING;
    }

}
