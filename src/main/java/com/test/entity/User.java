package com.test.entity;

import java.util.Date;

public class User {
	private String id;
	private String name; 
	private int age; 
	private Date address;
	private Date date;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getAddress() {
		return address;
	}
	public void setAddress(Date address) {
		this.address = address;
	}
	@Override
	public String toString(){
		
		return getId() + "," + getName()+"," + getAge() +"," +getAddress();
		
	}
}
