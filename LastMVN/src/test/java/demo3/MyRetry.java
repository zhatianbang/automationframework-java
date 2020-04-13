
package demo3;
 
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
 
/**
 * create by Anthony on 2017/11/27
 */
public class MyRetry implements IRetryAnalyzer{
    // 设置当前失败执行的次数
    private int retryCount = 1;
    // 设置最大失败执行次数
    private static int maxRetryCount = 3;
 
    @Override
    public boolean retry(ITestResult iTestResult) {
        if(retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}
