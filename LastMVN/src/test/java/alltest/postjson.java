package alltest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.ws.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

public class postjson {
	public static void main(String[] args) throws ClientProtocolException, IOException {

	//1.创建客户端
//	HttpClient client = HttpClients.createDefault();
	
	//1.使用cookiestore
	CookieStore cookiestor = new BasicCookieStore();
	HttpClient client  = HttpClients.custom().setDefaultCookieStore(cookiestor).build();//使用自定义的cookiestore
	
	
	//2.创建httppost方法，传入url
	HttpPost loginPost = new HttpPost("http://test-cc.chintcloud.net/api/auth/login");
	
	//3，创建一个StringEntity实体用于记录传递的参数内容，封装成为一个实体，这个接口这里没有，所以不需要
	StringEntity jsonParam = new StringEntity("{\"username\":\"common-api@chint.com\",\"password\":\"common-api\"}");
	
	//4、将字符串实体的内容设置，这个接口这里也没有，故不需要
	jsonParam.setContentType("application/json;charset=UTF-8");
	jsonParam.setContentEncoding("UTF-8");
	
	//5、设置psot方法中发送的主体内容
	loginPost.setEntity(jsonParam); //因为没有要携带的参数，这也不需要
	HttpResponse res = client.execute(loginPost);
	
	
	
	//6、类似返回的是一个包裹，下面一步是打开包裹，获取包裹里的内容
	String jsonResult = EntityUtils.toString(res.getEntity());
	System.out.println("首次访问接口："+jsonResult);
	
	//7、提取token
	String tokenValue = (String) JSONPath.read(jsonResult, "$.token");//提取token


	

	
}
}