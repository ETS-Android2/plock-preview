package com.petarpuric.plock;

public class GenException extends Exception {
	private String noOption;
	private String msg;
	
	public GenException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	//Exception thrown if no options are selected
	public String badOPtionChoice() {
		noOption = "You must select at least 1 option";
		return noOption;
		
	}
	//Exception thrown if use/password fields are blank
	public String badSave() {
		
		if (msg.equals("2")) {
			noOption = "You must specify a use";
		}
		else 
			noOption = "You must specify a password";
		
		return noOption;
	}

}
