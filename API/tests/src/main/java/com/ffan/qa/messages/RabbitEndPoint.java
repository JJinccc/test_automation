package com.ffan.qa.messages;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.ffan.qa.settings.TestConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class RabbitEndPoint {
    protected Channel channel;

    protected Connection connection;

    protected String endPointName;

    public RabbitEndPoint(String endpointName) throws IOException, TimeoutException
    {
        this.endPointName = endpointName;

        // Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(TestConfig.getCurrent().getRabbitHost());
        factory.setPort(TestConfig.getCurrent().getRabbitPort());
        factory.setUsername(TestConfig.getCurrent().getRabbitUser());
        factory.setPassword(TestConfig.getCurrent().getRabbitPassword());

        // getting a connection
        connection = factory.newConnection();

        // creating a channel
        channel = connection.createChannel();

        // declaring a queue for this channel. If queue does not exist,
        // it will be created on the server.
        // queueDeclare的参数：queue 队列名；durable true为持久化；exclusive 是否排外，true为队列只可以在本次的连接中被访问，
        // autoDelete true为connection断开队列自动删除；arguments 用于拓展参数
        channel.queueDeclare(endpointName, false, false, false, null);
    }

    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * @throws IOException
     */
    public void close() throws IOException, TimeoutException
    {
        this.channel.close();
        this.connection.close();
    }
}
