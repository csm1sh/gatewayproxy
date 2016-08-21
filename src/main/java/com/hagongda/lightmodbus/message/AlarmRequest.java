package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;

public class AlarmRequest  extends MDRequest {

	public AlarmRequest() {
		super();
		this.setDataLength(16);
		this.setComm_code( GateWayCommandCode.ALARM);
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
