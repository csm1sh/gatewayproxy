package com.hagongda.lightmodbus;

import com.hagongda.devicebean.GatewayAuth;

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
			
			public boolean auth(GatewayAuth authCode) {
				return true;
			}
		};
	}
	
}
