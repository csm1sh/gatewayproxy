package com.growte.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.growte.lightmodbus.code.GateWayCommandCode;

public class HeartBeatRequest  extends MDRequest {

	public HeartBeatRequest() {
		super();
		this.setDataLength(0);
		this.setComm_code( GateWayCommandCode.HEART_BEAT);
	}

	@Override
	public MDResponse createResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readData(DataInput din) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
