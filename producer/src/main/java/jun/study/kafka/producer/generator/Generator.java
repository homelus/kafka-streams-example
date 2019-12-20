package jun.study.kafka.producer.generator;

import jun.study.kafka.config.Controller;
import jun.study.kafka.config.RunningConfigInitializer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public abstract class Generator implements RunningConfigInitializer {

    protected final Producer<String, String> producer;

    public Generator(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void generate() {
        new Thread(this::execute).start();
    }

    private void execute() {
        String topic = topic() == null ? runningConfig().srcTopic() : topic();
        IntStream.range(0, 500)
                .forEach(i -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(Controller.SPEED_MILLIE_SECOND);
                        String message = getMessage();
                        String key = key();
                        final Future<RecordMetadata> response =
                                producer.send(new ProducerRecord<>(topic, key == null ?
                                        String.valueOf(i) : key, message));
                        final RecordMetadata recordMetadata = response.get();
                        System.out.println("key : " + key + ", message: " + message + "," +
                                " " +
                                "topic():" +
                                recordMetadata.topic() + ", partition() :" + recordMetadata.partition() + " " +
                                ", offset() : " + recordMetadata.offset());
                    } catch (InterruptedException | ExecutionException ignored) {}
                });
    }

    protected abstract String getMessage();

    protected String key() {
        return null;
    };

    protected String topic() {
        return null;
    }

}
