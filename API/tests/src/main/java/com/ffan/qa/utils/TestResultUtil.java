package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;
import com.ffan.qa.common.model.runner.TestCaseResult;
import com.ffan.qa.common.model.runner.TestClassResult;
import com.ffan.qa.common.model.runner.TestSuiteResult;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.runners.model.TestClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestResultUtil {
    public static TestSuiteResult parseTestSuiteResult(String xmlDir) {
        File dir = new File(xmlDir);
        File[] classResultFiles = dir.listFiles();

        TestSuiteResult result = new TestSuiteResult("testSuite");

        for (File classResultFile: classResultFiles) {
            TestClassResult cResult = parseTestClassResult(classResultFile.getAbsolutePath());
            if (null != cResult) {
                result.addTestClassResult(cResult);
            }
        }

        return result;
    }

    private static TestClassResult parseTestClassResult(String filePath) {
        File inputXml = new File(filePath);
        String className = inputXml.getName().replace("_results", "");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputXml);
            Element tsEle = document.getRootElement();
            String suiteName = tsEle.attributeValue("name");
            Integer totalCount = Integer.parseInt(tsEle.attributeValue("tests"));
            Integer failuresCount = Integer.parseInt(tsEle.attributeValue("failures"));
            Integer errorsCount = Integer.parseInt(tsEle.attributeValue("errors"));
            Integer skippedCount = Integer.parseInt(tsEle.attributeValue("skipped"));

            TestClassResult result = new TestClassResult(className, suiteName, totalCount, failuresCount, errorsCount, skippedCount);

            List<Element> testEles = tsEle.elements("testcase");
            for (Element testEle : testEles) {
                result.addTestCaseResult(parseTestCaseResult(testEle));
            }

            return result;
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return null;
        }
    }

    private static TestCaseResult parseTestCaseResult(Element testcaseEle) {
        String name = testcaseEle.attributeValue("name");
        String classname = testcaseEle.attributeValue("classname");
        Double time = Double.parseDouble(testcaseEle.attributeValue("time"));
        TestCaseResult result = new TestCaseResult(name, classname, time);
        Element failureEle = testcaseEle.element("failure");
        if (null == failureEle) {
            result.success();
        } else {
            result.fail(failureEle.attributeValue("message"), failureEle.getText());
        }
        return result;
    }
}
