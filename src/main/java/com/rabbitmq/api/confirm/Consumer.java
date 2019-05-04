package com.rabbitmq.api.confirm;

import com.rabbitmq.client.*;

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

        String exchangeName = "test.confirm.exchange";
        String queueName = "test.confirm.queue";
        String routingKey = "confirm.*";

        //声明交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //通过路由键绑定交换机和队列
        channel.queueBind(queueName, exchangeName, routingKey);

        //绑定消费者与队列
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到的消息为：" + msg);
        }
    }
}
