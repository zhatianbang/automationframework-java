package com.testing.inter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.NamedValue;
import org.apache.axis2.transport.http.HTTPConstants;
import org.json.JSONObject;

import com.testing.common.AutoLogger;

public class SoapInterKeyWord {
	//headers，用axis2类中的键值对的list进行存放
	private List<NamedValue> headers;
	//充当webservice的客户端
	private ServiceClient client;
	//client的设置
	private Options opts;
	
	/**
	 * 创建webservice的客户端，建立和wsdl的链接
	 * @param wsdlUrl wsdl文档所在的URL
	 */
	public void createCon(String wsdlUrl) {
		
		try {
			client = new ServiceClient();
			opts = new Options();
			//连接的地址
			EndpointReference end = new EndpointReference(wsdlUrl);
			//将请求的wsdl地址设置为参数
			opts.setTo(end);
			//client调用参数。
			client.setOptions(opts);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 基于创建的客户端client，通过调用想要使用的接口，在其中加入参数列表中指定的参数，拼接成为XML的请求，
	 * 之后通过client完成请求的发送，并最终获得返回值结果。
	 * @param targetNS 在wsdl文档中获取的targetNamespace元素的值
	 * @param interName 接口名称（比如auth、login、register）
	 * @param paramString 以Json格式传递的参数列表
	 * @return
	 * @throws Exception
	 */
	public String doSoap(String targetNS, String interName, String paramString) throws Exception {
		try {
			//创建方法调用对应的接口
			OMElement method;
			//trycatch分别适应命名空间为TNS和为空的SOAP接口的情况。
			try {
				//使用工厂模式创建对象
				OMFactory fac = OMAbstractFactory.getOMFactory();
				//创建命名空间，有些以tns开头，有些为空，所以用到try catch
				OMNamespace omNs = fac.createOMNamespace(targetNS, "tns");
				//创建方法，即通过命名空间调用接口。
				method = fac.createOMElement(interName, omNs);
				// 通过遍历参数列表，使用Qname类添加参数
				if (paramString.length() > 0) {
					//将json格式的参数列表字符串转换为json格式
					JSONObject paramJson = new JSONObject(paramString);
					//遍历json格式中的每个键值对，将其作为子元素添加到接口方法中。
					for (Iterator<String> it = paramJson.keys(); it.hasNext();) {
						String key = it.next();
						//以键名创建QNAME，相当于新建了一个XML标签
						QName param = new QName(key);
						//在XML请求中加入这个标签
						OMElement value = fac.createOMElement(param);
						//设置标签的值
						value.setText(paramJson.get(key).toString());
						//将标签添加作为方法的子孙元素
						method.addChild(value);
					}
					System.out.println(method);
				}
			} 
			//如果使用命名空间tns组合发包失败，则尝试使用空的命名空间发包
			catch (Exception e) {
				OMFactory fac = OMAbstractFactory.getOMFactory();
				OMNamespace omNs = fac.createOMNamespace(targetNS, "");
				method = fac.createOMElement(interName, omNs);
				// 通过遍历参数列表，使用Qname类添加参数
				if (paramString.length() > 0) {
					JSONObject paramJson = new JSONObject(paramString);
					for (Iterator<String> it = paramJson.keys(); it.hasNext();) {
						String key = it.next();
						QName param = new QName(key);
						OMElement value = fac.createOMElement(param);
						value.setText(paramJson.get(key).toString());
						method.addChild(value);
					}
					System.out.println(method);
				}
			}
			// 通过serviceClient发送请求
			OMElement res = client.sendReceive(method);
			// 获取返回结果
			String response = res.getFirstElement().getText();
			System.out.println(response);
			return response;
		} catch (AxisFault e) {
			AutoLogger.log.error("SOAP请求发送失败，请检查！");
			AutoLogger.log.error(e, e.fillInStackTrace());
			return null;
		}
	}

	public void addHeader(List<NamedValue> headerList) {
		headers = headerList;
		opts.setProperty(HTTPConstants.HTTP_HEADERS, headers);
		client.setOptions(opts);
		System.out.println(opts.getProperties());
	}

	public void clearHeader() {
		headers = new ArrayList<NamedValue>();
		opts.setProperty(HTTPConstants.HTTP_HEADERS, headers);
		client.setOptions(opts);
		System.out.println(opts.getProperties());
	}
}
