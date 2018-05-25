package com.ffan.qa.common.constants;

public class MQNames {
    private static final String QUEUESMS = "sms_queue";
    private static final String QUEUEEMAIL = "email_queue";

    public static String getSMSQueueName() {
        return QUEUESMS;
    }

    public static String getEmailQueueName() {
        return QUEUEEMAIL;
    }
}
