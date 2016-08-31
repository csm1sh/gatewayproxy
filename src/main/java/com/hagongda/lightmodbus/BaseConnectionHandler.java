package com.hagongda.lightmodbus;

import com.hagongda.devicebean.GatewayAuth;
import com.hagongda.lightmodbus.message.AuthServerRequest;
import com.hagongda.lightmodbus.message.AuthServerResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;

public abstract class BaseConnectionHandler {
	private IAuthService authService = AuthServiceProvider.getInstance().getDefault();
	
	protected MDResponse auth(MDRequest request) {
		MDResponse response = null;
		AuthServerRequest authRequest= ((AuthServerRequest)request);
		  GatewayAuth auth = authRequest.getGatewayAuth();
		  if(!authService.auth(auth)){
			  response = request.createExceptionResponse(GatewayAuth.Failed());
		  }else{ 
			  response = new AuthServerResponse(GatewayAuth.Ok());
			  registeSelfAfterAuth(auth.getPhoneNum());
		  }
		return response;
	}
	
	public abstract void registeSelfAfterAuth(String gatewayId);

}
