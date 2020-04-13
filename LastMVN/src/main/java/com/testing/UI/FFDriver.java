package com.testing.UI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

//Firefox浏览器驱动类
public class FFDriver {
	private WebDriver driver = null;

	public FFDriver(String propath, String driverpath) {
		String firefoxDriver = "";
		if(driverpath == "") {
			firefoxDriver = System.getProperty("user.dir") + "\\tools\\geckodriver.exe";
		}
		else {
			firefoxDriver = driverpath; 
		}
		// 设置 Firefox驱动的路径
		System.setProperty("webdriver.gecko.driver", firefoxDriver);
		// 设置Firefox的安装目录
		if (propath != null && propath.length() > 0)
		System.setProperty("webdriver.firefox.bin", propath);

		FirefoxOptions firefoxOptions = new FirefoxOptions() {
			FirefoxProfile profile = new FirefoxProfile();
		};

		// 创建一个 Firefox的浏览器实例
		try {
			driver = new FirefoxDriver(firefoxOptions);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("log--error：创建driver失败！！");
		}

	}

	public WebDriver getdriver() {
		return this.driver;
	}
}