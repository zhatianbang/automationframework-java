package com.testing.UI;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class GoogleDriver { // Chrome浏览器驱动类
	private WebDriver driver = null;

	public GoogleDriver(String driverpath) {
		String chromedriver = "";
		if(driverpath =="") {
			chromedriver = System.getProperty("user.dir") + "\\tools\\chromedriver.exe";
		}
		else {
			chromedriver = driverpath;
		}
		
		// 设置 chrome 的路径
		System.setProperty("webdriver.chrome.driver", chromedriver);
		// 创建chrome浏览器的属性对象。
		ChromeOptions option = new ChromeOptions();
 		
		//静默执行浏览器
		option.addArguments("--headless");
		option.addArguments("--no-sandbox");                //# 解决DevToolsActivePort文件不存在的报错
		option.addArguments("window-size=1920x1080");      // 设置浏览器分辨率
		option.addArguments("--disable-gpu");               // 谷歌文档提到需要加上这个属性来规避bug
		option.addArguments("--hide-scrollbars") ;          // 隐藏滚动条，应对一些特殊页面
		option.addArguments("blink-settings=imagesEnabled=false");      // 不加载图片，提升运行速度
		
		// 去除Chrome浏览器上的黄色警告
//		option.addArguments("--disable-infobars");

//		加载chrome用户文件
//		option.addArguments("--user-data-dir=C:\\Users\\pc\\AppData\\Local\\Google\\Chrome\\User Data");
		// 最大化浏览器窗口
		option.addArguments("--start-maximized");
		// 白名单设置
//		option.addArguments("--whitelisted-ips=\"\"");

		// 创建一个 ChromeDriver 的接口，用于连接 Chrome
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability(ChromeOptions.CAPABILITY, option);
//		capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
//		capabilities.setCapability(CapabilityType.VERSION, "");
//		capabilities.setCapability(CapabilityType.PLATFORM, "WINDOWS");

		try { // 创建一个 Chrome 的浏览器实例
			this.driver = new ChromeDriver(option);
			// 让浏览器访问空白页
			driver.get("about:blank");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("log--error：创建driver失败！！");
		}

	}

	public WebDriver getdriver() {
		return this.driver;
	}
}