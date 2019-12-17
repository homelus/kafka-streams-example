package jun.study.kafka.producer;

import jun.study.kafka.domain.Controller;
import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StringGenerator extends Generator{

    private final Producer<String, String> producer;

    public StringGenerator(Producer<String, String> producer) {
        this.producer = producer;
    }

    @Override
    protected void execute() {
        IntStream.range(0, 500)
                .forEach(i -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(Controller.SPEED_MILLIES);
                        String message = "kafka-test-" + i;
                        final Future<RecordMetadata> response =
                                producer.send(new ProducerRecord<>(KafkaConfig.STRING_TOPIC,
                                        String.valueOf(i),
                                        message));
                        final RecordMetadata recordMetadata = response.get();
                        System.out.println("message: " + message + " topic(): " + recordMetadata.topic() + " " +
                                "partition():" +
                                " " + recordMetadata.partition() + " " +
                                "offset(): " + recordMetadata.offset());
                    } catch (InterruptedException | ExecutionException ignored) {}
                });
    }

}
