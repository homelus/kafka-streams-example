package jun.study.kafka.producer.generator;

import jun.study.kafka.config.RunningConfig;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

import static jun.study.kafka.config.RunningConfig.MERGE;

@Service
public class MergeGenerator extends Generator {

    public MergeGenerator(Producer<String, String> producer) {
        super(producer);
    }

    @Override
    public void generate() {
        Merge1Generator m1 = new Merge1Generator(producer);
        Merge2Generator m2 = new Merge2Generator(producer);
        m1.generate();
        m2.generate();
    }

    @Override
    protected String getMessage() {
        return null;
    }

    @Override
    public RunningConfig runningConfig() {
        return MERGE;
    }

    public static class Merge1Generator extends Generator {

        private AtomicInteger keyCnt = new AtomicInteger(0);

        private AtomicInteger valCnt = new AtomicInteger(0);

        public Merge1Generator(Producer<String, String> producer) {
            super(producer);
        }

        @Override
        protected String key() {
            return String.valueOf(keyCnt.getAndAdd(1));
        }

        @Override
        protected String getMessage() {
            return "merge-1-data-" + valCnt.getAndAdd(2);
        }

        @Override
        protected String topic() {
            return "merge1";
        }

        @Override
        public RunningConfig runningConfig() {
            return MERGE;
        }
    }

    public static class Merge2Generator extends Generator {

        private AtomicInteger keyCnt = new AtomicInteger(0);

        private AtomicInteger valCnt = new AtomicInteger(1);

        public Merge2Generator(Producer<String, String> producer) {
            super(producer);
        }

        @Override
        protected String key() {
            return String.valueOf(keyCnt.getAndAdd(2));
        }

        @Override
        protected String getMessage() {
            return "merge-2-data-" + valCnt.getAndAdd(2);
        }

        @Override
        protected String topic() {
            return "merge2";
        }

        @Override
        public RunningConfig runningConfig() {
            return MERGE;
        }
    }


}
