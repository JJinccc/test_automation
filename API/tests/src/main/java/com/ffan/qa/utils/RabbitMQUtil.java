package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;
import com.ffan.qa.settings.TestConfig;
import com.rabbitmq.client.*;

public class RabbitMQUtil {
    private static ConnectionFactory factory;

    static {
        factory = new ConnectionFactory();
        factory.setHost(TestConfig.getCurrent().getRabbitHost());
        factory.setPort(TestConfig.getCurrent().getRabbitPort());
        factory.setUsername(TestConfig.getCurrent().getRabbitUser());
        factory.setPassword(TestConfig.getCurrent().getRabbitPassword());
    }

    public static void sendMessage(String queueName, String message) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            channel.close();
            connection.close();
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }

    }
}
