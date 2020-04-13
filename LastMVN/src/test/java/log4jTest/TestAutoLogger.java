package log4jTest;


import log4jTest.AutoLogger;

public class TestAutoLogger {
	
//	<!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
	public static void main(String[] args) {
		AutoLogger.log.debug("DEBUG测试1");
		AutoLogger.log.info("INFO测试2");	
		AutoLogger.log.warn("warn测试3");	
		AutoLogger.log.error("ERROR测试4");	


	}
	

}