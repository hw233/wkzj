package com.jtang.gameserver.module.user.platform;

public class PlatformJsonResult {
	public String StatusCode;
	public String UserId;
	public String Successed;
	public String Message;
//	{"StatusCode":1,"UserId":4912,"Successed":true,"Message":"ok"}
	public boolean isOK(){
		if (Successed == null || "".equals(Successed)|| Successed.equals("false")){
			return false;
		}
		
		if (Successed.equals("true")) {
			return true;
		}
		return false;
	}
	
}
