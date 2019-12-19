package jun.study.kafka.producer.generator;

import jun.study.kafka.domain.RunningConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

@Component
public class AnimalGenerator extends Generator {

    private final static List<String> animals = asList(
            "Lion", "Tiger", "Dog", "Cat", "Cow", "Pig", "Deer", "Bear", "Pig", "raccoon");

    private final Random random = new Random();

    public AnimalGenerator(Producer<String, String> producer) {
        super(producer);
    }

    @Override
    protected String getMessage() {
        return animals.get(random.nextInt(animals.size() - 1));
    }

    @Override
    public RunningConfig runningType() {
        return RunningConfig.ANIMAL;
    }

}
