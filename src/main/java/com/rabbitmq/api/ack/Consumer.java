package com.rabbitmq.api.ack;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

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
        String queueName = "test.ack.queue";
        String routingKey = "ack.#";

        //声明交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //通过路由键绑定交换机和队列
        channel.queueBind(queueName, exchangeName, routingKey);

        //绑定消费者与队列,把autoAck设置为false，即手动签收模式
        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
