package com.testing.UI;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class IEDriver { // IE浏览器驱动类
	public WebDriver driver;
	//webdriver连接启动浏览器时，启动的服务。
	public InternetExplorerDriverService service = null;

	public IEDriver(String driverpath) {
		// 设置 IE 的路径
		System.setProperty("webdriver.ie.driver", driverpath);
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		//设置忽略安全校验
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		// 创建一个 IEDriver 的服务，用于连接 IE	
		try {
			service = new InternetExplorerDriverService.Builder().usingDriverExecutable(new File(driverpath))
					.usingAnyFreePort().build();
			service.start();
			// service.stop();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("log--error：service启动错误！");
		}
		
		try {
			// 创建一个 IE 的浏览器实例
			this.driver = new RemoteWebDriver(service.getUrl(), ieCapabilities);
			// 让浏览器访问空白页面
			driver.get("about:blank");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("log--error：创建driver失败！！");
		}
	}

	//将在构造函数中实例化的成员变量driver对象通过该方法返回
	public WebDriver getdriver() {
		return this.driver;
	}
	
	public void close() {
		driver.quit();
		service.stop();
	}
}