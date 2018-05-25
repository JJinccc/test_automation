package com.ffan.qa.common.model.runner;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TestClassResult {
    @Getter
    private String testClassName;
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
    private List<TestCaseResult> testCaseResults;

    public TestClassResult(String testClassName, String testSuiteName, Integer totalCount, Integer failuresCount, Integer errorsCount, Integer skippedCount) {
        this.testClassName = testClassName;
        this.testSuiteName = testSuiteName;
        this.totalCount = totalCount;
        this.failuresCount = failuresCount;
        this.errorsCount = errorsCount;
        this.skippedCount = skippedCount;

        testCaseResults = new ArrayList<>();
    }

    public void addTestCaseResult(TestCaseResult result) {
        testCaseResults.add(result);
    }
}
