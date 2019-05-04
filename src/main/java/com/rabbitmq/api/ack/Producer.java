package com.rabbitmq.api.ack;

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

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.2");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(3000);

        //获取连接
        Connection connection = factory.newConnection();

        //获取通道
        Channel channel = connection.createChannel();

        String exchangeName = "test.ack.exchange";
        String routingKey = "ack.save";


        //发送消息
        for (int i = 0; i < 5; i++) {
            Map<String,Object> headers = new HashMap<>();
            headers.put("num", i);

            String msg = "hello rabbitmq 4 topic ack exchange message..." + i;

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .deliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue())
                    .contentEncoding("UTF-8")
                    .build();
            channel.basicPublish(exchangeName, routingKey, true, props, msg.getBytes());
        }

        //关闭连接
//        channel.close();
//        connection.close();
    }
}
