package com.bonaparte.service;

import com.alibaba.fastjson.JSON;
import com.bonaparte.bean.PonaparteMqMessage;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by yangmingquan on 2018/9/13.
 * kafka 消息，producer, consumer,
 *       zookeeper
 */
@Component
public class KafkaMqService {
    @Resource(name = "kafkaTemplate")
    private KafkaTemplate kafkaTemplate;

    private static Log log = LogFactory.getLog(KafkaMqService.class);

    /**
     * kafka 消息发送
     * */
    public Map<String, Object> sendMesForTemplate(PonaparteMqMessage message,
                                                  Long key,
                                                  String topic){
        MessageHeaders messageHeaders = message.getHeaders();
        Map<String, Object> msg = new HashedMap();
        msg.put("application", "Ponaparte");
        msg.put("initTime", messageHeaders.getTimestamp());
        msg.put("body", message.getMessageBody());
        message.setMessageBody(msg);
        log.info("Kafka 发送消息:" + JSON.toJSONString(message));
        Random random = new Random();
        // 依据消息的key分区
        Integer partionIndex = random.nextInt(3) + 1;
        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(topic, partionIndex, key, message);
        return null;
    }

    /**
     * kafka 消息消费
     * */
    @KafkaListener(topics = {"${spring.kafka.consumer.topic}"})
    public Object ponaparteKafkaConsumer(String data){
        log.info("kafka消费消息");
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
