package com.wtz.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author tiezhu
 * @date 2022/2/22
 */
public class _01_FlinkKafka {

    private static final String BROKER_LIST = "kudu1:9092";

    private static final Logger LOG = LoggerFactory.getLogger(_01_FlinkKafka.class);

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        final Properties properties = new Properties();
        // 添加属性
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "worker02.zszqtest.com:9092,worker01.zszqtest.com:9092,worker03.zszqtest.com:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        FlinkKafkaConsumer<byte[]> consumer = new FlinkKafkaConsumer<>("src_uf20_hs_prod_entrust", new MDS(), properties);
        consumer.setStartFromTimestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 100L));
        env.addSource(consumer).returns(byte[].class).flatMap(
                new FlatMapFunction<byte[], String>() {
                    @Override
                    public void flatMap(byte[] data, Collector<String> collector) throws Exception {
                        final String s = new String(data, StandardCharsets.UTF_8);
                        if (s.contains("645000019200907070000191")
                                || s.contains("AALNZXAAlAAAFivAAE")) {
                            System.out.println("Data bytes: " + Arrays.toString(data));
                            System.out.println("Data String with utf-8: " + new String(data, StandardCharsets.UTF_8));
                            System.out.println("Data String with ISO_8859_1: " + new String(data, StandardCharsets.ISO_8859_1));
                            System.out.println("Data String with GBK: " + new String(data, "GBK"));
                            System.out.println("Data String with US_ASCII: " + new String(data, StandardCharsets.US_ASCII));
                            System.out.println("Data String with UTF_16: " + new String(data, StandardCharsets.UTF_16));
                            collector.collect(s);
                        }
                    }

                }).addSink(new SinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
            }
        });
        env.execute("11111111111111111111111111");
    }

    static class MDS implements DeserializationSchema<byte[]>, SerializationSchema<byte[]> {
        @Override
        public byte[] deserialize(byte[] bytes) throws IOException {
            return bytes;
        }

        @Override
        public byte[] serialize(byte[] bytes) {
            return bytes;
        }

        @Override
        public boolean isEndOfStream(byte[] bytes) {
            return false;
        }

        @Override
        public TypeInformation<byte[]> getProducedType() {
            return null;
        }
    }
}
