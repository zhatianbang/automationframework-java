package com.testing.runner;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.testing.UI.KeywordOfWeb;

public class login{
	
	public static void loginPage(KeywordOfWeb driver) {
		KeywordOfWeb kw = driver;
		kw.openBrowser("chrome");
		// 隐式等待  等待网页加载完成
		kw.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
//		System.setProperty("webdriver.chrome.driver", "tools/chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		kw.driver.get("http://test-cc.chintcloud.net/#/login");
		kw.driver.manage().window().maximize();
		kw.driver.findElement(By.xpath("//input[@id='username-input']")).sendKeys("common-api@chint.com");
		kw.driver.findElement(By.xpath("//input[@id='password-input']")).sendKeys("common-api");
		kw.driver.findElement(By.xpath("//*[@class='md-raised md-button md-tb-dark-theme md-ink-ripple']")).click();
	}
	
	public static void main(String[] args) {
		KeywordOfWeb kw = new KeywordOfWeb();
		kw.openBrowser("chrome");
		// 隐式等待  等待网页加载完成
		kw.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//		System.setProperty("webdriver.chrome.driver", "tools/chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		kw.driver.get("http://test-cc.chintcloud.net/#/login");
		kw.driver.manage().window().maximize();
		kw.driver.findElement(By.xpath("//input[@id='username-input']")).sendKeys("common-api@chint.com");
		kw.driver.findElement(By.xpath("//input[@id='password-input']")).sendKeys("common-api");
//		kw.driver.findElement(By.xpath("//*[@class='md-raised md-button md-tb-dark-theme md-ink-ripple']")).click();
		kw.click("//*[@class='md-raised md-button md-tb-dark-theme md-ink-ripple']");
	}
	
	
}
