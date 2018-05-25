package com.ffan.qa.utils;


import com.ffan.qa.common.model.Person;
import com.ffan.qa.common.model.runner.Task;
import com.ffan.qa.common.model.runner.TaskConfig;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class TaskConfigUtil {
    private static Logger logger = Logger.getLogger(TaskConfigUtil.class);
    public static TaskConfig readConfig() {
        String configName = "task-config.xml";
        TaskConfig tc = new TaskConfig();

        // 解析xml文档内容
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(configName);
            Element rootElement = document.getRootElement();

            // 读取persons节点
            Element personsEle = rootElement.element("persons");
            List<Element> personEles = personsEle.elements();
            for (Element pe: personEles) {
                tc.addPerson(new Person(
                        pe.element("wx").getText().trim(),
                        pe.element("name").getText().trim(),
                        pe.element("phone").getText().trim(),
                        pe.element("email").getText().trim()
                ));
            }

            // 获取settings节点
            Element settingsEle = rootElement.element("settings");

            // 读取root
            tc.setRoot(settingsEle.element("root").getText().trim());

            // 读取classpath
            tc.setClasspath(settingsEle.element("classpath").getText().trim());

            // 读取mode
            tc.setMode(settingsEle.element("mode").getText().trim());

            // 读取interval
            tc.setInterval(Integer.parseInt(settingsEle.element("interval").getText().trim()));

            // 读取alarm
            tc.setAlarm(Boolean.parseBoolean(settingsEle.element("alarm").getText().trim()));

            // 读取tasks节点
            Element tasksEle = rootElement.element("tasks");
            List<Element> taskEles = tasksEle.elements();
            for (Element te: taskEles) {
                Task task = new Task(
                        te.element("name").getText().trim(),
                        te.element("file").getText().trim(),
                        te.element("type").getText().trim(),
                        te.element("inform").getText().trim()
                );
                task.generateInfoList(tc.getPersonMap());
                tc.addTask(task);
            }
        } catch (Exception e) {
            logger.error("Read config xml error: "+ e);
            return null;
        }
        return tc;
    }
}
