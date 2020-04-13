package com.testing.appKw;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class DataDrivenOfApp {
	@SuppressWarnings("rawtypes")
	public AndroidDriver driver;
	public int line=0;
	public ExcelWriter appExcel;
	
	
	public DataDrivenOfApp(ExcelWriter excel) {
		appExcel=excel;
	}

	//强制等待
	public String wait(String time) {
		int t = 0;
		try {
			t = Integer.parseInt(time);
			Thread.sleep(t*1000);
			AutoLogger.log.info("强制等待 " + time + " 秒成功!");
			appExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error("强制等待 " + time +  " 秒出错！:" );
			AutoLogger.log.error(e,e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	// 脚本执行CMD命令的函数
	public String runCmd(String str) {
		String cmd = "cmd /c start " + str;
		Runtime runtime = Runtime.getRuntime();
		try {
			AutoLogger.log.info("执行cmd命令:"+str);
			runtime.exec(cmd);
			appExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error("cmd命令执行失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
	//通过cmd启动appium的服务
	public String StartAppium(String port, String time) {
		// 启动appium的服务端
		AutoLogger.log.info("启动appiumserver服务");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		String createdate = sdf.format(date);
	
		// 拼接文件名，形式为：工作目录路径+方法名+执行时间.png
		String appiumLogFile = "logs/" + createdate + "AppiumLog.txt";
		String startAppiumCMD ="appium -a 127.0.0.1 -p " + port + " --log " + appiumLogFile +" --local-timezone";
		AutoLogger.log.error(startAppiumCMD);
		System.out.println(startAppiumCMD);
		try {
			runCmd(startAppiumCMD);
			int t = 1000;
			t = Integer.parseInt(time);
			Thread.sleep(t);
			AutoLogger.log.error("启动appium服务成功");
			appExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (InterruptedException e) {
			AutoLogger.log.error("启动appium服务失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
		
	}
	
	/**
	 * 启动被测APP
	 * @param platformVersion  手机版本
	 * @param deviceName
	 * @param appPackage
	 * @param appActivity
	 * @param appiumServerIP
	 * @param time
	 * @return 
	 */
	public String runAPP(String platformVersion, String deviceName, String appPackage,String appActivity,String appiumServerIP,String time) {
			String status ="fail";
			try {
				AutoLogger.log.info("启动待测App");
				AppDriver app = new AppDriver(platformVersion, deviceName, appPackage, appActivity, appiumServerIP, time);
				driver = app.getdriver();
				status ="pass";
				AutoLogger.log.info("启动待测App成功");
				appExcel.writeCell(line, 10, "PASS");
			} catch (Exception e) {
				AutoLogger.log.error("启动待测App失败");
				AutoLogger.log.error(e, e.fillInStackTrace());
				appExcel.writeFailCell(line, 10, "FAIL");
			}
			return status;
	}

	public void runBrowser(String platformVersion,String deviceName,  String appiumServerIP,String waitTime) {
		try {
			AutoLogger.log.info("启动安卓浏览器");
			BrowserDriver browser = new BrowserDriver(platformVersion, deviceName, appiumServerIP, waitTime);
			driver = browser.getdriver();
			appExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error("启动安卓浏览器失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	// 测试微信小程序时，加载chromeoption设置，指定小程序进程，以便进行context切换。
	public void runMM(String platformVersion, String deviceName, String appPackage,String appActivity,String appiumServerIP,String time) {
			try {
				AutoLogger.log.info("启动待测App");
				AppDriver app = new AppDriver(platformVersion, deviceName, appPackage, appActivity, appiumServerIP, time);
				driver = app.getdriver();
				appExcel.writeCell(line, 10, "PASS");
			} catch (Exception e) {
				AutoLogger.log.error("启动待测App失败");
				AutoLogger.log.error(e, e.fillInStackTrace());
				appExcel.writeFailCell(line, 10, "FAIL");
			}
	}
	
	public void visitH5(String url) {
		try {
			AutoLogger.log.info("安卓浏览器访问"+url);
			driver.get(url);
			appExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	public String input(String xpath, String text) {
		try {
			WebElement ele = findele(xpath);
			ele.clear();
			ele.sendKeys(text);
			appExcel.writeCell(line, 10, "PASS");
			AutoLogger.log.info("向 " + xpath + " 输入 " + text +" 成功!");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error("输入 "+text+" 失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	public String click(String xpath) {
		try {
			WebElement ele = findele(xpath);
			ele.click();
			appExcel.writeCell(line, 10, "PASS");
			AutoLogger.log.info (xpath +" 点击成功!");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(xpath +" 点击失败!");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	// 调用adb滑动
	public void adbSwipe(int i, int j, int k, int l, int m) {
		try {
			this.runCmd("adb shell input swipe " + i + " " + j + " " + k + " " + l + " " + m);
			appExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行滑动失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	
	
	// 调用adb模拟按键
	public void adbPressKey(String keycode) {
		try {
			int k = Integer.parseInt(keycode);
			String cmd = " adb shell input keyevent " + k;
			runCmd(cmd);
			Thread.sleep(2000);
			appExcel.writeCell(line, 10, "PASS");
		} catch (InterruptedException e) {
			AutoLogger.log.error("通过adb执行按键事件失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	public void adbTap(String xAxis, String yAxis) {
		try {
			int x = Integer.parseInt(xAxis);
			int y = Integer.parseInt(yAxis);
			runCmd("adb shell input tap " + x + " " + y);
			appExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行点击失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	public void quitApp() {
		try {
			driver.closeApp();
			appExcel.writeFailCell(line, 10, "FAIL");
		} catch (Exception e) {
			AutoLogger.log.error("关闭app失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			
		}
	}

	public String closeAppium() {
		try {
			runCmd("taskkill /F /IM node.exe");
			appExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error("关闭appiumserver服务失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	// 断言
	public void assertSame(String xpath, String paramRes) {
		try {
			explicityWait(xpath);
			String result = driver.findElement(By.xpath(xpath)).getText();
			System.out.println(result);
			if (result.equals(paramRes)) {
				AutoLogger.log.info("测试用例执行成功");
				appExcel.writeCell(line, 10, "PASS");
			} else {
				AutoLogger.log.info("测试用例执行失败");
				appExcel.writeFailCell(line, 10, "FAIL");
			}
		} catch (Exception e) {
			AutoLogger.log.error("执行断言时报错");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	// 通过appium的方法进行滑屏
	public void appiumSwipe(String iniX, String iniY, String finX, String finY) {
		try {
			int x = Integer.parseInt(iniX);
			int y = Integer.parseInt(iniY);
			int x1 = Integer.parseInt(finX);
			int y1 = Integer.parseInt(finY);
			@SuppressWarnings("rawtypes")
			TouchAction action = new TouchAction(driver);
			@SuppressWarnings("rawtypes")
			PointOption pressPoint=PointOption.point(x, y);
			@SuppressWarnings("rawtypes")
			PointOption movePoint=PointOption.point(x1, y1);
			action.longPress(pressPoint).moveTo(movePoint).release().perform();
			appExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	// 使用appium的方法点击坐标
	public void appiumTap(String x, String y) {
		try {
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			@SuppressWarnings("rawtypes")
			TouchAction action = new TouchAction(driver);
			@SuppressWarnings("rawtypes")
			PointOption pressPoint=PointOption.point(xAxis, yAxis);
			// action类分解动作，先长按，再移动到指定位置，再松开
			action.tap(pressPoint).release().perform();
			appExcel.writeCell(line, 10, "PASS");
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium点击坐标方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	// 使用appium方法长按
	public void appiumHold(String x, String y, String time) {
		try {
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			int t = Integer.parseInt(time);
			@SuppressWarnings("rawtypes")
			PointOption pressPoint=PointOption.point(xAxis, yAxis);
			Duration last = Duration.ofSeconds(t);
			@SuppressWarnings("rawtypes")
			TouchAction action = new TouchAction(driver);
			action.longPress(pressPoint).waitAction(WaitOptions.waitOptions(last)).perform();
			appExcel.writeCell(line, 10, "PASS");
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	/**
	 * 实现显式等待的方法，在每次定位元素时，先尝试找元素，给10秒钟的最长等待。
	 */
	public void explicityWait(final String xpath) {
		try {
			WebDriverWait eWait = new WebDriverWait(driver, 10);
			eWait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return d.findElement(By.xpath(xpath));
				}
			});
		} catch (Exception e) {
			AutoLogger.log.error(e);
		}
	}
	
	/**
	 * 实现隐式等待的方法
	 */
	public void implicitlyWait() {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			appExcel.writeFailCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error(e,e.fillInStackTrace());
			appExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	public void printContexts() {
		@SuppressWarnings("unchecked")
		Set<String> contexts=driver.getContextHandles();
		for(String s:contexts) {
			System.out.println(s);
			AutoLogger.log.info("context:"+s);
		}
	}

	public void switchContext(String contextName) {
		try {
			AutoLogger.log.info("切换到"+contextName+"context");
			driver.context(contextName);
		} catch (Exception e) {
			AutoLogger.log.error("切换context失败。");
		}
	}
	
	public void saveScrShot(String method) {
		// 获取当前的执行时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		// 当前时间的字符串
		String createdate = sdf.format(date);
		// 拼接文件名，形式为：工作目录路径+方法名+执行时间.png
		String scrName = "SCRshot/" + method + createdate + ".png";
		// 以当前文件名创建文件
		File scrShot = new File(scrName);
		// 将截图保存到临时文件
		File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(tmp, scrShot);
		} catch (IOException e) {
			AutoLogger.log.error(e,e.fillInStackTrace());
			AutoLogger.log.error("截图失败！");
		}
	}
	
	//获取屏幕尺寸
	public Dimension getSize() {
		Dimension size = driver.manage().window().getSize();
		return size;
	}
	
//	public String swipeUp() {
//		try {
//			int width = getSize().width;
//			int height = getSize().height;
//			wait("1");
////			driver.swipe(width/2, height*3/4, width/2, height*1/4);
//			appExcel.writeFailCell(line, 10, "PASS");
//		} catch (Exception e) {
//			AutoLogger.log.error(e,e.fillInStackTrace());
//			appExcel.writeFailCell(line, 10, "FAIL");
//		}
//		
//		return "fail";
//	}
//	
	
	//定位页面元素
	public WebElement findele(String path) {
		WebElement ele = null;
		try {
			if(path.startsWith("/")) {
				ele = driver.findElement(By.xpath(path));
			}
			else {
				try {
					driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					ele = driver.findElementByAccessibilityId(path);
				}
				catch (Exception e) {
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					ele = driver.findElement(By.id(path));
				}
			}
		} catch (Exception e) {
			AutoLogger.log.error(path + " 查找失败!");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
		return ele;
	}	

}
