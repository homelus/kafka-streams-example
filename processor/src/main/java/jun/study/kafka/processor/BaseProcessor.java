package jun.study.kafka.processor;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

public class BaseProcessor {

    public void log(String key, String value) {
        System.out.println("[" + Thread.currentThread().getName() + "] [String]" + key + ":" + value);
    }

    public void log(String message) {
        System.out.println("--------------------------");
        System.out.println(message);
        System.out.println("--------------------------");
    }

    public Topology build(StreamsBuilder builder) {
        final Topology topology = builder.build();
        log(topology.describe().toString());
        return topology;
    }

}
