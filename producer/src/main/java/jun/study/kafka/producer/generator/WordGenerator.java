package jun.study.kafka.producer.generator;

import jun.study.kafka.config.RunningConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

@Component
public class WordGenerator extends Generator {

    public WordGenerator(Producer<String, String> producer) {
        super(producer);
    }

    @Override
    protected String getMessage() {
        return randomString() + " " + randomString() + " " + randomString();
    }

    @Override
    public RunningConfig runningConfig() {
        return RunningConfig.WORD;
    }

    private String randomString() {
        return RandomStringUtils.randomAlphabetic(3);
    }
}
