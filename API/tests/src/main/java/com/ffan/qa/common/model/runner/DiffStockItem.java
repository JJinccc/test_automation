package com.ffan.qa.common.model.runner;

import lombok.Data;

@Data
public class DiffStockItem {
    private String schemeId;
    private String couponNo;
    private String title;
    private Integer cStock;
    private Integer bStock;
    private String time;
}
