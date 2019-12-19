package jun.study.kafka.processor;

import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.processor.support.BaseProcessor;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Service;

@Service
public class StringProcessor extends BaseProcessor {

    @Override
    protected void streamProcess(KStream<String, String> stream) {
        stream.mapValues((ValueMapper<String, String>) String::toUpperCase)
                .mapValues(s -> s.replaceAll("TEST", "REAL"))
                .to(RunningConfig.STRING.desTopic());
    }

    @Override
    public RunningConfig runningConfig() {
        return RunningConfig.STRING;
    }

}
