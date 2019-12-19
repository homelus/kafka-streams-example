package jun.study.kafka.config;

public enum RunningConfig {

    ANIMAL(false, "animal", "animal-agg"),
    STRING(false, "string", "string-changed"),
    WORD(false, "word", "word-agg"),
    PRODUCTION(true, "production", "production-changed"),
    ;

    private boolean run;

    private String srcTopic;

    private String desTopic;

    RunningConfig(boolean run, String srcTopic, String desTopic) {
        this.run = run;
        this.srcTopic = srcTopic;
        this.desTopic = desTopic;
    }

    public boolean isRun() {
        return run;
    }

    public String srcTopic() {
        return srcTopic;
    }

    public String desTopic() {
        return desTopic;
    }
}
