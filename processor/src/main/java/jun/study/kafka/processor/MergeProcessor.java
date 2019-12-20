package jun.study.kafka.processor;

import jun.study.kafka.config.RunningConfig;
import jun.study.kafka.processor.support.MultiProcessor;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class MergeProcessor extends MultiProcessor {

    @Override
    protected void streamProcess(KStream<String, String> stream1, KStream<String, String> stream2) {
        stream1.join(stream2,
                    (ValueJoiner<String, String, Object>) (v1, v2) -> v1 + " : " + v2,
                    JoinWindows.of(Duration.ofSeconds(3)))
                .peek(this::afterLog)
                .filter((k,v) -> (Integer.valueOf(k) % 4) == 0)
                .mapValues(Object::toString)
                .to(runningConfig().desTopic());
    }

    @Override
    public RunningConfig runningConfig() {
        return RunningConfig.MERGE;
    }
}
