package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;

public class FunctionalResponse extends MDResponse {

	private static final String  OK = "{ status:'OK'}";
	private static final String  FAILED = "{ status:'Failed'}";
	private String statusMsg = OK;
	
	FunctionalResponse(int commd_code){
		this.setComm_code( commd_code);
		this.setDataLength(statusMsg.length());
	}
	@Override
	public String getParamBody() {
		// TODO Auto-generated method stub
		return this.statusMsg;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		 dout.writeBytes(this.statusMsg); 
	}

	@Override
	public void readData(DataInput din) throws IOException {
		byte[] msg = new byte[this.getDataLength()];
		din.readFully(msg);
		statusMsg = new String(msg);

	}

}
