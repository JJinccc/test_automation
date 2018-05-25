package com.ffan.qa.common.model.runner;

import lombok.Data;

@Data
public class TestCaseResult {
    private String name;
    private String classname;
    private Double time;
    private Boolean success;
    private TestFailure failure;

    public TestCaseResult(String name, String classname, Double time) {
        this.name = name;
        this.classname = classname;
        this.time = time;
    }

    public void success() {
        success = true;
        failure = null;
    }

    public void fail(String message, String stackTrace) {
        success = false;
        failure = new TestFailure(message, stackTrace);
    }
}
