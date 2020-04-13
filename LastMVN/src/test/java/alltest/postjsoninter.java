package alltest;

import com.testing.inter.DataDrivenOfInter;

public class postjsoninter {
	
	public static void main(String[] args) {
		
		String a ="D:\\installation\\eclipse-workspace\\LastMVN\\cases\\Interbasic.xlsx";
		String b ="D:\\installation\\eclipse-workspace\\LastMVN\\cases\\result\\result-20200225-15-49-55Interbasic.xlsx";
		DataDrivenOfInter http = new DataDrivenOfInter(a, b);
		String input = "{\"username\":\"common-api@chint.com\",\"password\":\"common-api\"}";
		String  url ="http://test-cc.chintcloud.net/api/auth/login";
		String res = http.postJson(url, input);
		System.out.println(res);
		
		
//		http.saveParam(key, jsonPath);
		
	}

}
