package com.ffan.qa.executor;

import com.ffan.qa.messages.RabbitConsumer;

public class AgentRunner {
    public static void main(String[] args) throws Exception {
        RabbitConsumer consumer = new RabbitConsumer("sms_queue");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }
}
