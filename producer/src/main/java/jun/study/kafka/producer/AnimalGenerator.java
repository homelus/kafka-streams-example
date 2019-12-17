package jun.study.kafka.producer;

import jun.study.kafka.domain.Controller;
import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class AnimalGenerator extends Generator{

    private final Producer<String, String> producer;

    private final static List<String> animals = asList(
            "Lion", "Tiger", "Dog", "Cat", "Cow", "Pig", "Deer", "Bear", "Pig", "raccoon");

    public AnimalGenerator(Producer<String, String> producer) {
        this.producer = producer;
    }

    @Override
    protected void execute() {
        int size = animals.size();
        final Random random = new Random();
        IntStream.range(0, 500)
                .forEach(i -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(Controller.SPEED_MILLIES);
                        String message = animals.get(random.nextInt(size - 1));
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

}
