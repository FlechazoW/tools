package com.wtz.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/1/20 星期四
 */
public class _01_Consumer {

    private static final String BROKER_LIST = "172.16.22.182:9992";

    private static final String GROUP_ID = "*";

    public static void main(String[] args) {
        final String topic = args[0];
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(1));
            for (Object re : records) {
                System.out.println(re.toString());
            }
        }
    }
}
