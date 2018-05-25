package com.ffan.qa.common.model.runner;

import lombok.Data;

import java.util.List;

@Data
public class MailInfo {
    //邮箱服务器 如smtp.163.com
    private String host;
    //邮箱端口
    private String port;
    //用户邮箱 如**@163
    private String formName;
    //用户授权码 不是用户名密码 可以自行查看相关邮件服务器怎么查看
    private String formPassword;
    //消息回复邮箱
    private String replayAddress;
    //发送地址
    private List<String> toAddresses;
    //发送主题
    private String subject;
    //发送内容
    private String content;
    //附件
    private String[] attachments;
}
