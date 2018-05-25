package com.ffan.qa.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BLoginModel {
    private String userId;
    private String tenantId;
    private String token;
}
