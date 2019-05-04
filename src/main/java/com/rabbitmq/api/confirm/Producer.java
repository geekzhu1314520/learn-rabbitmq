package com.rabbitmq.api.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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

        //开启确认模式
        channel.confirmSelect();

        String exchangeName = "test.confirm.exchange";
        String routingKey = "confirm.save";
        String msg = "hello rabbitmq 4 topic confirm exchange message...";

        //发送消息
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //添加监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck ...." + deliveryTag + "," + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack ...." + deliveryTag + "," + multiple);
            }
        });

        //关闭连接
//        channel.close();
//        connection.close();
    }

}
