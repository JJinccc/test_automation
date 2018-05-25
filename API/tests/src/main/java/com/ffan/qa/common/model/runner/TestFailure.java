package com.ffan.qa.common.model.runner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestFailure {
    private String message;
    private String stackTrace;
}
