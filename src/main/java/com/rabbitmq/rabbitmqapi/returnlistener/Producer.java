package com.rabbitmq.rabbitmqapi.returnlistener;

import com.rabbitmq.client.*;

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

        String exchangeName = "test.return.exchange";
        String routingKey = "return.save";
        String errorRoutingKey = "abc.save";
        String msg = "hello rabbitmq 4 topic return exchange message...";

        //发送消息
//        channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        //错误的routingKey消息
        channel.basicPublish(exchangeName, errorRoutingKey, true, null, msg.getBytes());


        //添加Return监听
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey
                    , AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);
                System.out.println("properties:" + properties);
                System.out.println("body:" + new String(body));
            }
        });

        //关闭连接
//        channel.close();
//        connection.close();
    }

}
