package jun.study.kafka.config;

import jun.study.kafka.VoidConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Controller {

    public final static int SPEED_MILLIE_SECOND = 1_000;
    public final static int PROCESSOR_NUM = 2;

    public static void run(RunningConfig t, VoidConsumer consumer) {
        if (t.isRun()) {
            consumer.run();
        }
    }

    public static <T> void run(RunningConfig config, T obj, Consumer<T> consumer) {
        if (config.isRun()) {
            consumer.accept(obj);
        }
    }

    public static <Consumer, Topic> void run(RunningConfig config, Consumer consumer, Topic topic,
                                             BiConsumer<Consumer, Topic> biConsumer) {
        if (config.isRun()) {
            biConsumer.accept(consumer, topic);
        }
    }

}
