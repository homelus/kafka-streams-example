package jun.study.kafka.domain;

public enum RunningConfig {

    ANIMAL(true),
    STRING(true),
    WORD(true);

    private boolean run;

    RunningConfig(boolean run) {
        this.run = run;
    }

    public boolean isRun() {
        return run;
    }
}
