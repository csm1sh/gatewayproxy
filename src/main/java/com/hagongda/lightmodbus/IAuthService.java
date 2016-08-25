package com.hagongda.lightmodbus;

import com.hagongda.devicebean.GatewayAuth;

public interface IAuthService {
	public boolean auth(GatewayAuth authCode);
}
