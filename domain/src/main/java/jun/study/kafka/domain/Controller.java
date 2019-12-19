package jun.study.kafka.domain;

import java.util.function.Consumer;

public class Controller {

    public final static int SPEED_MILLIES = 1_000;

    public static void run(RunningConfig t, VoidConsumer consumer) {
        if (t.isRun()) {
            consumer.run();
        }
    }

    public static <T> void run(RunningConfig t, T obj, Consumer<T> consumer) {
        if (t.isRun()) {
            consumer.accept(obj);
        }
    }

}
