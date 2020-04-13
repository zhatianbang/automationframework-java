package com.testing.appKw;

import java.net.URL;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.testing.common.AutoLogger;

import io.appium.java_client.android.AndroidDriver;
/**
 * 该类用于启动腾讯微信进行测试，其中加入了对小程序进行测试时，如果要切换到webview来进行测试，所需要的chrome浏览器内核设置
 * @author roy
 *
 */
public class MMDriver {
	public AndroidDriver driver = null;

	// 设备名称、app的main Activity类、appium服务器ip端口、等待启动时间
	public MMDriver(String platformVersion,String deviceName, String appPackage, String appActivity, String appiumServerIP,String waitTime) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//必要参数
		capabilities.setCapability("deviceName", deviceName);
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity", appActivity);
		capabilities.setCapability("platformName", "Android");
		//可选参数
		capabilities.setCapability("noSign", true);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		//电脑连接了多个设备的时候，指定设备。
		capabilities.setCapability("udid", deviceName);
		//通过chromeoption指定微信小程序进程，从而能够切换到context中，如果单纯使用appium进行元素定位操作，下面这一大段都可以不要。
		ChromeOptions option=new ChromeOptions();
		option.setExperimentalOption("androidProcess", "com.tencent.mm:appbrand0");
		//将chromeoption的配置加载到appium启动参数中
		capabilities.setCapability(ChromeOptions.CAPABILITY, option);
		//由于设定了chromeoption，必须加上这一行代码来指定不启动浏览器，否则appium将会报错。
		capabilities.setCapability("browserName", "");
		capabilities.setCapability("recreateChromeDriverSessions", true);
		try {
			driver=new AndroidDriver(new URL(appiumServerIP), capabilities);
			int t=1000;
			t=Integer.parseInt(waitTime);
			Thread.sleep(t*1000);
			AutoLogger.log.info("APP正在启动中……");
		} catch (Exception e) {
			AutoLogger.log.error("APP启动失败，请检查配置！");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}

	public AndroidDriver getdriver() {
		return this.driver;
	}
}
