package com.growte.lightmodbus.command;

import java.io.DataOutput;
import java.io.IOException;

import com.growte.devicebean.LightInfo;

public interface Writable {
	public void writeData(DataOutput dop) throws IOException;
	public int dataLength();
	public void setLightInfo(LightInfo info);
}
