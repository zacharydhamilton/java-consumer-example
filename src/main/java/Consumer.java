import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws IOException {
        Properties props = loadProps("/config/setup.properties");
//        Properties props = loadProps("setup.properties");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, (System.getenv("GROUP_ID") != null) ? System.getenv("GROUP_ID") : "consumer-examples");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, (System.getenv("AUTO_OFFSET_RESET") != null) ? System.getenv("AUTO_OFFSET_RESET") : "earliest");

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        ConsumerAssignment.create(consumer);

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    System.out.printf("key: %s, value: %s\n", key, value);
                }
            }
        } finally {
            consumer.close();
        }
    }
    private static Properties loadProps(String file) throws IOException {
        if (!Files.exists(Paths.get(file))) {
            throw new IOException(file + " does not exist or could not be found.");
        }
        final Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return props;
    }
}
