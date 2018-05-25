package com.ffan.qa.messages;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer extends RabbitEndPoint implements Runnable, Consumer {
    public RabbitConsumer(String endPointName) throws IOException, TimeoutException
    {
        super(endPointName);
    }

    public void run()
    {
        try
        {
            // start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, false,this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Called when consumer is registered.
     */
    public void handleConsumeOk(String consumerTag)
    {
        System.out.println("Consumer " + consumerTag + " registered");
    }

    /**
     * Called when new message is available.
     */
    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        String str = new String(bytes, "UTF-8");
        System.out.println(str + " received.");
    }

    public void handleCancel(String consumerTag)
    {
    }

    public void handleCancelOk(String consumerTag)
    {
    }

    public void handleRecoverOk(String consumerTag)
    {
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1)
    {
    }
}
