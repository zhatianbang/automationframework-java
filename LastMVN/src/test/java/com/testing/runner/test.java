package com.testing.runner;

import java.util.HashMap;
import java.util.Map;

public class test {
	public static Map<String, String> headersMap = new HashMap<String, String>();

	//生成随机数
	public static String random() {
		int max=10000,min=1;
		int ran2 = (int) (Math.random()*(max-min)+min); 
//		System.out.println(ran2);
		return String.valueOf(ran2);
		}
	
	//用随机数替换指定字符
	public static String replaceInput(String text) {
		String regex = ".*useRandom.*";
		String randomValue =random();
		String result = text;
		if(text.matches(regex)) {
				//替换掉需要随机数的部分
				result = text.replace("useRandom", randomValue);
				//把text当作键，把text替换后的值当作值
				headersMap.put(text, result);
			}
		return result;
		}
	
	//用于替换掉xpath中含有的引用变量
	public static String replaceXpath(String xpath) {
			String resultXpath = xpath;
			if(xpath.matches(".*\\{.*\\}.*")) {
				for(String key:headersMap.keySet()) {
					 if(xpath.contains(key)) {
					 resultXpath = xpath.replace("{" + key + "}", headersMap.get(key));
			}
			}
				
			}else {
				System.out.println("没进入第一个if");
			}
			return resultXpath;
	}
	
	public static void main(String[] args) {
		String sa = "我是租户useRandom";
		String sb = replaceInput(sa);
		System.out.println(headersMap);
		String sc = "//*[contains(text(),'{我是租户useRandom}')]/ancestor::md-card[@role='button']/descendant::div[@aria-label='管理员']";
		String sd = replaceXpath(sc);
		System.out.println("sd:"+ sd);
	}

}
