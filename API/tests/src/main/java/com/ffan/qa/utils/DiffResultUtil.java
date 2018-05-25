package com.ffan.qa.utils;

import com.ffan.qa.common.Logger;
import com.ffan.qa.common.model.runner.DiffResult;
import com.ffan.qa.common.model.runner.DiffResultItem;
import com.ffan.qa.common.model.runner.DiffStockItem;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class DiffResultUtil {
    private static final String compareFolderName = "compare";

    public static void createCompareFolder() {
        String fullPath = FileUtil.pathCombine(SystemUtil.getExecutePath(), compareFolderName);
        FileUtil.createDir(fullPath);
    }

    public static void deleteCompareFolder() {
        String fullPath = FileUtil.pathCombine(SystemUtil.getExecutePath(), compareFolderName);
        FileUtil.delDir(fullPath);
    }

    public static void writeCouponResult(String city, String plazaId, String plazaName, List<DiffResultItem> diffFiles, boolean addFailTag) {
        createCompareFolder();

        String fullPath = FileUtil.pathCombine(SystemUtil.getExecutePath(), compareFolderName, plazaId + (addFailTag ? "_fail" : "") + ".xml");

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("result");
        root.addAttribute("city", city);
        root.addAttribute("plazaId", plazaId);
        root.addAttribute("plazaName", plazaName);

        for (DiffResultItem diff: diffFiles) {
            Element diffEle = root.addElement("diff");
            diffEle.addAttribute("name", diff.getSortName());
            Element bincludeEle = diffEle.addElement("binclude");
            Element cincludeEle = diffEle.addElement("cinclude");
            Element stocksEle = diffEle.addElement("stocks");
            stocksEle.addAttribute("total", diff.getCTotal().toString());
            stocksEle.addAttribute("failCount", String.valueOf(diff.getDiffStocks().size()));
            stocksEle.addAttribute("failRate", String.valueOf((double) diff.getDiffStocks().size() * 100 / diff.getCTotal()));

            for(Map<String, Object> obj: diff.getDiffBInclude()) {
                Element couponEle = bincludeEle.addElement("coupon");
                couponEle.addAttribute("no", obj.get("couponNo").toString());
                couponEle.addAttribute("title", obj.get("title").toString());
                couponEle.addAttribute("beginTime", new Date(Long.parseLong(obj.get("beginTime").toString())).toString());
                couponEle.addAttribute("endTime", new Date(Long.parseLong(obj.get("endTime").toString())).toString());

            }

            for(Map<String, Object> obj: diff.getDiffCInclude()) {
                Element couponEle = cincludeEle.addElement("coupon");
                couponEle.addAttribute("no", obj.get("couponNo").toString());
                couponEle.addAttribute("title", obj.get("title").toString());
                couponEle.addAttribute("beginTime", obj.get("adtBeginTime").toString());
                couponEle.addAttribute("endTime", obj.get("adtEndTime").toString());
            }

            for (DiffStockItem sResult: diff.getDiffStocks()) {
                if (sResult.getCStock().equals(0)) {
                    Element stockEle = stocksEle.addElement("stock");
                    stockEle.addAttribute("schemeId", sResult.getSchemeId());
                    stockEle.addAttribute("couponNo", sResult.getCouponNo());
                    stockEle.addAttribute("title", sResult.getTitle());
                    stockEle.addAttribute("cStock", sResult.getCStock().toString());
                    stockEle.addAttribute("bStock", sResult.getBStock().toString());
                    stockEle.addAttribute("time", sResult.getTime());
                }
            }
        }

        // 创建输出格式(OutputFormat对象)
        OutputFormat format = OutputFormat.createPrettyPrint();

        ///设置输出文件的编码
//      format.setEncoding("GBK");

        try {
            // 创建XMLWriter对象
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(fullPath)), format);

            //设置不自动进行转义
            writer.setEscapeText(false);

            // 生成XML文件
            writer.write(document);

            //关闭XMLWriter对象
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DiffResult> readDiffResults(String resultsDir) {
        List<DiffResult> results = new ArrayList<>();

        File dir = new File(resultsDir);
        File[] resultFiles = dir.listFiles();
        for (File xmlFile:
             resultFiles) {
            if (xmlFile.getName().indexOf("_fail") > 0) {
                DiffResult result = readDiffCouponResult(xmlFile);
                results.add(result);
            }
        }

        return results;
    }

    private static DiffResult readDiffCouponResult(File xmlFile) {
        DiffResult result = new DiffResult();

        try
        {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);
            Element rootElement = document.getRootElement();
            result.setCity(rootElement.attributeValue("city"));
            result.setPlazaId(rootElement.attributeValue("plazaId"));
            result.setPlazaName(rootElement.attributeValue("plazaName"));

            List<Element> diffEles = rootElement.elements("diff");
            for (Element diffEle: diffEles) {
                DiffResultItem resultItem = new DiffResultItem();
                resultItem.setSortName(diffEle.attributeValue("name"));

                // 读取BInclude下Coupon
                Element bincludeEle = diffEle.element("binclude");
                List<Map<String, Object>> diffBInclude = readDiffCouponsFromElement(bincludeEle);
                resultItem.setDiffBInclude(diffBInclude);

                // 读取CInclude下Coupon
                Element cincludeEle = diffEle.element("cinclude");
                List<Map<String, Object>> diffCInclude = readDiffCouponsFromElement(cincludeEle);
                resultItem.setDiffCInclude(diffCInclude);

                // 读取Stock
                Element stocksEle = diffEle.element("stocks");
                List<DiffStockItem> stocks = readDiffStockFromElement(stocksEle);
                resultItem.setDiffStocks(stocks);

                result.addDiffItem(resultItem);
            }
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
        }

        return result;
    }

    private static List<Map<String, Object>> readDiffCouponsFromElement(Element element) {
        List<Map<String, Object>> coupons = new ArrayList<>();
        if (null != element){
            List<Element> couponEles = element.elements();
            for (Element cele: couponEles) {
                Map<String, Object> cm = new HashMap<>();
                cm.put("no", cele.attributeValue("no"));
                cm.put("title", cele.attributeValue("title"));
                cm.put("beginTime", cele.attributeValue("beginTime"));
                cm.put("endTime", cele.attributeValue("endTime"));
                coupons.add(cm);
            }
        }

        return coupons;
    }

    private static List<DiffStockItem> readDiffStockFromElement(Element element) {
        List<DiffStockItem> stocks = new ArrayList<>();
        if (null != element) {
            List<Element> stockEles = element.elements("stock");
            for (Element sEle:stockEles) {
                DiffStockItem stockItem = new DiffStockItem();
                stockItem.setSchemeId(sEle.attributeValue("schemeId"));
                stockItem.setCouponNo(sEle.attributeValue("couponNo"));
                stockItem.setTitle(sEle.attributeValue("title"));
                stockItem.setCStock(Integer.parseInt(sEle.attributeValue("cStock")));
                stockItem.setBStock(Integer.parseInt(sEle.attributeValue("bStock")));
                stockItem.setTime(sEle.attributeValue("time"));

                stocks.add(stockItem);
            }
        }
        return stocks;
    }
}
