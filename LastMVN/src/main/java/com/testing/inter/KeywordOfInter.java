package com.testing.inter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.testing.common.AutoLogger;

public class KeywordOfInter {
	//httpclientkw对象，方便所有的关键字方法，调用httpclientkw封装好的方法
	public HttpClientKw client;
	//存储参数所用的map
	public Map<String, String> paramMap;
	//每次发包之后的返回结果,在下一次调用请求方法之前都不会发生变化，但是会从中进行一些提取。
	public String tmpResponse;
	
	public KeywordOfInter() {
		client=new HttpClientKw();
		paramMap=new HashMap<String,String>();
	}
	
	public String testGet(String url,String input) {
		try {
			//解析参数
			String param=toParam(input);
			tmpResponse =client.doGet(url, param);
			return tmpResponse;
		} catch (Exception e) {
			AutoLogger.log.error("get方法发送失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			return tmpResponse;
		}
		
	}
	
	public String testPost(String url,String input) {
		try {
			url=toParam(url);
			String param=toParam(input);
//			System.out.println("param:"+param);
			tmpResponse =client.doPost(url, param);
			return tmpResponse;
		} catch (Exception e) {
			AutoLogger.log.error("post方法发送失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			return tmpResponse;
		}
		
	}
	
	public String testPostJson(String url,String jsonInput) {
		try {
			String param=toParam(jsonInput);
			param=URLEncoder.encode(param, "utf-8");
//			System.out.println("param:"+param);
			tmpResponse =client.doPostJson(url, param);
			return tmpResponse;
		} catch (Exception e) {
			AutoLogger.log.error("post方法发送失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			return tmpResponse;
		}
	}
	
	public String testPostSoap(String url,String xmlInput) {
		try {
			String param=toParam(xmlInput);
			System.out.println("param:"+param);
			tmpResponse =client.doSoap(url, param);
			return tmpResponse;
		} catch (Exception e) {
			AutoLogger.log.error("post方法发送失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			return tmpResponse;
		}
	}
	
	public void saveCookie() {
		client.saveCookie();
	}

	public void clearCookie() {
		AutoLogger.log.info("清空cookie池");
		client.clearCookie();
	}
	
	//传递头域信息，以json格式字符串接收，解析为map格式之后，作为参数传递给httpclientkw的addheader方法使用
		// originJson可以为 "{\"token\":\"{tokenValue}\"}" 这种
	public void addHeader(String originJson) {
		System.out.println(originJson);
		//使用json.jar工具，将json解析为map
		Map<String, String> jsonmap=new HashMap<String, String>();
		//在解析为map之前，先替换{参数名}为参数值
		String headerJson=toParam(originJson);
		try {
			//以json格式的头域列表为基础，创建一个json类型的对象
			JSONObject json = new JSONObject(headerJson);
			//通过迭代器，遍历json对象中的每个键，将键值对添加到map中
			Iterator<String> jsonit = json.keys();
			while (jsonit.hasNext()) {
				String jsonkey = jsonit.next();
				jsonmap.put(jsonkey, json.get(jsonkey).toString());
			}
		} catch (JSONException e) {
			AutoLogger.log.error("头域参数格式错误，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
		//转换出的map作为addheader所使用的map，来进行添加头域的操作。
		client.addHeader(jsonmap);
		
	}
	
	public void clearHeader() {
		client.clearHeader();
	}
	/**
	 * 存储参数到parammap中
	 * @param key 存储的参数的键名
	 * @param jsonPath 从json中通过jsonpath解析出来一个值，作为存储的参数的值
	 */
	public void saveParam(String key,String jsonPath) {
		String value;
		try {
			value = JsonPath.read(tmpResponse,jsonPath).toString();
			paramMap.put(key, value);
		} catch (Exception e) {
			AutoLogger.log.error("保存参数失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}
	
	/**
	 * 传入一个字符串，解析其中的键名，替换为parammap中对应的参数值。
	 * @param origin 传入的字符串。
	 * @return
	 */
	public String toParam(String origin) {
		String param=origin;
		for(String key:paramMap.keySet()) {
		param=param.replaceAll("\\{"+key+"\\}", paramMap.get(key));
		}
		return param;
	}
	
//	public String toParamName(String param) {
//		String paramRes = param;
//		if(paramRes.contains("<") & paramRes.contains(">")) {
//			int randNum = (int)(Math.random()*999999999);
//			param = param.replace("<", "&").replace(">", "&");
//			String[] arr = param.split("&");
//			if (arr.length == 2) {
//				paramRes = arr[0] + arr[1] + String.valueOf(randNum);
//			}
//			else if (arr.length == 3) {
//				paramRes = arr[0] + arr[1] + String.valueOf(randNum) + arr[2];
//			}
//			else {
//				System.out.println("excel参数" + param + "替换失败");
//			}
//			
//		}
//		
//		
//		return paramRes;
//	}
	

	public boolean assertSame(String expect,String jsonPath) {
		boolean success=false;
		try {
			String actual=JsonPath.read(tmpResponse,jsonPath).toString();
			if(actual!=null&&actual.equals(expect)) {
				AutoLogger.log.info("测试通过！");
				success=true;
				return success;
			}
			else {
				AutoLogger.log.info("测试失败！");
				success=false;
				return success;
			}
		} catch (Exception e) {
			AutoLogger.log.error("解析失败，请检查jsonPath表达式");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
		return success;
	}
	

	
	public void assertContains(String expect,String jsonPath) {
		try {
			String actual=JsonPath.read(tmpResponse,jsonPath).toString();
			if(actual!=null&&actual.contains(expect)) {
				AutoLogger.log.info("测试通过！");
			}
			else {
				AutoLogger.log.info("测试失败！");
			}
		} catch (Exception e) {
			AutoLogger.log.error("解析失败，请检查jsonPath表达式");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}
	
	public static void main(String[] args) {
		// 遇到<>的
		String a="aaa<ddddd>aa";
		String b="<abdawrer>";
		KeywordOfInter aa = new KeywordOfInter();
//		System.out.println(aa.toParamName(a));
//		System.out.println(aa.toParamName(b));
	}
}
