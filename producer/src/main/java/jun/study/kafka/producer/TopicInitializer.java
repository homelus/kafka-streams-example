package jun.study.kafka.producer;

import jun.study.kafka.config.KafkaConfig;
import jun.study.kafka.config.RunningConfig;
import org.apache.kafka.clients.admin.*;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static jun.study.kafka.config.Controller.*;
import static jun.study.kafka.config.RunningConfig.*;

public class TopicInitializer {

    private final AdminClient adminClient;

    public TopicInitializer(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void init() {
        for (RunningConfig config : values()) {
            if (config == MERGE) {
                run(config, asList(config.srcTopic() + "1", config.desTopic() + "1"),
                        this::initInternal);
                run(config, asList(config.srcTopic() + "2", config.desTopic() + "2"),
                        this::initInternal);
            } else {
                run(config, asList(config.srcTopic(), config.desTopic()), this::initInternal);
            }
        }
    }

    private void initInternal(List<String> tps) {
        try {
            System.out.println(tps.get(0) + " Topic Initialize");
            final ListTopicsResult listTopicsResult = adminClient.listTopics();
            final String topics = String.join(",", listTopicsResult.names().get());
            System.out.println("current topics : " + topics);

            for (String tp : tps) {
                if (topics.contains(tp)) {
                    final DeleteTopicsResult deleteTopicsResult =
                            adminClient.deleteTopics(Collections.singleton(tp));
                    deleteTopicsResult.all().get(1, TimeUnit.SECONDS);
                    System.out.println(tp + " is Deleted");
                    TimeUnit.MILLISECONDS.sleep(500);
                }


                final NewTopic newTopic = new NewTopic(tp, KafkaConfig.PARTITION_SIZE, KafkaConfig.REPLICATION_FACTOR);
                final CreateTopicsResult createdResult =
                        adminClient.createTopics(
                                asList(newTopic));
                createdResult.all().get();
                System.out.println("created: " + String.join(",", createdResult.values().keySet())
                        + ", partitions: " + newTopic.numPartitions());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
