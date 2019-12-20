package jun.study.kafka.processor.support;

import jun.study.kafka.config.Controller;
import jun.study.kafka.config.KafkaConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public abstract class MultiProcessor implements Processor {

    @Override
    public void process() {
        String srcTopic = runningConfig().srcTopic();
        for (int i = 0; i < Controller.PROCESSOR_NUM; i++) {
            StreamsBuilder builder = new StreamsBuilder();
            final KStream<String, String> stream1 = builder.stream(srcTopic + 1);
            final KStream<String, String> stream2 = builder.stream(srcTopic + 2);
            streamProcess(stream1.peek(this::log), stream2.peek(this::log));
            final Topology topology = build(builder);
            final KafkaStreams kafkaStreams = new KafkaStreams(topology,
                    KafkaConfig.createStreamsProperties(srcTopic + "-merge-application"));

            kafkaStreams.setUncaughtExceptionHandler(
                    (t, e) -> System.out.println("Exception: " + t.getName() + " - " + e.getMessage()));
            kafkaStreams.cleanUp();
            kafkaStreams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        }
    }

    protected abstract void streamProcess(KStream<String, String> stream1, KStream<String,
            String> stream2);

}
