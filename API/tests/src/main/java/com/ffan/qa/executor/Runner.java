package com.ffan.qa.executor;

import com.ffan.qa.common.constants.MQNames;
import com.ffan.qa.common.model.runner.*;
import com.ffan.qa.utils.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Runner {
    private static Logger logger = Logger.getLogger(Runner.class);
    private static TaskConfig config;

    private static final String resultHistoryFolder = "results";
    private static final String testResultFolder = "test-output";
    private static final Integer seedNumber = 100000;
    private static Instant lastSendDataSms = Instant.now().plus(Duration.ofDays(-1));
    private static Instant lastRunTime = Instant.now().plus(Duration.ofDays(-1));

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        logger.info("准备开始执行");

        logger.info("读取配置");
        config = TaskConfigUtil.readConfig();

        boolean isLinux = SystemUtil.isOSLinux();
        logger.info(isLinux ? "当前是Linux操作系统" : "当前是Windows操作系统");

        if (config.getMode().equals("single")) {
            for (Task task : config.getTasks()) {
                executeTask(task);
            }
            logger.info("执行完毕");
        } else if (config.getMode().equals("loop")) {
            // 循环执行
            while (true) {
                if (Duration.between(lastRunTime, Instant.now()).getSeconds() > config.getInterval()) {
                    lastRunTime = Instant.now();
                    for (Task task : config.getTasks()) {
                        if (canRun()) {
                            executeTask(task);
                        }
                    }
                    logger.info("执行完毕，稍后继续执行...");
                } else {
                    Thread.sleep(1 * 1000);
                }
            }

        }

    }

    /**
     * 执行任务
     *
     * @param task
     */
    public static void executeTask(Task task) {
        FileUtil.delDir(FileUtil.pathCombine(SystemUtil.getExecutePath(), "compare"));
        FileUtil.delDir(FileUtil.pathCombine(SystemUtil.getExecutePath(), "test-output", "xml"));
        executeTestNg(task.getFile());

        // 转储历史测试结果中
        Integer index = getNewIndex(task);
        transferResults(task, index);
        analyzeTaskResults(task, index);
    }

    /**
     * 执行TestNg测试
     *
     * @param testNgFile
     */
    public static void executeTestNg(String testNgFile) {
        //String preCmd = SystemUtil.isOSLinux() ? "/bin/sh" : "cmd.exe /c";
        String cmd = "java -classpath \"" + config.getClasspath() + "\" org.testng.TestNG " + testNgFile;
        if (SystemUtil.isOSLinux()) {
            cmd = "/bin/sh run.sh \"" + config.getClasspath() + "\" " + testNgFile;
        }
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            //Process process = Runtime.getRuntime().exec(preCmd + " run.sh");
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
            errorGobbler.start();
            outputGobbler.start();
            process.waitFor();
        } catch (Exception ex) {
            logger.error(ex);
        }

    }

    /**
     * 获取任务索引
     *
     * @param task
     * @return
     */
    public static Integer getNewIndex(Task task) {
        String taskRoot = FileUtil.pathCombine(resultHistoryFolder, task.getName());
        FileUtil.createDir(resultHistoryFolder);
        FileUtil.createDir(taskRoot);
        String filePath = FileUtil.pathCombine(taskRoot, "index.log");
        FileUtil.createFile(filePath);

        try {
            String content = FileUtil.readFileContent(filePath);
            if (StringUtil.isNullOrEmpty(content)) {
                FileUtil.writeFileContent(filePath, seedNumber.toString());
                return seedNumber;
            } else {
                String[] parts = content.split("\n");
                // -2是因为/r/n会多一行空行
                Integer newIndex = Integer.parseInt(parts[parts.length - 2]) + 1;
                FileUtil.writeFileContent(filePath, newIndex.toString());
                return newIndex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return 0;
    }

    /**
     * 拷贝测试结果文件
     *
     * @param task
     * @param index
     */
    public static void transferResults(Task task, Integer index) {
        try {
            String taskResultFolder = getTaskRoot(task, index);
            FileUtil.createDir(resultHistoryFolder);
            FileUtil.createDir(FileUtil.pathCombine(resultHistoryFolder, task.getName()));
            FileUtil.createDir(taskResultFolder);
            FileUtil.copyFile(FileUtil.pathCombine(testResultFolder, "indexEx.html"), FileUtil.pathCombine(taskResultFolder, "indexEx.html"));
            FileUtil.copyDir(FileUtil.pathCombine(SystemUtil.getExecutePath(), testResultFolder, "xml"), FileUtil.pathCombine(SystemUtil.getExecutePath(), taskResultFolder, "xml"));
            FileUtil.copyDir(FileUtil.pathCombine(SystemUtil.getExecutePath(), "compare"), FileUtil.pathCombine(SystemUtil.getExecutePath(), taskResultFolder, "compare"));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void analyzeTaskResults(Task task, Integer index) {
        String resultXmlPath = FileUtil.pathCombine(getTaskRoot(task, index), "xml");
        TestSuiteResult result = TestResultUtil.parseTestSuiteResult(resultXmlPath);

        if (task.getType().equals("func")) {
            if (null != result && (result.getFailuresCount() + result.getErrorsCount() + result.getSkippedCount()) > 0) {
                sendResult(task, index, result, false);
            } else {
                sendResult(task, index, result, true);
            }
        } else if (task.getType().equals("data")) {
//            List<DiffResult> df = compareDataDiff(
//                    DiffResultUtil.readDiffResults(FileUtil.pathCombine(getTaskRoot(task, index))),
//                    DiffResultUtil.readDiffResults(FileUtil.pathCombine(getTaskRoot(task, index - 1))));
//            if (df.size() > 0) {
//                sendDataResult(task, index, result, df, false);
//            } else {
//                sendDataResult(task, index, result, df,true);
//            }
            Set<String> keys = JedisUtil.getKeys("RESULT_DIFFCOUPON_*_DIFF");
            if (null != keys && keys.size() > 0) {
                sendDataResult(task, index, keys, false);
            } else {
                sendDataResult(task, index, keys, true);
            }
        }
    }

    public static void sendResult(Task task, Integer index, TestSuiteResult result, boolean onlyMe) {
        List<String> toAddress = task.getInformEmailList();
        List<String> phoneList = task.getInformPhoneList();
        if (onlyMe) {
            toAddress.clear();
            phoneList.clear();
            toAddress.add("liqianyang@wanda.cn");
        }

        Integer passrate = (result.getTotalCount() - result.getFailuresCount() - result.getErrorsCount() - result.getSkippedCount()) * 100 / result.getTotalCount();
        String subject = task.getName();
        if (!passrate.equals(100)) {
            subject += "异常";
        } else {
            subject += "(" + passrate.toString() + "%)";
        }
        MailInfo mailInfo = new MailInfo();
        mailInfo.setSubject(subject);
        mailInfo.setContent("<h1>本轮任务执行完毕，详情请查看附件。遇到问题请联系李倩阳(liqianyang)</h1>");
        if (task.getType().equals("func")) {
            mailInfo.setAttachments(new String[]{FileUtil.pathCombine(getTaskRoot(task, index), "indexEx.html")});
        } else if (task.getType().equals("data")) {
            File compDir = new File(FileUtil.pathCombine(getTaskRoot(task, index), "compare"));
            File[] xmlFiles = compDir.listFiles();
            List<String> attachedFiles = new ArrayList<>();
            attachedFiles.add(FileUtil.pathCombine(getTaskRoot(task, index), "indexEx.html"));
            for (File f :
                    xmlFiles) {
                if (f.getName().indexOf("_fail") > 0) {
                    attachedFiles.add(f.getAbsolutePath());
                }
            }
            mailInfo.setAttachments(attachedFiles.toArray(new String[0]));
        }
        mailInfo.setToAddresses(toAddress);
        try {
            MailSendUtil.sendHtmlMail(mailInfo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        if (!onlyMe) {
//            if (task.getType().equals("data") && Duration.between(lastSendDataSms, Instant.now()).getSeconds() < 4 * 60 * 60) {
//                return;
//            }
            SmsSendUtil.sendSMS(phoneList, "小程序线上API监控发现功能异常，请及时查看邮件确认详细信息！");
        }
    }

    public static void sendDataResult(Task task, Integer index, Set<String> keys, boolean onlyMe) {
        List<String> toAddress = task.getInformEmailList();
        List<String> phoneList = task.getInformPhoneList();
        String subject = task.getName();
        if (null == keys || keys.size() == 0) {
            toAddress.clear();
            phoneList.clear();
            toAddress.add("liqianyang@wanda.cn");
            subject += "正常（100%）";
        } else {
            subject += "异常";
        }

        MailInfo mailInfo = new MailInfo();
        mailInfo.setSubject(subject);
        mailInfo.setContent(getEmailContent(keys));
        //mailInfo.setAttachments(new String[]{FileUtil.pathCombine(getTaskRoot(task, index), "indexEx.html")});
        mailInfo.setToAddresses(toAddress);
        try {
            MailSendUtil.sendHtmlMail(mailInfo);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        if (!onlyMe) {
            if (task.getType().equals("data") && Duration.between(lastSendDataSms, Instant.now()).getSeconds() < 4 * 60 * 60) {
                return;
            }
            lastSendDataSms = Instant.now();
            SmsSendUtil.sendSMS(phoneList, "小程序线上API监控发现数据异常，请及时查看邮件确认详细信息！");
        }
    }

    private static String getEmailContent(Set<String> keys) {
        String content = "";

        String heder = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-family:'微软雅黑',Helvetica,Arial,sans-serif;font-size:14px \" width=\"100%\" style=\"border: 0; font-family: 'Microsoft YaHei UI Light'; padding:0;\">\n" +
                "  <thead style=\"background-color: rgb(223, 216, 232); font-size: 18px;\">\n" +
                "  <tr>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">键值</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">类型</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">券码</td>\n" +
                "  </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";

        content += heder;
        for (String key: keys) {
            String val = JedisUtil.get(key);
            content += String.format("<tr>" +
                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                            "  </tr>",
                    key,
                    key.contains("_B_") ? "千帆存在，小程序不显示" : "小程序显示，千帆不存在",
                    val);
        }

        content += "</tbody></table>";
        return content;
    }

    private static String buildStockContent(List<DiffResult> results) {
        String stockHeader = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-family:'微软雅黑',Helvetica,Arial,sans-serif;font-size:14px \" width=\"100%\" style=\"border: 0; font-family: 'Microsoft YaHei UI Light'; padding:0;\">\n" +
                "  <thead style=\"background-color: rgb(223, 216, 232); font-size: 18px;\">\n" +
                "  <tr>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">标题</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">券码</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">schemeId</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">小程序显示</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">千帆显示</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">执行时间</td>\n" +
                "  </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";
        String content = "";

        // 生成Stock节点
        if (hasStockDiff(results)) {
            content += "<h1>券库存不一致</h1>";
            for (DiffResult result: results) {
                if (hasStockDiff(result)) {
                    content += String.format("<h3>%s</h3>", result.getPlazaName()) + stockHeader;
                    for(DiffResultItem resultItem: result.getDiffItems()) {
                        for(DiffStockItem stockItem: resultItem.getDiffStocks()) {
                            content += String.format("<tr>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "  </tr>",
                                    stockItem.getTitle(),
                                    stockItem.getCouponNo(),
                                    stockItem.getSchemeId(),
                                    stockItem.getCStock().toString(),
                                    stockItem.getBStock().toString(),
                                    stockItem.getTime());
                        }
                    }
                    content += "</tbody></table>";
                }
            }
        }

        return content;
    }

    private static String buildCIncludeContent(List<DiffResult> results) {
        String stockHeader = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-family:'微软雅黑',Helvetica,Arial,sans-serif;font-size:14px \" width=\"100%\" style=\"border: 0; font-family: 'Microsoft YaHei UI Light'; padding:0;\">\n" +
                "  <thead style=\"background-color: rgb(223, 216, 232); font-size: 18px;\">\n" +
                "  <tr>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">标题</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">券码</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">开始时间</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">结束时间</td>\n" +
                "  </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";
        String content = "";

        // 生成Stock节点
        if (hasCIncludeDiff(results)) {
            content += "<h1>券只在小程序出现</h1>";
            for (DiffResult result: results) {
                if (hasCIncludeDiff(result)) {
                    content += String.format("<h3>%s</h3>", result.getPlazaName()) + stockHeader;
                    for(DiffResultItem resultItem: result.getDiffItems()) {
                        for(Map<String, Object> diffItem: resultItem.getDiffCInclude()) {
                            content += String.format("<tr>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "  </tr>",
                                    diffItem.get("title").toString(),
                                    diffItem.get("no").toString(),
                                    diffItem.get("beginTime").toString(),
                                    diffItem.get("endTime").toString());
                        }
                    }
                    content += "</tbody></table>";
                }
            }
        }

        return content;
    }

    private static String buildBIncludeContent(List<DiffResult> results) {
        String stockHeader = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-family:'微软雅黑',Helvetica,Arial,sans-serif;font-size:14px \" width=\"100%\" style=\"border: 0; font-family: 'Microsoft YaHei UI Light'; padding:0;\">\n" +
                "  <thead style=\"background-color: rgb(223, 216, 232); font-size: 18px;\">\n" +
                "  <tr>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">标题</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">券码</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">开始时间</td>\n" +
                "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">结束时间</td>\n" +
                "  </tr>\n" +
                "  </thead>\n" +
                "  <tbody>";
        String content = "";

        // 生成Stock节点
        if (hasBIncludeDiff(results)) {
            content += "<h1>券只在千帆出现</h1>";
            for (DiffResult result: results) {
                if (hasBIncludeDiff(result)) {
                    content += String.format("<h3>%s</h3>", result.getPlazaName()) + stockHeader;
                    for(DiffResultItem resultItem: result.getDiffItems()) {
                        for(Map<String, Object> diffItem: resultItem.getDiffBInclude()) {
                            content += String.format("<tr>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "    <td style=\"border: solid 1px rgb(211, 202, 221); padding: 5px 10px;\">%s</td>" +
                                            "  </tr>",
                                    diffItem.get("title").toString(),
                                    diffItem.get("no").toString(),
                                    diffItem.get("beginTime").toString(),
                                    diffItem.get("endTime").toString());
                        }
                    }
                    content += "</tbody></table>";
                }
            }
        }

        return content;
    }

    private static boolean hasStockDiff(List<DiffResult> results) {
        for (DiffResult r:
             results) {
            boolean fileHas = hasStockDiff(r);
            if (fileHas) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasStockDiff(DiffResult result) {
        for (DiffResultItem item:
                result.getDiffItems()) {
            if (item.getDiffStocks().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasBIncludeDiff(List<DiffResult> results) {
        for (DiffResult r:
                results) {
            boolean fileHas = hasBIncludeDiff(r);
            if (fileHas) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasBIncludeDiff(DiffResult result) {
        for (DiffResultItem item:
                result.getDiffItems()) {
            if (item.getDiffBInclude().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCIncludeDiff(List<DiffResult> results) {
        for (DiffResult r:
                results) {
            boolean fileHas = hasCIncludeDiff(r);
            if (fileHas) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCIncludeDiff(DiffResult result) {
        for (DiffResultItem item:
                result.getDiffItems()) {
            if (item.getDiffCInclude().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private static String getTaskRoot(Task task, Integer index) {
        return resultHistoryFolder + File.separator + task.getName() + File.separator + index.toString();
    }

    private static List<DiffResult> compareDataDiff(List<DiffResult> newList, List<DiffResult> oldList) {
        List<DiffResult> retList = new ArrayList<>();

        for (DiffResult currentResult: newList) {
            boolean hasSame = false;
            DiffResult preResult = findDiffResult(oldList, currentResult.getPlazaId());
            if (null != preResult) {
                DiffResult tempResult = new DiffResult();
                tempResult.setCity(currentResult.getCity());
                tempResult.setPlazaId(currentResult.getPlazaId());
                tempResult.setPlazaName(currentResult.getPlazaName());

                for (DiffResultItem currentItem: currentResult.getDiffItems()) {
                    DiffResultItem preItem = findDiffItem(preResult.getDiffItems(), currentItem.getSortName());
                    if (null != preItem) {
                        boolean sortSame = false;
                        DiffResultItem tempDiffItem = new DiffResultItem();
                        tempDiffItem.setSortName(currentItem.getSortName());
                        tempDiffItem.setBTotal(currentItem.getBTotal());
                        tempDiffItem.setCTotal(currentItem.getCTotal());
                        tempDiffItem.setIsSame(false);
                        for (Map<String, Object> currentBCoupon: currentItem.getDiffBInclude()) {
                            if (hasSameCoupon(preItem.getDiffBInclude(), currentBCoupon)) {
                                hasSame = true;
                                sortSame = true;
                                tempDiffItem.addBCoupon(currentBCoupon);
                            }
                        }

                        for (Map<String, Object> currentCCoupon: currentItem.getDiffCInclude()) {
                            if (hasSameCoupon(preItem.getDiffCInclude(), currentCCoupon)) {
                                hasSame = true;
                                sortSame = true;
                                tempDiffItem.addCCoupon(currentCCoupon);
                            }
                        }

                        for (DiffStockItem currentStock: currentItem.getDiffStocks()) {
                            if (hasSameStock(preItem.getDiffStocks(), currentStock)) {
                                hasSame = true;
                                sortSame = true;
                                tempDiffItem.addStock(currentStock);
                            }
                        }

                        if (sortSame) {
                            tempResult.addDiffItem(tempDiffItem);
                        }
                    }
                }

                if (hasSame) {
                    retList.add(tempResult);
                }
            }
        }

        return retList;
    }

    private static DiffResult findDiffResult(List<DiffResult> list, String plazaId) {
        for (DiffResult r: list) {
            if (r.getPlazaId().equals(plazaId)) {
                return r;
            }
        }
        return null;
    }

    private static DiffResultItem findDiffItem(List<DiffResultItem> list, String sortName) {
        for (DiffResultItem item: list) {
            if (item.getSortName().equals(sortName)) {
                return item;
            }
        }
        return null;
    }

    private static boolean hasSameCoupon(List<Map<String, Object>> list, Map<String, Object> expectCoupon) {
        if (null == list) {
            return false;
        }
        for(Map<String, Object> mo: list) {
            if (mo.get("no").toString().equals(expectCoupon.get("no").toString())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSameStock(List<DiffStockItem> list, DiffStockItem expectStock) {
        if (null == list) {
            return false;
        }

        for (DiffStockItem item: list) {
            if (item.getCouponNo().equals(expectStock.getCouponNo())) {
                return true;
            }
        }
        return false;
    }

    private static boolean canRun() {
        return !FileUtil.exists(FileUtil.pathCombine(SystemUtil.getExecutePath(), "stop.txt"));
    }
}
