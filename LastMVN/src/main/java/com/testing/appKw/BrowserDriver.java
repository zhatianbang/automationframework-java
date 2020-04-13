package com.testing.appKw;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.testing.common.AutoLogger;

import io.appium.java_client.android.AndroidDriver;
/**
 * 当需要对纯H5功能进行测试，通过手机原生浏览器访问页面时的启动方式，
 *注意如果要切换context，使用web定位方法，先确认手机的android system webview系统应用的版本号
 *找到appium安装路径（desktop以及命令行）搜索chromedriver.exe替换为webview对应版本的chromedriver，默认存放位置如下：
 *C:\Program Files (x86)\Appium\resources\app\node_modules\appium\node_modules\appium-chromedriver\chromedriver\win
 * 或者通过desiredcapabilities设置chromedriverExcutable参数为对应版本chromedriver的路径
 * @author pc
 *
 */
public class BrowserDriver {
	public AndroidDriver driver = null;

	// 设备名称、app的main Activity类、appium服务器ip端口、等待启动时间
	public BrowserDriver(String platformVersion,String deviceName,  String appiumServerIP,String waitTime) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//必要参数
		capabilities.setCapability("deviceName", deviceName);
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("browserName", "Browser");
		//可选参数
		capabilities.setCapability("noSign", true);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		//电脑连接了多个设备的时候，指定设备。
		capabilities.setCapability("udid", deviceName);
		//设置webview操作所使用的chromedriver所在路径
//		capabilities.setCapability("chromedriverExcutable", "F:\\DnTest\\chromedriver224\\chromedriver.exe");
		
		try {
			driver=new AndroidDriver(new URL(appiumServerIP), capabilities);
			int t=1000;
			t=Integer.parseInt(waitTime);
			Thread.sleep(t*1000);
			AutoLogger.log.info("安卓浏览器正在启动中……");
		} catch (Exception e) {
			AutoLogger.log.error("安卓浏览器启动失败，请检查配置！");
		}
	}

	public AndroidDriver getdriver() {
		return this.driver;
	}
}
