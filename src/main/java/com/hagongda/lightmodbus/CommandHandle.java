package com.hagongda.lightmodbus;

import com.hagongda.lightmodbus.message.MDRequest;

public interface CommandHandle {
	public void sendRequest(MDRequest request);
	public boolean isBusy();
}
