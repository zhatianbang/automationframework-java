package com.failRetry;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
 
public class Retry implements IRetryAnalyzer {
    private int retryCount         = 0;
    private int maxRetryCount     = 2;   // 失败测试重跑2次
 
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}