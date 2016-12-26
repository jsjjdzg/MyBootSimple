package com.dzg.demo;

public class Test {

	public static void main(String[] args) {
		String a ="cache_result{&}hits";
		String[] b = a.split("\\{&\\}");
		for(String c : b){
			System.err.println(c);
		}
	}
}
