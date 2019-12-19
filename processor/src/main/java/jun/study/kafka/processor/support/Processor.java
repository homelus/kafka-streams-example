package jun.study.kafka.processor.support;

import jun.study.kafka.config.RunningConfigInitializer;

public interface Processor extends RunningConfigInitializer {

    void process();

}
