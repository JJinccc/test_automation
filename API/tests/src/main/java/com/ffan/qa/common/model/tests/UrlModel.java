package com.ffan.qa.common.model.tests;

import lombok.Data;

import java.util.Date;

@Data
public class UrlModel {
    private Integer baseUrlId;
    private String baseUrlKey;
    private String baseUrl;
    private Boolean expired;
    private String creator;
    private Date createdTime;
}