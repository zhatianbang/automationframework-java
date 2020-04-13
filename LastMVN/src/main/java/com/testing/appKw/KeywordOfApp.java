package com.testing.appKw;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.testing.appKw.BrowserDriver;
import com.testing.common.AutoLogger;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class KeywordOfApp {
	private static final WebElement NULL = null;
	//声明成员变量driver，在所有的方法当中都需要调用
	public AndroidDriver driver;

	public KeywordOfApp() {
		
	}

	/**
	 * 强制等待
	 * @param 时间，字符串，单位秒
	 */
	public void wait(String time) {
		int t = 0;
		try {
			t = Integer.parseInt(time);
			Thread.sleep(t*1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 脚本执行CMD命令的函数
	public void runCmd(String str) {
		//打开新的cmd窗口执行指定的dos命令
		String cmd = "cmd /c start " + str;
		Runtime runtime = Runtime.getRuntime();
		try {
			AutoLogger.log.info("执行cmd命令:"+str);
			runtime.exec(cmd);
		} catch (Exception e) {
			AutoLogger.log.error("cmd命令执行失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}
	
	//通过cmd启动appium的服务
	public void StartAppium(String port, String time) {
		// 启动appium的服务端
		AutoLogger.log.info("启动appiumserver服务");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		// 当前时间的字符串
		String createdate = sdf.format(date);
		// 拼接文件名，形式为：工作目录路径+执行时间+AppiumLog.txt
		String appiumLogFile = "log/" + createdate + "AppiumLog.txt";
		String startAppiumCMD ="appium -a 127.0.0.1 -p " + port + " --log " + appiumLogFile +" --local-timezone --log-timestamp";
		runCmd(startAppiumCMD);
		try {
			int t = 1000;
			t = Integer.parseInt(time);
			Thread.sleep(t*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// 启动被测APP
	public void runAPP(String platformVersion, String deviceName, String appPackage,String appActivity,String appiumServerIP,String time) {
			try {
				AutoLogger.log.info("启动待测App");
				AppDriver app = new AppDriver(platformVersion, deviceName, appPackage, appActivity, appiumServerIP, time);
				driver = app.getdriver();
			} catch (Exception e) {
				AutoLogger.log.error("启动待测App失败");
				AutoLogger.log.error(e, e.fillInStackTrace());
			}
	}
	
	//对纯H5网页在手机浏览器上进行测试时，启动手机浏览器进行访问
	public void runBrowser(String platformVersion,String deviceName,  String appiumServerIP,String waitTime) {
		try {
			AutoLogger.log.info("启动安卓浏览器");
			BrowserDriver browser = new BrowserDriver(platformVersion, deviceName, appiumServerIP, waitTime);
			driver = browser.getdriver();
		} catch (Exception e) {
			AutoLogger.log.error("启动安卓浏览器失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}
	
	// 测试微信小程序时，加载chromeoption设置，指定小程序进程，以便进行context切换。
	public void runMM(String platformVersion, String deviceName, String appPackage,String appActivity,String appiumServerIP,String time) {
			try {
				AutoLogger.log.info("启动待测App");
				MMDriver app = new MMDriver(platformVersion, deviceName, appPackage, appActivity, appiumServerIP, time);
				driver = app.getdriver();
			} catch (Exception e) {
				AutoLogger.log.error("启动待测App失败");
				AutoLogger.log.error(e, e.fillInStackTrace());
			}
	}
	
	public void visitH5(String url) {
		try {
			AutoLogger.log.info("安卓浏览器访问"+url);
			driver.get(url);
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}
	
	public void input(String xpath, String text) {
		try {
//			explicityWait(xpath);
			WebElement ele = findele(xpath);
			ele.clear();
			ele.sendKeys(text);
			AutoLogger.log.info(xpath+"元素中输入"+text);
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("input");
		}
	}

	public void click(String xpath) {
		try {
//			explicityWait(xpath);
			WebElement ele = findele(xpath);
			ele.click();
			AutoLogger.log.info(xpath+" 进行点击成功");
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("click");
		}
	}

	// 调用adb滑动
	public void adbSwipe(int i, int j, int k, int l) {
		try {
			this.runCmd("adb shell input swipe " + i + " " + j + " " + k + " " + l + " ");
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行滑动失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	// 调用adb模拟按键
	public void adbPressKey(String keycode) {
		try {
			int k = Integer.parseInt(keycode);
			String cmd = " adb shell input keyevent " + k;
			runCmd(cmd);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			AutoLogger.log.error("通过adb执行按键事件失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}
	
	// 调用adb模拟Tap
	public void adbTap(String xAxis, String yAxis) {
		try {
			int x = Integer.parseInt(xAxis);
			int y = Integer.parseInt(yAxis);
			runCmd("adb shell input tap " + x + " " + y);
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行点击失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	public void quitApp() {
		try {
			driver.closeApp();
		} catch (Exception e) {
			AutoLogger.log.error("关闭app失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	//关闭Appium并退出cmd
	public void closeAppium() {
		
		try {
			runCmd("taskkill /F /IM node.exe");
			runCmd("taskkill /F /IM cmd.exe");
		} catch (Exception e) {
			AutoLogger.log.error("关闭appiumserver服务失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	//关闭正在运行的cmd
	public void killCMD() {
        Runtime rt = Runtime.getRuntime();
        try {
            rt
                    .exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");//关闭正在运行的cmd窗口
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	// 断言
	public void assertSame(String xpath, String paramRes) {
		try {
			explicityWait(xpath);
			String result = driver.findElement(By.xpath(xpath)).getText();
//			System.out.println(result);
			if (result.equals(paramRes)) {
				AutoLogger.log.info("测试用例执行成功");
			} else {
				AutoLogger.log.info("测试用例执行失败");
			}
		} catch (Exception e) {
			AutoLogger.log.error("执行断言时报错");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	// 通过appium的方法进行滑屏
	public void appiumSwipe(String iniX, String iniY, String finX, String finY) {
		try {
			//string型的参数转换为int型
			int x = Integer.parseInt(iniX);
			int y = Integer.parseInt(iniY);
			int x1 = Integer.parseInt(finX);
			int y1 = Integer.parseInt(finY);
			TouchAction action = new TouchAction(driver);
			//设置起点和终点
			PointOption pressPoint=PointOption.point(x, y);
			PointOption movePoint=PointOption.point(x1, y1);
			//滑动操作由长按起点->移动到终点->松开组成。
			action.longPress(pressPoint).moveTo(movePoint).release().perform();
		} catch (Exception e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	// 使用appium的方法点击坐标
	public void appiumTap(String x, String y) {
		try {
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			TouchAction action = new TouchAction(driver);
			PointOption pressPoint=PointOption.point(xAxis, yAxis);
			// action类分解动作，先长按，再移动到指定位置，再松开
			action.tap(pressPoint).release().perform();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium点击坐标方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	// 使用appium方法长按
	public void appiumHold(String x, String y, String time) {
		try {
			//string转int
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			int t = Integer.parseInt(time);
			//指定长按的坐标
			PointOption pressPoint=PointOption.point(xAxis, yAxis);
			//长按的时间，使用java提供的time类库中的duration类
			Duration last = Duration.ofSeconds(t);
			WaitOptions wait=WaitOptions.waitOptions(last);
			TouchAction action = new TouchAction(driver);
			//长按坐标->指定按住的时间进行等待
			action.longPress(pressPoint).waitAction(WaitOptions.waitOptions(last)).perform();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}
	//长按页面上的某一个元素
	public void appiumHoldEl(String xpath,String time) {
		try {
			int t = Integer.parseInt(time);
			Duration last = Duration.ofSeconds(t);
			TouchAction action = new TouchAction(driver);
			//定位到一个元素，并且在该元素上长按指定的时长
			action.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(driver.findElementByXPath(xpath))).withDuration(last)).waitAction(WaitOptions.waitOptions(last)).perform();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
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
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}
	
	/**
	 * 实现隐式等待的方法
	 */
	public void implicitlyWait() {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}

	public void printContexts() {
		Set<String> contexts=driver.getContextHandles();
		for(String s:contexts) {
			System.out.println(s);
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
	
	//定位页面元素
	public WebElement findele(String path) {
		WebElement ele = NULL;
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
			// TODO Auto-generated catch block
			AutoLogger.log.error(e);
		}
		return ele;
		
	}
	/**
	 * 重置输入法为百度输入法
	 */
	public void restToBaiduInput() {
		KeywordOfApp appkw= new KeywordOfApp();
		appkw.StartAppium("4725", "5");
		appkw.runAPP("8.0.0", "KWG5T16C29081222",
				"com.android.settings", ".HWSettings", "http://127.0.0.1:4725/wd/hub", "5");
		appkw.adbSwipe(500, 1600, 500, 400);
		appkw.click("//*[@text='系统导航、系统更新、关于手机、语言和输入法']");
		appkw.click("//*[@text='语言和输入法']");
		appkw.click("//*[@text='默认']");
		appkw.click("//*[@text='百度输入法华为版']");
		appkw.quitApp();
//		appkw.killAppium();
	}
}
