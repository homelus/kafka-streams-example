package jun.study.kafka.processor.support;

import jun.study.kafka.config.Controller;
import jun.study.kafka.config.KafkaConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public abstract class SingleProcessor implements Processor {

    @Override
    public void process() {
        String srcTopic = runningConfig().srcTopic();
        for (int i = 0; i < Controller.PROCESSOR_NUM; i++) {
            StreamsBuilder builder = new StreamsBuilder();
            final KStream<String, String> stream = builder.stream(srcTopic);
            streamProcess(stream.peek(this::log));
            final Topology topology = build(builder);
            final KafkaStreams kafkaStreams = new KafkaStreams(topology,
                    KafkaConfig.createStreamsProperties(srcTopic + "-application"));

            kafkaStreams.setUncaughtExceptionHandler(
                    (t, e) -> System.out.println("Exception: " + t.getName() + " - " + e.getMessage()));
            kafkaStreams.cleanUp();
            kafkaStreams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        }
    }

    protected abstract void streamProcess(KStream<String, String> stream);

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
