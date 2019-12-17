package jun.study.kafka.producer;

import jun.study.kafka.domain.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class TopicInitializer {

    private final AdminClient adminClient;

    public TopicInitializer(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void init() {
        try {
            initInternal();
        } catch (ExecutionException | InterruptedException ignored) { }
    }

    private void initInternal() throws ExecutionException, InterruptedException {
        System.out.println("Kafka Topic Initialize");
        final ListTopicsResult listTopicsResult = adminClient.listTopics();
        final String topics = String.join("", listTopicsResult.names().get());

        if (!topics.contains(KafkaConfig.TOPIC)) {
            final CreateTopicsResult createdResult =
                    adminClient.createTopics(Arrays.asList(new NewTopic(KafkaConfig.TOPIC, 1, (short) 1)));
            createdResult.all().get();
            System.out.println("created: " + String.join(",", createdResult.values().keySet()));
        }

        if (!topics.contains(KafkaConfig.CHANGED_TOPIC)) {
            final CreateTopicsResult createdResult =
                    adminClient.createTopics(Arrays.asList(new NewTopic(KafkaConfig.CHANGED_TOPIC, 1, (short) 1)));
            createdResult.all().get();
            System.out.println("created: " + String.join(",", createdResult.values().keySet()));
        }
    }

}
