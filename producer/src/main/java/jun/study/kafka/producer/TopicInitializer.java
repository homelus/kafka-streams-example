package jun.study.kafka.producer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.admin.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static jun.study.kafka.domain.Controller.runAnimal;
import static jun.study.kafka.domain.Controller.runString;

public class TopicInitializer {

    private final AdminClient adminClient;

    public TopicInitializer(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void init() {
        runString(asList(KafkaConfig.STRING_TOPIC, KafkaConfig.STRING_CHANGED_TOPIC), this::initInternal);
        runAnimal(asList(KafkaConfig.ANIMAL_TOPIC, KafkaConfig.ANIMAL_AGGS_TOPIC), this::initInternal);
    }

    private void initInternal(List<String> tps) {
        try {
            System.out.println(tps.get(0) + " Topic Initialize");
            final ListTopicsResult listTopicsResult = adminClient.listTopics();
            final String topics = String.join("", listTopicsResult.names().get());

            for (String tp : tps) {
                if (topics.contains(tp)) {
                    final DeleteTopicsResult deleteTopicsResult =
                            adminClient.deleteTopics(Collections.singleton(tp));
                    deleteTopicsResult.all().get();
                    System.out.println(tp + " is Deleted");
                    TimeUnit.SECONDS.sleep(1);
                }

                final CreateTopicsResult createdResult =
                        adminClient.createTopics(asList(new NewTopic(tp, KafkaConfig.PARTITION_SIZE,
                                KafkaConfig.REPLICATION_FACTOR)));
                createdResult.all().get();
                System.out.println("created: " + String.join(",", createdResult.values().keySet()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
