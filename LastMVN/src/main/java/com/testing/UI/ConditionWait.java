package com.testing.UI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import com.testing.UI.KeywordOfWeb;

public class ConditionWait {
	/**
	 *	作者：Ronnie
	 * 	说明：封装的均是各种条件的等待，等待超时后即报错
	 * 	2018年9月18日
	 */
	
	public  int timeout = 10;
	
    /**
     * 精确断言页面title是否相等，参数为页面title,如果timeout内断言失败，直接报错
     * @param driver
     * @param xpath
     */
	public  void assertTitleIs(WebDriver driver,String titleTxt) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.titleIs(titleTxt));
		} catch (Exception e) {
			System.out.println("断言title等于【"+titleTxt+"】，断言失败：");
			e.printStackTrace();
		}

	}
	
    /**
     * 精确断言页面title是否包含titleTxt，参数为页面title,如果timeout秒内断言结果为false，则报错
     * @param driver
     * @param xpath
     */
	public  void assertTitleContains(WebDriver driver,String titleTxt) {
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.titleContains(titleTxt));
		} catch (Exception e) {
			AutoLogger.log.error("断言title包含【" +titleTxt + "】,断言失败!");
			e.printStackTrace();
		}
        
	}
	
    /**
     * 等待该元素是否被加载在DOM中，并不代表该元素一定可见  ，参数为xpath表达式,如果超时timeout秒则报错
     * @param driver
     * @param xpath
     */
	public  void waitPresenceOfElementLocated(WebDriver driver,String xpath) {
        try {
        	Thread.sleep(1000);
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println(" 等待该元素:【 "+ xpath + " 】被加载在DOM中（并不代表该元素一定可见 ），等待超时！ ");
			e.printStackTrace();
		}
        
	}	

    /**
     *等到已定位到的一个元素从DOM中移除，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitStalenessOf(WebDriver driver,String xpath) {
        try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(xpath))));
		} catch (Exception e) {
			System.out.println(" 等待该元素:【 "+ xpath + " 】从DOM中移除，等待超时！ ");
			e.printStackTrace();
		}
        
	}	

    /**
     *等待某个元素不存在于DOM或不可见，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitInvisibilityOfElementLocated(WebDriver driver,String xpath) {
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println(" 等待该元素:【 "+ xpath + " 】不存在于DOM或不可见，等待超时！ ");
			e.printStackTrace();
		}
        
	}	
	
    /**
     *等待元素可点击（可见并且是enable）  ，参数为xpath表达式,如果超时timeout秒则报错
     * @param driver
     * @param xpath
     * @throws InterruptedException 
     */
	public  void waitElementToBeClickable(WebDriver driver,String xpath) throws InterruptedException {
		Thread.sleep(1);
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		} catch (Exception e) {
			AutoLogger.log.error(" 等待元素:【 "+ xpath + " 】可点击（可见并且是enable），等待超时！ ");
			AutoLogger.log.error(e, e.fillInStackTrace());
			
		}
        
	}	
	
    /**
     *等待元素可见(元素需要能定位到，可见代表肉眼可见)，参数为xpath表达式,如果超时timeout秒则报错，如果成功返回该元素
     * @param driver
     * @param xpath
     */
	public  void waitVisibilityOf(WebDriver driver,String xpath) {
		// visibilityOf传入的是element，存在且可见，返回element，不存在返回false
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))));
		} catch (Exception e) {
			System.out.println(" 等待该元素:【 "+ xpath + " 】可见（元素需要能定位到），等待超时！ ");
			e.printStackTrace();
		}
        
	}
	
    /**
     *判断元素是否可见（非隐藏，并且元素的宽和高都不等以0），参数为xpath表达式,如果超时timeout秒则报错，如果成功返回该元素
     * @param driver
     * @param xpath
     */
	public  void waitVisibilityOfElementLocated(WebDriver driver,String xpath) {
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println(" 等待该元素:【 "+ xpath + " 】可见（非隐藏，并且元素的宽和高都不等以0），等待超时！ ");
			e.printStackTrace();
		}
        
	}	

    /**
     *等待某个元素被选中，一般用在下拉列表或者单选/多选框，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitElementToBeSelected(WebDriver driver,String xpath) {
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeSelected(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println("等待该元素:【 "+ xpath + " 】被选中，等待超时！ ");
			e.printStackTrace();
		}
        
	}	

	/**
     *等待元素只要有一个存在就可以（代表在dom中，不代表可见），参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitPresenceOfAllElementsLocatedBy(WebDriver driver,String xpath) {
//		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println("等待元素:【 "+ xpath + " 】至少出现1个（代表在dom中，不代表可见），等待超时！ ");
			e.printStackTrace();
		}
        
	}	

	/**
     *等待元素出现（必须所有符合条件的元素都加载出来才通过,不一定可见），参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitpresenceOfElementLocated(WebDriver driver,String xpath) {
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println("等待元素:【 "+ xpath + " 】元素出现（必须所有符合条件的元素都加载出来才通过），等待超时！ ");
			e.printStackTrace();
		}
        
	}	
	
	/**
     *等待元素中的text是否包含预期的字符串，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitTextToBePresentInElementLocated(WebDriver driver,String xpath,String exceptText) {
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath),exceptText));
		} catch (Exception e) {
			System.out.println("等待元素:【"+ xpath + "】的text中包含【" + exceptText +"】，等待超时！ ");
			e.printStackTrace();
		}
        
	}	

	/**
     *等待元素中的value属性中是否包含预期的字符串，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitTextToBePresentInElementValue(WebDriver driver,String xpath,String exceptText) {
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.textToBePresentInElementValue(By.xpath(xpath),exceptText));
		} catch (Exception e) {
			System.out.println("等待元素:【"+ xpath + "】的value属性中包含【" + exceptText +"】，等待超时！ ");
			e.printStackTrace();
		}
        
	}

	/**
     *等待该frame是否可以切换过去，可以就切换过去并返回true，否则返回false，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */
	public  void waitFrameToBeAvailableAndSwitchToIt(WebDriver driver,String xpath) {
		waitPresenceOfElementLocated(driver,xpath);  // 元素被加载到dom树中，这是前提
        try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(xpath)));
		} catch (Exception e) {
			System.out.println("等待切换进frame:【"+ xpath + "】中，等待超时！ ");
			e.printStackTrace();
		}
        
	}
	
	/**
     *等待弹窗出现，可以就切换过去并返回true，否则返回false，参数为xpath表达式,如果超时timeout秒则报错，
     * @param driver
     * @param xpath
     */	
	public  void waitAlertIsPresent(WebDriver driver) {
		new WebDriverWait(driver, timeout).until(ExpectedConditions.alertIsPresent());
	}
	
