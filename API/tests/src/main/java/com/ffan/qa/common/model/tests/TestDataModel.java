package com.ffan.qa.common.model.tests;

import lombok.Data;

import java.util.Date;

@Data
public class TestDataModel {
    private Integer dataId;
    private String dataKey;
    private String dataValue;
    private Boolean expired;
    private String creator;
    private Date createdTime;
}
