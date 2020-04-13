package appKwTest;

import com.testing.appKw.DataDrivenOfApp;
import com.testing.appKw.KeywordOfApp;

public class testQQ {
	public static void main(String[] args) {
		String s1= "D:\\installation\\eclipse-workspace\\LastMVN\\cases\\result\\123.xlsx";
		String s2 = "D:\\installation\\eclipse-workspace\\LastMVN\\cases\\result\\1233.xlsx";
		KeywordOfApp kw = new KeywordOfApp();
		kw.StartAppium( "4723", "5");
		kw.runAPP("6.0.1", "127.0.0.1:7555", "com.tencent.mobileqq", ".activity.SplashActivity", "http://127.0.0.1:4723/wd/hub", "5");
		kw.wait("3");
		kw.input("//*[@content-desc=\"请输入QQ号码或手机或邮箱\"]", "120517972");
		kw.input("//*[@content-desc=\"密码 安全\"]", "dkjdkj1990qq");
		kw.click("登 录");
		
		kw.click("//*[contains(@text,'看点')]");
		kw.wait("3");
		kw.appiumSwipe("400", "110", "100", "110");
		kw.wait("30");
		kw.appiumSwipe("100", "110", "400", "110");
		
		kw.wait("3");
	
		
		
		
		
		
		
		
		
		
		
		
		kw.click("//*[contains(@text,'联系人')]");
		kw.click("帐户及设置");
		kw.click("设置");
		kw.click("//*[@content-desc=\"帐号管理\"]");
		kw.click("退出当前帐号按钮");
		kw.click("//*[contains(@text,'确认退出')]");
		kw.wait("3");
		kw.quitApp();
		kw.closeAppium();
		
		
		 
		

		
	}

}
