package com.testing.web.chint;

import com.testing.UI.KeywordOfWeb;

public class basic {
	public static void main(String[] args) {
		KeywordOfWeb kw  = new KeywordOfWeb();
//		kw.openBrowser("chrome");
//		kw.visitWeb("http://test-cc.chintcloud.net");
//		kw.sleep("30");
//		kw.input("//*[@aria-label='所属产品']", "我_一型一密_直连非ssl");
//		kw.sleep("10");
//		
		

        String test = "abc123测试";
        // 遍历所有字符
        for (int i = 0; i < test.length(); i++) {
            char item = test.charAt(i);
            System.out.println(String.valueOf(item));
        }

		}

}
