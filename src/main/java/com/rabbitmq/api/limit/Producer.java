package com.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
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

        String exchangeName = "test.qos.exchange";
        String routingKey = "qos.save";
        String msg = "hello rabbitmq 4 topic qos exchange message...";

        //发送消息
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }

        //关闭连接
//        channel.close();
//        connection.close();
    }
}
