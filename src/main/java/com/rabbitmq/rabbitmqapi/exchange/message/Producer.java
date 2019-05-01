package com.rabbitmq.rabbitmqapi.exchange.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.1.15");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        Map<String, Object> headers = new HashMap<>();
        headers.put("my1", 111);
        headers.put("my2", 222);

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .contentType("UTF-8")
                .deliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue())
                .expiration("10000")
                .headers(headers)
                .build();

        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ";
            channel.basicPublish("", "test01", props, msg.getBytes());
        }

        channel.close();
        connection.close();

    }

}
