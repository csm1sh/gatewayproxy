package com.hagongda.lightmodbus;

public class AuthServiceProvider {
	private static  AuthServiceProvider instance = null;
	private  AuthServiceProvider(){
	}
	public static AuthServiceProvider getInstance(){
		if(instance == null){
			instance = new AuthServiceProvider();
		}
		return instance;
	}
	//return a mock IAuthService
	public IAuthService getDefault(){
		return new IAuthService() {
			
			public String auth(String authCode) {
				return "18563912825";
			}
		};
	}
	
}
