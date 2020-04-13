
package com.testing.runner;
 
import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;
 
/**
 * create by Anthony on 2017/11/18
 */
public class RunTestNG {
 
    public static void main(String[] args) throws InterruptedException {
 
          TestNG testNG = new TestNG();
          List<String> suites = new ArrayList<String>();
          suites.add(".\\testng.xml");
          //suites.add(".\\test-output\\testng-failed.xml");
          testNG.setTestSuites(suites);
          testNG.run();
 
          // 等待执行结束，然后去执行失败用例
          TestNG testNG1 = new TestNG();
          List<String> suites1 = new ArrayList<String>();
          Thread.sleep(5000);
          suites1.add(".\\test-output\\testng-failed.xml");
          testNG1.setTestSuites(suites1);
          testNG1.run();
 
 
        }
}
