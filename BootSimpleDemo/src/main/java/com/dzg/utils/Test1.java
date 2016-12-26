package com.dzg.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * String 类的相关问题
 * 
 * @author DZG
 * @since V1.0 2016年9月12日
 */
public class Test1 {

	// 检测回文1
	public static boolean isHuiWen(String str) {
		if (null == str) {
			return false;
		} else {
			StringBuffer sb = new StringBuffer(str);
			return str.equals(sb.reverse().toString());
		}
	}

	// 检测回文2
	public static boolean isHuiWen2(String str) {
		if (null == str) {
			return false;
		} else {
			int length = str.length();
			System.out.println(length / 2);
			for (int i = 0; i < length / 2; i++) {
				if (str.charAt(i) != str.charAt(length - i - 1)) {
					return false;
				}
			}
			return true;
		}
	}

	// 删除给定字符串指定的字符
	public static String deleteChar(String str, char c) {
		if(null == str){
			return null;
		}else{
			return str.replaceAll(Character.toString(c), "");
		}
	}
	
	//字符串switch
	public static String getStrSwitch(String str) {
		switch (str) {
		case "1":
			return "这是1";
		case "2":
			return "这是2";
		default:
			return "不知道这是几";
		}
	}
	
	//使用递归排列字符串
	public static Set<String> getFlex(String str) {
		Set<String> sets = new HashSet<String>();
		return sets;
	}

	public static void main(String[] args) {
		System.err.println(isHuiWen("12321"));
		System.err.println(isHuiWen2("1232"));
		System.err.println(deleteChar("1232",new Character('2').charValue()));
		System.err.println(deleteChar("1232",'2'));
		System.err.println(getStrSwitch("1"));
		System.err.println(23&17 + 23|17);
	}
}
