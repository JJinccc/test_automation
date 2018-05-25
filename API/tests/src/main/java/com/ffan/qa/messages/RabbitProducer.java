package com.ffan.qa.messages;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitProducer extends RabbitEndPoint {
    public RabbitProducer(String endPointName) throws IOException, TimeoutException
    {
        super(endPointName);
    }

    public void sendMessage(String message) throws IOException {
        channel.basicPublish("", endPointName, null,message.getBytes("UTF-8"));
    }
}
