package jun.study.kafka.processor.support;

import jun.study.kafka.config.RunningConfigInitializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

public interface Processor extends RunningConfigInitializer {

    void process();

    default void log(String key, String value) {
        System.out.println("[" + Thread.currentThread().getName() + "] [String]" + key + ":" + value);
    }

    default void log(String message) {
        System.out.println("--------------------------");
        System.out.println(message);
        System.out.println("--------------------------");
    }

    default void afterLog(String key, Object value) {
        System.out.println("[" + Thread.currentThread().getName() + "] [After]" + key + ":" + value);
    }

    default Topology build(StreamsBuilder builder) {
        final Topology topology = builder.build();
        log(topology.describe().toString());
        return topology;
    }

}
