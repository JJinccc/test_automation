package com.ffan.qa.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CLoginModel {
    private String phoneNumber;
    private String cookieStr;
    private String memberId;
    private String puid;
    private String ploginToken;
}
