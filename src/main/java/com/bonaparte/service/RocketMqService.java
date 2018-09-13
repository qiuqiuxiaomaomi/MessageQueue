package com.bonaparte.service;

import com.bonaparte.bean.RocketmqEvent;
import com.bonaparte.constant.RocketmqProps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Created by yangmingquan on 2018/9/13.
 * rocketmq
 */
@Service
public class RocketMqService {
    private static Log log = LogFactory.getLog(KafkaMqService.class);
    @Autowired
    private DefaultMQProducer defaultMQProducer;
    @Autowired
    private RocketmqProps rocketmqProps;

    /**
     * rocketmq 消息发送
     * */
    public void defaultSendMsg(String data){
        Message msg = new Message(rocketmqProps.getSendTopic(),
                rocketmqProps.getTags(),
                data.getBytes());

        try{
            defaultMQProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息发送成功");
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("消息发送失败");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * rocketmq consumer
     * 消费特定topic的消息
     * */
    @EventListener(condition = "#event.topic==${spring.kafka.consumer.topic}  &&  #event.tag=='rocket'")
    public void rockemqMsgListen(RocketmqEvent event) {
        try {

        } catch (Exception e) {

        }
    }
}
