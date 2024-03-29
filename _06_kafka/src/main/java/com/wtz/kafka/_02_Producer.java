package com.wtz.kafka;

import com.alibaba.fastjson.JSONException;
import com.wtz.kafka.entity.RandomValue;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/1/20 星期四
 */
public class _02_Producer {

//    private static final String BROKER_LIST = "flink01:9092,flink02:9092,flink03:9092";
        private static final String BROKER_LIST = "172.16.100.109:9092";

    public static void main(String[] args) throws JSONException, InterruptedException {

        final String brokerList = args[0];
        final String topic = args[1];

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        int i = 0;
        while (i > -1) {
            sendData(producer, topic, RandomValue.buildJsonData(i++));
            if (i > 100000) {
                break;
            }
        }
    }

    private static void sendData(KafkaProducer<String, String> producer, String topic, String data)
            throws InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);
        System.out.println(data);
        producer.send(
                record,
                (message, exception) -> {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                });
        producer.flush();
        //Thread.sleep(100);
    }
}
