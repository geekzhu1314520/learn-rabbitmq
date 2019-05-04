package com.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.15");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(3000);

        //通过工厂创建连接
        Connection connection = factory.newConnection();

        //通过连接创建通道
        Channel channel = connection.createChannel();

        //发布消息
        String exchangeName = "test.direct.exchange";
        String routingKey = "test.direct";
        String msg = "Hello 4 direct exchange";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //关闭连接
        channel.close();
        connection.close();
    }

}
