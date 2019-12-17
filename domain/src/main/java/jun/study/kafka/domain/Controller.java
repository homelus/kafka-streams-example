package jun.study.kafka.domain;

import java.util.function.Consumer;

public class Controller {

    public final static boolean ANIMAL = true;
    public final static boolean STRING = false;

    public final static int SPEED_MILLIES = 1_000;

    public static <T> void runAnimal(T obj, Consumer<T> consumer) {
        if (ANIMAL) {
            consumer.accept(obj);
        }
    }

    public static <T> void runString(T obj, Consumer<T> consumer) {
        if (STRING) {
            consumer.accept(obj);
        }
    }

}
