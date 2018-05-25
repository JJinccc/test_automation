package com.ffan.qa.common.model.runner;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteResult {
    @Getter
    private String testSuiteName;
    @Getter
    private Integer totalCount;
    @Getter
    private Integer failuresCount;
    @Getter
    private Integer errorsCount;
    @Getter
    private Integer skippedCount;
    private List<TestClassResult> testClassResults;

    public TestSuiteResult(String testSuiteName) {
        this.testSuiteName = testSuiteName;
        this.totalCount = 0;
        this.failuresCount = 0;
        this.errorsCount = 0;
        this.skippedCount = 0;

        testClassResults = new ArrayList<>();
    }

    public void addTestClassResult(TestClassResult result) {
        this.testClassResults.add(result);
        this.totalCount += result.getTotalCount();
        this.failuresCount += result.getFailuresCount();
        this.errorsCount += result.getErrorsCount();
        this.skippedCount += result.getSkippedCount();
    }
}
