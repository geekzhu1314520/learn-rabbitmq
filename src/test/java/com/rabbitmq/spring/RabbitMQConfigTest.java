package com.rabbitmq.spring;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQConfigTest {

    @Autowired
    private RabbitAdmin rabbitAdmin;


    @Test
    public void testRabbitAdmin(){

        String directExchange = "test_direct";
        String topicExchange = "test_topic";
        String fanoutExchange = "test_fanout";
        rabbitAdmin.declareExchange(new DirectExchange(directExchange, false, false));
        rabbitAdmin.declareExchange(new TopicExchange(topicExchange, false, false));
        rabbitAdmin.declareExchange(new FanoutExchange(fanoutExchange, false, false));

        String directQueue = "test_direct_queue";
        String topicQueue = "test_topic_queue";
        String fanoutQueue = "test_fanout_queue";
        rabbitAdmin.declareQueue(new Queue(directQueue, false));
        rabbitAdmin.declareQueue(new Queue(topicQueue, false));
        rabbitAdmin.declareQueue(new Queue(fanoutQueue, false));

        rabbitAdmin.declareBinding(new Binding(directQueue, Binding.DestinationType.QUEUE, directExchange
                , "direct", new HashMap<>()));
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue(topicQueue, false))
                .to(new TopicExchange(topicExchange, false, false))
                .with("user.#"));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(fanoutQueue, false))
                .to(new FanoutExchange(fanoutExchange, false, false)));

        rabbitAdmin.purgeQueue(topicQueue, false);

    }

}