package com.ffan.qa.utils;

import com.ffan.qa.common.model.TestData;
import com.ffan.qa.common.model.TestDataList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class ExcelUtil {
    /**
     * 读取Excel
     * @param path
     * @param sheetName
     * @return
     */
    public static TestDataList readTestData(String path, String sheetName) {
        if (StringUtil.isNullOrEmpty(path)) {
            return null;
        }
        InputStream is;
        Workbook workbook;
        try {
            is = new FileInputStream(path);
            if (path.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
            is.close();
            return readTestData(workbook, sheetName);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("转换excel文件失败：" + e.getMessage());
        }

    }

    private static TestDataList readTestData(Workbook workbook, String sheetName) {
        TestDataList tdl = new TestDataList();

        Sheet sheet = workbook.getSheet(sheetName);
        // 获取第一行环境数量
        Row firstRow = sheet.getRow(0);
        if (null == firstRow) {
            return tdl;
        }

        List<Object> heads = getRow(firstRow);
        for (int i = 1; i < heads.size(); i++) {
            tdl.addData(new TestData(heads.get(i).toString(), new HashMap<>()));
        }

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            List<Object> rowData = getRow(sheet.getRow(i));

            for (int j = 1; j < rowData.size(); j++) {
                tdl.addParam(heads.get(j).toString(), rowData.get(0).toString(), rowData.get(j));
            }
        }

        return tdl;
    }

    public static Object[][] readProviderData(String path, String sheetName) {
        if (StringUtil.isNullOrEmpty(path)) {
            return null;
        }
        InputStream is;
        Workbook workbook;
        try {
            is = new FileInputStream(path);
            if (path.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
            is.close();
            return readProviderData(workbook, sheetName);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("转换excel文件失败：" + e.getMessage());
        }
    }

    public static boolean sheetExist(String path, String sheetName) {
        if (StringUtil.isNullOrEmpty(path)) {
            return false;
        }
        if (!FileUtil.exists(path)) {
            return false;
        }
        InputStream is;
        Workbook workbook;
        try {
            is = new FileInputStream(path);
            if (path.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
            is.close();
            Sheet sheet = workbook.getSheet(sheetName);
            return null != sheet;
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("转换excel文件失败：" + e.getMessage());
        }
    }

    private static Object[][] readProviderData(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        Integer maxRowNum = sheet.getLastRowNum();

        // 读取第一行key名称
        Row firstRow = sheet.getRow(0);
        List<Object> keys = getRow(firstRow);

        Object[][] datas = new Object[maxRowNum][1];
        for (int row = 1; row <= maxRowNum; row++) {
            Row dataRow = sheet.getRow(row);
            List<Object> rowDatas = getRow(dataRow);
            Map<String, Object> item = new HashMap<>();
            for(int i = 0; i < keys.size(); i++) {
                item.put(keys.get(i).toString(), rowDatas.get(i));
            }
            datas[row - 1][0] = item;
        }

        return datas;
    }

    private static List<Object> getRow(Row row) {
        List<Object> cells = new ArrayList<Object>();
        if (row != null) {
            for (short cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                Cell xssfCell = row.getCell(cellNum);
                cells.add(getValue(xssfCell));
            }
        }
        return cells;
    }

    private static String getValue(Cell cell) {
        if (null == cell) {
            return "";
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(cell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(cell.getStringCellValue());
        }
    }
}
