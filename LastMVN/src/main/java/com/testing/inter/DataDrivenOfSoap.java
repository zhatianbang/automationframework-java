package com.testing.inter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axis2.context.NamedValue;
import org.json.JSONException;
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;

public class DataDrivenOfSoap {
	public SoapInterKeyWord client;
	public Map<String, String> paramMap;
	//加入成员变量，方便在每一行用例调用时，统一操作的行数、返回结果的断言、excel的写入。
	public String tmpResponse;
	public int line = 0; // 成员变量行数，用于在用例执行时保持执行行和写入行一致
	public ExcelWriter outExcel;
	
	public DataDrivenOfSoap(String casePath,String resultPath) {
		client=new SoapInterKeyWord();
		paramMap=new HashMap<String,String>();
		outExcel =new ExcelWriter(casePath, resultPath);
	}
	
	public DataDrivenOfSoap(ExcelWriter excel) {
		client=new SoapInterKeyWord();
		paramMap=new HashMap<String,String>();
		outExcel =excel;
	}
	
	public void createCon(String wsdlUrl) {
		try {
			client.createCon(wsdlUrl);
			outExcel.writeCell(line, 10, "PASS");
			AutoLogger.log.info("基于wsdl文档地址建立连接");
		} catch (Exception e) {
			outExcel.writeFailCell(line, 10, "FAIL");
			AutoLogger.log.error("连接建立失败，请检查wsdl文档");
			AutoLogger.log.error(e,e.fillInStackTrace());
		}
	}
	
	public String testSoap(String targetNS,String interName,String paramString ) {
		String response=null;
		try {
			String realParam=toParam(paramString);
			response= client.doSoap(targetNS, interName, realParam);
			tmpResponse=response;
			outExcel.writeCell(line, 11, tmpResponse);
			return response;
		} catch (Exception e) {
			AutoLogger.log.error("post方法发送失败，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			outExcel.writeFailCell(line, 10, "FAIL");
			outExcel.writeFailCell(line, 11, "get方法发送失败，请检查log");
			return response;
		}
	}
	
	public void addHeader(String originJson) {
		List<NamedValue> headers=new ArrayList<NamedValue>();
		String headerJson=toParam(originJson);
		try {
			JSONObject json = new JSONObject(headerJson);
			Iterator<String> jsonit = json.keys();
			while (jsonit.hasNext()) {
				String jsonkey = jsonit.next();
				NamedValue h = new NamedValue(jsonkey, json.get(jsonkey).toString());
				headers.add(h);
			}
			outExcel.writeCell(line, 10, "PASS");
		} catch (JSONException e) {
			AutoLogger.log.error("头域参数格式错误，请检查");
			AutoLogger.log.error(e,e.fillInStackTrace());
			outExcel.writeFailCell(line, 10, "FAIL");
		}
		client.addHeader(headers);
	}
	
	public void clearHeader() {
		try {
			client.clearHeader();
			outExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			outExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	public void saveParam(String key,String jsonPath) {
		String value;
		try {
			value = JsonPath.read(tmpResponse,jsonPath).toString();
			paramMap.put(key, value);
			outExcel.writeCell(line, 10, "PASS");
		} catch (Exception e) {
			AutoLogger.log.error("保存参数失败");
			AutoLogger.log.error(e,e.fillInStackTrace());
			outExcel.writeCell(line, 10, "FAIL");
		}
	}
	
	public String toParam(String origin) {
		String param=origin;
		for(String key:paramMap.keySet()) {
		param=param.replaceAll("\\{"+key+"\\}", paramMap.get(key));
		}
		return param;
	}

	public void assertSame(String jsonPath,String expect) {
		try {
			String actual=JsonPath.read(tmpResponse,jsonPath).toString();
			if(actual!=null&&actual.equals(expect)) {
				AutoLogger.log.info("测试通过！");
				outExcel.writeCell(line, 10, "PASS");
			}
			else {
				AutoLogger.log.info("测试失败！");
				outExcel.writeFailCell(line, 10, "FAIL");
			}
		} catch (Exception e) {
			AutoLogger.log.error("解析失败，请检查jsonPath表达式");
			AutoLogger.log.error(e,e.fillInStackTrace());
			outExcel.writeFailCell(line, 10, "FAIL");
		}
	}
	
	public void assertContains(String jsonPath,String expect) {
		try {
			String actual=JsonPath.read(tmpResponse,jsonPath).toString();
			if(actual!=null&&actual.contains(expect)) {
				AutoLogger.log.info("测试通过！");
				outExcel.writeCell(line, 10, "PASS");
			}
			else {
				AutoLogger.log.info("测试失败！");
				outExcel.writeFailCell(line, 10, "FAIL");
			}
		} catch (Exception e) {
			AutoLogger.log.error("解析失败，请检查jsonPath表达式");
			AutoLogger.log.error(e,e.fillInStackTrace());
			outExcel.writeFailCell(line, 10, "FAIL");
		}
	}
}
