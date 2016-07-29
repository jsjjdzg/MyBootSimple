package com.dzg.entity;

public class Teacher {
	
	private String name;
	private String age;
	private String sex;
	private String className;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Teacher(String name) {
		super();
		this.name = name;
	}
	public Teacher() {
		super();
	}
}
