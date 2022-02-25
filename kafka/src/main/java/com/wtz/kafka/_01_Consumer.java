package com.wtz.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/1/20 星期四
 */
public class _01_Consumer {

    private static final String BROKER_LIST = "kudu1:9092";

    private static final String GROUP_ID = "*";

    public static void main(String[] args) throws Exception {
        final String topic = args[0];
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Object, Object> next : records) {
                Object value = next.value();
                if (value instanceof byte[]) {
                    System.out.println(new String((byte[]) value, StandardCharsets.UTF_8));
//                    System.out.println(new String((byte[]) value, StandardCharsets.ISO_8859_1));
//                    System.out.println(new String((byte[]) value, StandardCharsets.US_ASCII));
//                    System.out.println(next);
//                    byte2File((byte[]) value);
//                    System.out.println(Arrays.toString(((byte[]) value)));
                } else {
                    System.out.println(value.toString());
                }
            }
        }
    }

    private static void byte2File(byte[] byt) throws Exception {
        if (null == byt) {
            return;
        }
        String targetFile = "/Users/wtz/temp.txt";
        File file = new File(targetFile);
        OutputStream output = new FileOutputStream(file);
        BufferedOutputStream out = new BufferedOutputStream(output);
        InputStream in = new ByteArrayInputStream(byt);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = in.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        in.close();
        out.close();
//        File.WriteAllBytes("Foo.txt", arrBytes);
        System.out.println("---- done ----");
    }
}