//	public  void main(String[] args) {
//       	KeywordOfWeb kw = new KeywordOfWeb();
//       	kw.openBrowser("chrome");
//       	WebDriver driver = kw.driver;
//       	//这个html我已保存在笔记中，搜索【ExpectedConditions练习html】
//       	String html ="file:///C:/Users/Administrator/Desktop/%E7%99%BE%E5%BA%A6%E4%B8%80%E4%B8%8B%EF%BC%8C%E4%BD%A0%E5%B0%B1%E7%9F%A5%E9%81%93.html";
//        driver.get(html);
//        driver.manage().window().maximize();
//        ExplicitWait ec = new ExplicitWait();
//        System.out.println(driver.getPageSource());
        
//        ec.asserttitleContains(driver, "百度");	//断言包含
//        ec.assertTitleIs(driver, "百度");	//断言相等
//        ec.waitpresenceOfElementLocated(driver, "//*[@id='kw']");
//        ec.waitelementToBeClickable(driver, "//*[@id='kw1']");
//        

//************等待元素是否被加载到dom树中  ******************************        
//        String xpath1 = "//*[contains(text(),'京公网安备1timeout00002000001号1')]" ;  //不存在dom树中
//        ec.waitpresenceOfElementLocated(driver, xpath1);
//        System.out.println("分割线");
//        String xpath = "//*[contains(text(),'京公网安备1timeout00002000001号')]" ; //存在dom树中  ，这个元素已经被我添加了：style='display: none'
//        ec.waitpresenceOfElementLocated(driver, xpath);
        
