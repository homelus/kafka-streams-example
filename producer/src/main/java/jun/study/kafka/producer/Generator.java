package jun.study.kafka.producer;

public abstract class Generator {

    public void generate() {
        new Thread(this::execute).start();
    }

    protected abstract void execute();

}
