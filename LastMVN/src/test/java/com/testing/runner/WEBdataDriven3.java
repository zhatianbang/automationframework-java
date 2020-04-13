package com.testing.runner;

import org.testng.annotations.Test;
import com.testing.UI.DataDrivenOfWeb;
import com.testing.common.report;
import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import static org.testng.Assert.assertEquals;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.annotations.AfterSuite;

/** testng运行
 *  1、beforeSuite获取根目录，获取用例文件和生成结果用例（编号用例），实例化DataDrivenOfWeb
 *  2、@Test读取分组用例，循环遍历sheet页（使用的是分组用例），
 *  3、生成结果用例，
 *  4、exeCase循环遍历结果用例文件中所有的sheet页，每个用例行作为一个list传递给对应的执行方法（执行方法利用反射或者用switch方法执行用例），
 *  5、读取结果用例发送报告
 */
public class WEBdataDriven3 {
	public DataDrivenOfWeb web;
	public ExcelReader caseExcel;
	public ExcelWriter resultExcel;
	public String createdate;
	public String rootpath;
	
//通过数据指定的关键字运行，并且断言
  @Test(dataProvider = "keywords")
  public void f(String rowNo, String kong,String casename,String keywords,String param1,String param2,String param3,String k1,String k2,String k3,String k4 ) {
	  //读取excel返回的内容中的行数，作为当前正在操作的行
	  int No=0;
	  No=Integer.parseInt(rowNo);
	  web.line=No;

	  //通过java反射机制调用excel文件中指定的关键字方法。
	  String runRes=runUIWithInvoke(keywords, param1, param2, param3);
	  //行数和用例名称输出
	  System.out.println(rowNo+"  "+casename + "  "+keywords+ "  "+param1+ "  "+param2+ "  "+ runRes);
 	  //通过testng的断言机制来判断用例执行的结果是否是通过的。
	  assertEquals(runRes, "pass");
  }

  //读数据
  @DataProvider
  public Object[][] keywords() {
	  return caseExcel.readAsMatrix(2);
  }

  //完成excel文件的读取和关键字类的实例化
  @BeforeSuite
  public void beforeSuite() {
	  //获取当前的执行时间
      Date date = new Date();
      //将时间以设定的标准格式转存为一个字符串
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
      createdate = sdf.format(date);
      //指定文件路径打开excel用例文件，并且拷贝一份结果文件用于记录执行结果
      rootpath = System.getProperty("user.dir");
      String filename ="BasicWebCases2";
      caseExcel = new ExcelReader(rootpath+"\\cases\\"+filename +".xlsx");
	  resultExcel = new ExcelWriter(rootpath+"\\cases\\" + filename + ".xlsx", rootpath+"\\cases\\result\\result-"+createdate+ filename +".xlsx");
	  web =new DataDrivenOfWeb(resultExcel,0);
  }
  
  //清理测试环境，保存excel结果，关闭excel在内存中的对象。
  @AfterSuite
  public void afterSuite() {
	  caseExcel.close();
	  resultExcel.save();
//	  report htmlReport=new report();
//	  htmlReport.sendreport(rootpath+"\\cases\\\\result\\result-"+createdate+"WebCases2.xlsx",createdate);
  }
  //需要调用的反射方式执行关键字的方法。
  private String runUIWithInvoke(String key,String param1,String param2,String param3) {
	  	String result ="fail";
		try {
			Method appMethod = web.getClass().getDeclaredMethod(key);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=appMethod.invoke(web).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1, param2).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class, String.class, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1, param2,param3).toString();
			return result;
		} catch (Exception e) {
		}
		return result;
	}
  
}
