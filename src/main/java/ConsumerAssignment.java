import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;

import java.util.Arrays;

public class ConsumerAssignment {
    public static void create(KafkaConsumer<String, String> consumer) throws ConfigException {
        if (System.getenv("TOPIC") == null) {
            throw new ConfigException("Missing required config 'topic'.");
        } else {
            if (System.getenv("PARTITION") != null) {
                TopicPartition partition = new TopicPartition(System.getenv("TOPIC"),
                                                              Integer.parseInt(System.getenv("PARTITION")));
                consumer.assign(Arrays.asList(partition));
            } else {
                consumer.subscribe(Arrays.asList(System.getenv("TOPIC")));
            }
        }
    }
}
