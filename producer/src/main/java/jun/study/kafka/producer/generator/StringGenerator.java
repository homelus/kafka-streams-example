package jun.study.kafka.producer.generator;

import jun.study.kafka.domain.RunningConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StringGenerator extends Generator{

    private AtomicInteger cnt = new AtomicInteger(0);

    public StringGenerator(Producer<String, String> producer) {
        super(producer);
    }

    @Override
    protected String getMessage() {
        return "kafka-test-" + (cnt.addAndGet(1));
    }

    @Override
    public RunningConfig runningType() {
        return RunningConfig.STRING;
    }

}
