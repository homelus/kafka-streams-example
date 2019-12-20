package jun.study.kafka.producer.generator;

import jun.study.kafka.config.RunningConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static jun.study.kafka.config.RunningConfig.PRODUCTION;

@Service
public class ProductionGenerator extends Generator{

    public ProductionGenerator(Producer<String, String> producer) {
        super(producer);
    }

    private AtomicInteger productionNo = new AtomicInteger();

    @Override
    protected String getMessage() {
        return String.valueOf(productionNo.addAndGet(2));
    }

    @Override
    public RunningConfig runningConfig() {
        return PRODUCTION;
    }
}
