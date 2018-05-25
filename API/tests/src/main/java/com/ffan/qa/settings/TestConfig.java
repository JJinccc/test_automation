package com.ffan.qa.settings;

import com.ffan.qa.common.Logger;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Data
public class TestConfig {
    private static final String configName = "test-config.xml";
    private static TestConfig current = null;
    private String testEnv;
    private String redisHost;
    private String redisPort;
    private String mysqlUrl;
    private String mysqlUser;
    private String mysqlPassword;
    private String dataSource;
    private String rabbitHost;
    private Integer rabbitPort;
    private String rabbitUser;
    private String rabbitPassword;

    public TestConfig() {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(configName);
            Element rootElement = document.getRootElement();

            testEnv = rootElement.element("testEnv").getTextTrim();

            Element dataSettingsEle = rootElement.element("dataSettings");
            Element redisEle = dataSettingsEle.element("redis");
            redisHost = redisEle.element("host").getTextTrim();
            redisPort = redisEle.element("port").getTextTrim();

            Element mysqlEle = dataSettingsEle.element("mysql");
            mysqlUrl = mysqlEle.element("url").getTextTrim();
            mysqlUser = mysqlEle.element("user").getTextTrim();
            mysqlPassword = mysqlEle.element("password").getTextTrim();

            dataSource = dataSettingsEle.element("dataSource").getTextTrim();

            Element rabbitEle = dataSettingsEle.element("rabbitMQ");
            rabbitHost = rabbitEle.elementTextTrim("host");
            rabbitPort = Integer.parseInt(rabbitEle.elementTextTrim("port"));
            rabbitUser = rabbitEle.elementTextTrim("user");
            rabbitPassword = rabbitEle.elementTextTrim("password");
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }

    public static TestConfig getCurrent() {
        if (null == current) {
            current = new TestConfig();
        }
        return current;
    }
}