//************等待元素可见（肉眼可见），******************************        
//        String xpath = "//*[contains(text(),'京公网安备1timeout00002000001号')]" ; //存在dom树中  ，这个元素已经被我添加了：style='display: none'
//        ec.waitpresenceOfElementLocated(driver, xpath); //验证元素被加载到dom中
//        System.out.println(driver.findElement(By.xpath(xpath)).isDisplayed()); //判断元素是否可见
//        ec.waitvisibilityOf(driver, xpath);//等待可见
//        System.out.println("分割线");

//************等待元素可见（肉眼可见），******************************        
//      String xpath = "//*[contains(text(),'京公网安备1timeout00002000001号')]" ; //存在dom树中  ，这个元素已经被我添加了：style='display: none'
//      ec.waitpresenceOfElementLocated(driver, xpath); //验证元素被加载到dom中
//	    System.out.println(driver.findElement(By.xpath(xpath)).isDisplayed());  //判断元素是否可见
//	    ec.waitvisibilityOf(driver, xpath);//等待可见
//	    System.out.println("分割线");

//************等待元素可点击，******************************           
//        String xpath ="";  //不可点击
//	    System.out.println(driver.findElement(By.xpath(xpath)).isDisplayed());  //判断元素是可见的
//        ec.waitelementToBeClickable(driver, xpath);
//        System.out.println("1");
//        
//        String xpath1 = "//*[@id='su']" ;	//可点击
//        ec.waitelementToBeClickable(driver, xpath1);


//************等到一个元素从DOM中移除******************************        
//      String xpath ="//*[contains(text(),'把百度设为主页')]";  //这个元素存在
//      ec.waitstalenessOf(driver, xpath);   //从dom树中移除这个元素，就执行结束，否则直到超时
      
      
//************等到一个元素从DOM中移除******************************        
//        String xpath ="//*[@name='rn']";  //这个元素不可见
//      ec.waitpresenceOfElementLocated(driver, xpath); //验证元素被加载到dom中
//      	System.out.println("分割线");
//      ec.waitinvisibilityOfElementLocated(driver, xpath);//判断某个元素不存在于DOM或不可见

//************判断元素是否被选中******************************        
//      String xpath ="//*[@value='男生']";  //这个元素不可见
//      ec.waitVisibilityOf(driver, xpath); //验证元素被加载到dom中
//      System.out.println("分割线");
//      ec.waitElementToBeSelected(driver, xpath);//元素未被选中，所以分割线2一直未被打印
//      System.out.println("分割线2");
//      kw.driver.findElement(By.xpath(xpath)).click();
//      ec.waitElementToBeSelected(driver, xpath); //由于元素已经被选中，分割线3在 分割线2 打印后立刻打印了
//      System.out.println("分割线3");

        
//        driver.quit();


//        //判断某个元素的选中状态是否符合预期
//        ExpectedConditions.elementSelectionStateToBe(By.xpath("//*[@id='kw']"), true);
        
//        //判断某个元素(已定位)的选中状态是否符合预期
//        ExpectedConditions.elementSelectionStateToBe(driver.findElement(By.xpath("//*[@id='kw']")), false);

//        //--------------------自定义判断条件-----------------------------
//        WebDriverWait wait = new WebDriverWait(driver, 3);
//        wait.until(new ExpectedCondition<Boolean>() {
//             public Boolean apply(WebDriver driver) {
//                 return !driver.findElement(By.xpath("//*[@id='kw']")).getAttribute("class").contains("x-form-invalid-field");
//                             }
//                 });
//        
//    }
//


}




