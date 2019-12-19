package jun.study.kafka.producer.generator;

import jun.study.kafka.domain.Controller;
import jun.study.kafka.domain.KafkaConfig;
import jun.study.kafka.domain.RunningConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public abstract class Generator {

    private final Producer<String, String> producer;

    public Generator(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void generate() {
        new Thread(this::execute).start();
    }

    private void execute() {
            IntStream.range(0, 500)
                    .forEach(i -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(Controller.SPEED_MILLIES);
                            String message = getMessage();
                            final Future<RecordMetadata> response =
                                    producer.send(new ProducerRecord<>(KafkaConfig.ANIMAL_TOPIC,
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

    protected abstract String getMessage();

    public abstract RunningConfig runningType();

}
