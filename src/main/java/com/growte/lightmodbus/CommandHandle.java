package com.growte.lightmodbus;

import com.growte.lightmodbus.message.MDRequest;

public interface CommandHandle {
	public void sendRequest(MDRequest request);
}
