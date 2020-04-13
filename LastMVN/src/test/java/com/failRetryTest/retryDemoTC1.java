package com.failRetryTest;
 
import org.testng.annotations.Test;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Listeners;
public class retryDemoTC1 {
	public int randome()	  {    
		int max=100,min=1;
		int ran2 = (int) (Math.random()*(max-min)+min);
		return ran2;
		}
	
	@Test
	public void test1()
	{
		
	}
	@Test
	public void test2()
	{
		Assert.assertEquals(6, 6);
		System.out.println("222");
	}
//	@Test
//	public void test3()
//	{
//		Assert.assertEquals(1, 1);
//		System.out.println("333");
//		
//	}
	
	@Test
	public void test4()
	{
		System.out.println("******************* 测试重跑是否实现 ***********************");
		int a =randome();
		System.out.println(a+"值");
		Assert.assertEquals(a, 2);
		System.out.println("33");
	}
	
//	@Test
//	public void test5()
//	{
//		Assert.assertEquals(1, 1);
//		System.out.println("555");
//	}
}
