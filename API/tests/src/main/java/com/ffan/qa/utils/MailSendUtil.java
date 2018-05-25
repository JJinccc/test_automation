package com.ffan.qa.utils;

import com.ffan.qa.common.model.runner.MailInfo;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class MailSendUtil {
//    private final static String host = "smtp.163.com"; //163的服务器
//    private final static String port = "25"; //163邮箱端口
//    private final static String formName = "atinfovip@163.com";//你的邮箱
//    private final static String password = "wanda123"; //授权码
//    private final static String replayAddress = "atinfovip@163.com"; //你的邮箱

    private final static String host = "10.77.135.200"; //163的服务器
    private final static String port = "10025"; //163邮箱端口
    private final static String formName = "gerrit@ffan.cn";//你的邮箱
    private final static String password = "rTSZ],@@%J"; //授权码
    private final static String replayAddress = "APIAutomation"; //你的邮箱


    public static void sendHtmlMail(MailInfo info) throws Exception {
        info.setHost(host);
        info.setPort(port);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);

        Message message = getMessage(info);
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(info.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        // 添加附件
        if (null != info.getAttachments() && info.getAttachments().length > 0) {
            for (String file : info.getAttachments()) {
                html = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file); //得到数据源
                html.setDataHandler(new DataHandler(fds)); //得到附件本身并放入BodyPart
                html.setFileName(MimeUtility.encodeText(fds.getName()));  //得到文件名并编码（防止中文文件名乱码）同样放入BodyPart
                mainPart.addBodyPart(html);
            }
        }

        // 将MiniMultipart对象设置为邮件内容
        message.setContent(mainPart);
        Transport.send(message);
    }

    public static void sendTextMail(MailInfo info) throws Exception {
        info.setHost(host);
        info.setFormName(formName);
        info.setFormPassword(password);   //网易邮箱的授权码~不一定是密码
        info.setReplayAddress(replayAddress);
        Message message = getMessage(info);
        //消息发送的内容
        message.setText(info.getContent());

        Transport.send(message);
    }

    private static Message getMessage(MailInfo info) throws Exception {
        final Properties p = System.getProperties();
        p.setProperty("mail.smtp.host", info.getHost());
        p.setProperty("mail.smtp.port", info.getPort());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", info.getFormName());
        p.setProperty("mail.smtp.pass", info.getFormPassword());

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getInstance(p, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mail.smtp.user"), p.getProperty("mail.smtp.pass"));
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        //消息发送的主题
        message.setSubject(info.getSubject());
        //接受消息的人
        message.setReplyTo(InternetAddress.parse(info.getReplayAddress()));
        //消息的发送者
        message.setFrom(new InternetAddress(p.getProperty("mail.smtp.user"), "API Automation"));
        // 创建邮件的接收者地址，并设置到邮件消息中
        //message.setRecipient(Message.RecipientType.TO,new InternetAddress(info.getToAddress()));
        InternetAddress[] addresses = new InternetAddress[info.getToAddresses().size()];
        for (int i = 0; i < info.getToAddresses().size(); i++) {
            addresses[i] = new InternetAddress(info.getToAddresses().get(i));
        }
        message.setRecipients(Message.RecipientType.TO, addresses);
        // 消息发送的时间
        message.setSentDate(new Date());


        return message;
    }
}
