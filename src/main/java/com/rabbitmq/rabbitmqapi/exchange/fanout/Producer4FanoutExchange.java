package com.rabbitmq.rabbitmqapi.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer4FanoutExchange {

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.15");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(3000);

        //获取连接
        Connection connection = factory.newConnection();

        //获取通道
        Channel channel = connection.createChannel();

        String exchangeName = "test.fanout.exchange";
        String routingKey = "";
        String msg = "hello rabbitmq 4 fanout exchange message...";

        //发送消息
        for(int i=0; i<10; i++){
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        }

        //关闭连接
        channel.close();
        connection.close();
    }

}
