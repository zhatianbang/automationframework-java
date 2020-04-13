package com.testing.UI;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.testing.common.ExcelWriter;

public class DataDrivenOfWeb {
	// 成员变量webdrvier对象driver，用这个成员变量操作后续的所有方法。
	public WebDriver driver = null;
	public ConditionWait cw = new ConditionWait();
	
	//用户存放需要替换的参数
	public static Map<String, String> headersMap = new HashMap<String, String>();
	
	
	// 声明写excel对象。
	public ExcelWriter webExcel = null;
	// 当前行数的成员变量
	public int line = 0;
	// 默认使用第一个sheet页，这里再次定义只是为了供后面传递
	public int SheetNo = 0;
	
	public DataDrivenOfWeb(ExcelWriter excel,int sheetNo) {
		webExcel = excel;
		webExcel.useSheetByIndex(sheetNo);
	}

	/**
	 * 启动浏览器的方法
	 * 
	 * @param浏览器的类型：可以是IE/FF/chrome
	 */
	public String openBrowser(String browserType) {
		try {
			switch (browserType) {
			case "chrome":
				GoogleDriver gg = new GoogleDriver("tools/chromedriver.exe");
				driver = gg.getdriver();
				AutoLogger.log.info("chrome浏览器启动成功");
				break;
			case "firefox":
//				FireFox可以指定安装路径，所以这里这里写上
				FFDriver ff = new FFDriver("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe", "tools/geckodriver.exe");
				driver = ff.getdriver();
				break;
			case "ie":
				IEDriver IE = new IEDriver("tools/IEDriver.exe");
				driver = IE.getdriver();
				break;
			default:
				GoogleDriver google = new GoogleDriver("tools/chromedriver.exe");
				driver = google.getdriver();
			}
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			webExcel.writeFailCell(line, 10, "FAIL");
			AutoLogger.log.error(e, e.fillInStackTrace());
			return "fail";
		}

	}

