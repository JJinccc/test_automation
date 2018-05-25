package com.ffan.qa.common.model.runner;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DiffResultItem {
    private Boolean isSame;
    private Integer bTotal;
    private Integer cTotal;
    private String sortName;
    private List<Map<String, Object>> diffBInclude;
    private List<Map<String, Object>> diffCInclude;
    private List<DiffStockItem> diffStocks;

    public DiffResultItem() {
        diffBInclude = new ArrayList<>();
        diffCInclude = new ArrayList<>();
        diffStocks = new ArrayList<>();
    }

    public void addBCoupon(Map<String, Object> item) {
        diffBInclude.add(item);
    }

    public void addCCoupon(Map<String, Object> item) {
        diffCInclude.add(item);
    }

    public void addStock(DiffStockItem item) {
        diffStocks.add(item);
    }
}
