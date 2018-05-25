package com.ffan.qa.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private String wx;
    private String name;
    private String phone;
    private String email;
}