	/**
	 * 调整浏览器窗口大小
	 */
	public String setWindow() {
		try {
			Point p = new Point(320, 0);
			Dimension d = new Dimension(1400, 1000);
			driver.manage().window().setPosition(p);
			driver.manage().window().setSize(d);
			AutoLogger.log.info("调整浏览器窗口大小为1400*1000");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
	/**
	 * 调整浏览器窗口最大化
	 */
	public void setMaxiMize() {
		try {
			driver.manage().window().maximize();
			AutoLogger.log.info("窗口最大化");
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			webExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	/**
	 * 访问URL的方法
	 * @param参数为网页URL
	 */
	public String visitWeb(String URL) {
		try {
			setMaxiMize();  //窗口最大化
			implicitlyWait(); //显示等待
			driver.get(URL);
			AutoLogger.log.info("访问" + URL);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			webExcel.writeFailCell(line, 10, "FAIL");
			AutoLogger.log.error(e, e.fillInStackTrace());
			return "fail";
		}
	}

	/**
	 * 定位输入框,清除后输入内容
	 * @param 定位的表达式以及输入的字符串
	 */
	public String input(String xpath, String Text) {
		try {
			String content = replaceInput(Text);
			String Xpath = replaceXpath(xpath);
			//等待元素可点击
			cw.waitElementToBeClickable(driver,Xpath);
			WebElement element = driver.findElement(By.xpath(Xpath));
			element.clear();
			element.sendKeys(content);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("input");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 清除占位符，循环输入字符串中的字符
	 * @param 定位的表达式以及输入的字符串
	 */
	public String inputCyclic(String xpath, String Text) {
		try {
			String content = replaceInput(Text);
			String Xpath = replaceXpath(xpath);
			//等待元素可点击
			cw.waitElementToBeClickable(driver,Xpath);
			WebElement element = driver.findElement(By.xpath(Xpath));
			element.clear();
	        for (int i = 0; i < content.length(); i++) {
	            char item = content.charAt(i);
	            element.sendKeys(String.valueOf(item));
	        }
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("inputCyclic");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
	/**
	 * 定位元素并点击
	 * @param 定位的表达式
	 */
	public String click(String xpath) {
		try {
			String resultXpath = replaceXpath(xpath);
			cw.waitElementToBeClickable(driver, resultXpath);
			WebElement element = driver.findElement(By.xpath(resultXpath));
			element.click();
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("click");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
	/**
	 * type="file"类型的文件上传
	 * @param xpath及文件路径+文件名
	 */
	public String upload(String xpath, String filePath) {
		try {
			explicitlyWait(xpath);
			WebElement element = driver.findElement(By.xpath(xpath));
			element.sendKeys(filePath);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("上传文件");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
		
	}
	
	/**
	 * 使用actions类，调用moveElement方法实现悬停。
	 * 
	 * @param xpath
	 */
	public String hover(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			Actions act = new Actions(driver);
			act.moveToElement(element).build().perform();
			AutoLogger.log.info("悬停到指定元素");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.info("元素悬停失败！");
			saveScrShot("hover");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 实现显式等待的方法，在每次定位元素时，先尝试找元素，给10秒钟的最长等待。
	 */
	public void explicitlyWait(final String xpath) {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String resultXpath = replaceXpath(xpath);
			WebDriverWait ewait = new WebDriverWait(driver, 10);
			// 设置等待的预期条件为，元素可以被定位到。
			ewait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return d.findElement(By.xpath(resultXpath));
				}
			});
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	/**
	 * 实现获取页面标题的方法
	 */
	public String getTitle() {
		String title = "";
		try {
			title = driver.getTitle();
			return title;
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.info("获取页面标题失败！");
			return null;
		}

	}

	/**
	 * 实现关闭浏览器的方法
	 */
	public String closeBrowser() {
		try {
			driver.quit();
			driver = null;
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			webExcel.writeFailCell(line, 10, "FAIL");
			AutoLogger.log.error(e, e.fillInStackTrace());
			return "fail";
		}
	}

	/**
	 * 实现隐式等待的方法
	 */
	public void implicitlyWait() {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//			webExcel.writeCell(line, 10, "PASS");
//			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
//			webExcel.writeFailCell(line, 10, "FAIL");
//			return "fail";
		}
	}

	/**
	 * 强制等待的方法
	 * @param t强制等待的秒数，用字符串类型传递
	 */
	public String halt(String t) {
		try {
			int time = Integer.parseInt(t);
			Thread.sleep(time * 1000);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (InterruptedException e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 进入iframe子页面
	 * @param 子iframe定位表达式
	 */
	public String intoIframe(String xpath) {
		try {
			explicitlyWait(xpath);
			WebElement frame = driver.findElement(By.xpath(xpath));
			driver.switchTo().frame(frame);
			AutoLogger.log.info("进入iframe成功！");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.info("进入iframe失败");
			saveScrShot("切入Iframe");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 退出iframe子页面
	 * @param 子iframe定位表达式
	 */
	public String outIframe() {
		try {
			// 切回主页面
			driver.switchTo().defaultContent();
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("切出Iframe失败");
			saveScrShot("切出Iframe");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 获取并打印所有handle
	 */
	public void getAllHandles() {
		Set<String> handles = driver.getWindowHandles();
		for (String i:handles) {
			System.out.println("窗口：" + i);
		}
	}
	
	/**
	 * 通过窗口标题切换窗口
	 * @param 目标窗口handle
	 */
	public String switchWindow(String target) {
		// 创建一个字符串便于之后存放句柄
		String s = null;
		// 获取当前页面中的句柄
		Set<String> handles = driver.getWindowHandles();
		// 循环尝试，找到目标浏览器页面的句柄
		for (String t : handles) {
			// 遍历每一个句柄，判断窗口的标题是否包含预期字符
//					System.out.println(t);
			if (driver.switchTo().window(t).getTitle().equals(target)) {
				s = t;
			}
		}
		// 切换到目标句柄的页面中
		try {
			driver.switchTo().window(s);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.info("通过页面标题切换窗口失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	
	/**
	 * 关闭旧窗口，切换到新窗口操作
	 */
	public String closeOldWin() {
		List<String> handlelist = new ArrayList<String>();
		// 返回一个句柄集合
		Set<String> handles = driver.getWindowHandles();
		// 循环获取集合里面的句柄，保存到List数组handles里面
		Iterator<String> it = handles.iterator();
		while (it.hasNext()) {
			handlelist.add(it.next().toString());
		}
		// 关闭第一个窗口
		driver.close();
		// 切换到新窗口
		try {
			driver.switchTo().window(handlelist.get(1));
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.info("关闭旧窗口切换到新窗口失败");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	// 关闭新窗口
	public String closeNewWin() {
		List<String> handles = new ArrayList<String>();
		Set<String> s = driver.getWindowHandles();
		// 循环获取集合里面的句柄，保存到List数组handles里面
		for (Iterator<String> it = s.iterator(); it.hasNext();) {
			handles.add(it.next().toString());
		}
		try {
			driver.switchTo().window(handles.get(1));
			driver.close();
			driver.switchTo().window(handles.get(0));
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("关闭新窗口失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 获取js运行结果
	 * @param 待执行js脚本
	 */
	public String getJs(String text) {
		String t = "";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			t = js.executeScript("return " + text).toString();
			webExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("JS脚本执行失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
		}
		return t;
	}

	/**
	 * 执行无返回的js脚本
	 * @param 待执行js脚本
	 */
	public String runJs(String text) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			js.executeScript(text);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("JS脚本执行失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
    /**
     * js执行点击操作
     * @param xpath 页面元素定位
     * @return pass或fail
     */
	public String runJsClick(String xpath) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String resultXpath = replaceXpath(xpath);
		try {
			WebElement button = driver.findElement(By.xpath(resultXpath));
			js.executeScript("arguments[0].click();",button);
			webExcel.writeCell(line, 10, "PASS");
			halt("1");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e,e.fillInStackTrace());
			AutoLogger.log.error(xpath + " JS脚本执行点击失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	//****************************************  以上为JS模块  *********************************************

    /**Enter键封装
     * @return */
    public  String pressEnter(String xpath){
    	explicitlyWait(xpath);
    	try {
			WebElement element = driver.findElement(By.xpath(xpath));
			element.sendKeys(Keys.ENTER);
//			AutoLogger.log.info(xpath+" 发送ENTER键成功");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error("xpath：" + xpath+" 发送ENTER键失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
    } 
    
    /**DOWN键封装
     * @return */
    public  String pressDown(String xpath){
    	explicitlyWait(xpath);
    	try {
			WebElement element = driver.findElement(By.xpath(xpath));
			element.sendKeys(Keys.DOWN);
//			AutoLogger.log.info(xpath+" 发送DOWN键成功");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error("xpath：" + xpath+" 发送ENTER键失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
    } 
	
	// 执行浏览器滚动
	public void scrollWindowStraight(String height) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			String jsCmd = "window.scrollTo(0," + height + ")";
			js.executeScript(jsCmd);
			webExcel.writeFailCell(line, 10, "PASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("操作浏览器滚动条失败");
			saveScrShot("scroll");
			webExcel.writeFailCell(line, 10, "FAIL");
		}
	}

	
	/**
	 * 循环点击键盘下，直到元素出现并点击
	 * @param xpath 
	 * @return 
	 */
	public String pressDownKeyClick(String xpath) {
		/* ele：滚动条所在元素位置
		 * Keys.DOWN：点击键盘下键
		 * perform()：确定键盘操作事件，不能省略
		*/
		try {
			Actions actions = new Actions(driver);
			//通过xpath获取到滚动条所在元素位置
			WebElement ele = driver.findElement(By.xpath(xpath));
			//循环点击键盘下键，直到元素可见
			while(true) {
			    actions.sendKeys(ele, Keys.DOWN).perform();
			    break;
			}
			webExcel.writeFailCell(line, 10, "PASS");
			return "pass";
		}
		catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("操作浏览器滚动条失败");
			saveScrShot("scroll");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
    /**
     * 实现select通过文本选择
     * @param xpath 页面元素定位，text select的文本值
     * @return pass或fail
     */
	public String selectByText(String xpath, String text) {
		try {
			// 将webelement转换为select
			Select userSelect = new Select(driver.findElement(By.xpath(xpath)));
			userSelect.selectByVisibleText(text);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("通过文本选择Select失败！");
			saveScrShot("Select");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

    /**
     * 实现select通过value值选择
     * @param xpath 页面元素定位，value select的value值
     * @return pass或fail
     */
	public String selectByValue(String xpath, String value) {
		try {
			// 将webelement转换为select
			Select userSelect = new Select(driver.findElement(By.xpath(xpath)));
			userSelect.selectByValue(value);
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("通过值选择Select失败！");
			saveScrShot("Select");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

    /**
     * 实现报错时截图
     * @param method操作的方法
     * @return pass或fail
     */
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
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("截图失败！");
		}
		
	}

	/**
	 * 断言标题中包含指定内容
	 * 
	 * @param target 标题包含的内容
	 */
	public String assertTitleContains(String target) {
		String result = getTitle();
		if (result.contains(target)) {
			AutoLogger.log.info("测试成功！");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} else {
			AutoLogger.log.info("测试失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 断言标题的内容符合预期
	 * 
	 * @param target 标题内容
	 */
	public String assertTitleIs(String target) {
		String result = getTitle();
		if (result.equals(target)) {
			AutoLogger.log.info("测试成功！");
			webExcel.writeFailCell(line, 10, "PASS");
			return "pass";
		} else {
			AutoLogger.log.info("测试失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}

	/**
	 * 断言页面中某个元素的文本内容是否符合预期
	 * 
	 * @param xpath  元素定位xpath表达式
	 * @param target 预期的内容
	 */
	public String assertContentIs(String xpath, String target) {
		try {
			WebElement ele = driver.findElement(By.xpath(xpath));
			String text = ele.getText();
			if (text.equals(target)) {
				AutoLogger.log.info("测试成功！");
				webExcel.writeCell(line, 10, "PASS");
				return "pass";
			} else {
				AutoLogger.log.info("测试失败！");
				webExcel.writeFailCell(line, 10, "FAIL");
				return "fail";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.info("未找到指定元素！");
			return "fail";
		}
	}

	/**
	 * 断言元素的某个值是否符合预期
	 * 
	 * @param xpath  元素定位xpath表达式
	 * @param attr   元素中的属性
	 * @param target 属性值
	 */
	public String assertAttrIs(String xpath, String attr, String target) {
		try {
			WebElement ele = driver.findElement(By.xpath(xpath));
			String value = ele.getAttribute(attr);
			if (value.equals(target)) {
				AutoLogger.log.info("测试成功！");
				webExcel.writeCell(line, 10, "PASS");
				return "pass";
			} else {
				AutoLogger.log.info("测试失败！");
				webExcel.writeFailCell(line, 10, "FAIL");
				return "fail";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.info("未找到指定元素的指定属性！");
			return "fail";
		}
	}

	/**
	 * 断言页面源码中包含某个内容
	 * 
	 * @param target 页面中预期包含的内容
	 */
	public String assertPageContains(String target) {
		String pageContent = driver.getPageSource();
		if (pageContent.contains(target)) {
			AutoLogger.log.info("测试成功！");
			webExcel.writeCell(line, 10, "PASS");
			return "pass";
		} else {
			AutoLogger.log.info("测试失败！");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	

	/**
	 * 断言页面元素消失，如判断元素是否删除成功
	 * 
	 * @param target 页面中预期包含的内容
	 */
	public String assertInvisibility(String xpath) {
		try {
			List<WebElement> eles = driver.findElements(By.xpath(xpath));
			if (eles.size() == 0) {
				webExcel.writeCell(line, 10, "PASS");
				return "pass";
			}
			else if (new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(xpath))))) {
				webExcel.writeCell(line, 10, "PASS");
				return "pass";
			}
			else{
				webExcel.writeCell(line, 10, "PASS");
				return "faill";
			}
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("input");
			webExcel.writeFailCell(line, 10, "FAIL");
			return "fail";
		}
	}
	
	
	/**
	 * 弹窗：取消弹窗
	 */
	public void closeAlert() {
		try {
			driver.switchTo().alert().dismiss();
			AutoLogger.log.info("取消弹窗成功！");
			webExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.info("取消弹窗失败！");
			AutoLogger.log.error(e,e.fillInStackTrace());
			webExcel.writeFailCell(line, 10, "FAIL");
		}
    }
	
	/**
	 * 弹窗   接受弹窗
	 */
	public void acceptAlert() {
       try {
		driver.switchTo().alert().accept();
		AutoLogger.log.info("接收弹窗成功！");
		webExcel.writeCell(line, 10, "PASS");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		AutoLogger.log.error(e,e.fillInStackTrace());
		AutoLogger.log.info("接受弹窗失败");
		webExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	// 获取弹窗文本	 
	public void getAlertText() {
	         try {
				driver.switchTo().alert().getText();
				AutoLogger.log.info("获取弹窗本成功！");
				webExcel.writeCell(line, 10, "PASS");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				AutoLogger.log.error(e,e.fillInStackTrace());
				AutoLogger.log.info("获取弹窗文本失败");
				webExcel.writeFailCell(line, 10, "FAIL");
				saveScrShot("获取弹窗失败截图");
			}
	    }

	//生成随机数
	public static String random() {
		int max=999999,min=1;
		int ran2 = (int) (Math.random()*(max-min)+min); 
//		System.out.println(ran2);
		return String.valueOf(ran2);
		}
	
	//用随机数替换指定字符
	public static String replaceInput(String text) {
		String result = text;
		if(text.contains("{") && text.contains("}")) {
			return text;
		}
		else {
		String regex = ".*useRandom.*";
		String randomValue =random();
		if(text.matches(regex)) {
				//替换掉需要随机数的部分
				try {
					result = result.replace("useRandom", randomValue);
					//把text当作键，把text替换后的值当作值
					headersMap.put(text, result);
				} catch (Exception e) {
					AutoLogger.log.error(e,e.fillInStackTrace());
				}
			}
		return result;
		}
	}
	
	//用于替换掉xpath中含有的引用变量
	public static String replaceXpath(String xpath) {
			String resultXpath = xpath;
			if(xpath.matches(".*\\{.*\\}.*")) {
				for(String key:headersMap.keySet()) {
					 if(xpath.contains(key)) {
//				      System.out.println("进入xpath替换：" + headersMap);
					 resultXpath = xpath.replace("{" + key + "}", headersMap.get(key));
			}
			}
				
			}
			return resultXpath;
	}
	

	
}

