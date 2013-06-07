package com.petarpuric.plock;

public class Passwords {
	
	//Passwords class created to store passwords and their use for easy retrieval from the database
	
	int id, HId;
	String use;
	String pass;
	
	
	public int getHId() {
		return HId;
	}
	
	public void setHId(int HId) {
		this.HId = HId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		
		this.id = id;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	

}


