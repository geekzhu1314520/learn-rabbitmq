package com.rabbitmq.rabbitmqapi.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer4DirectExchange {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

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

        String exchangeName = "test.direct.exchange";
        String queueName = "test.direct.queue";
        String routingKey = "test.direct";

        //声明一个交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true,  false, false, null);
        //声明一个队列
        channel.queueDeclare(queueName, true, false, false,null);
        //交换机和队列建立绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        //队列和消费者建立关系
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费内容：" + msg);
        }

    }

}
