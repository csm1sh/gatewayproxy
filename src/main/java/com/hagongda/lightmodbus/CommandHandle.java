package com.hagongda.lightmodbus;

import java.util.concurrent.TimeoutException;
import com.hagongda.lightmodbus.message.MDRequest;

public interface CommandHandle {
	public void sendRequest(MDRequest request) throws TimeoutException;
	public boolean isBusy();
}
