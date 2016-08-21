package com.hagongda.lightmodbus.command;

import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.devicebean.LightInfo;
import com.hagongda.lightmodbus.code.GateWayCommandCode;

public class BulkConfigCommand implements ICommand {
	int commandCode = GateWayCommandCode.BULK_CONFIG;
	String paramBody;
	
	public BulkConfigCommand(String paramBody){
	    this.paramBody = paramBody;
	}
	
	
	public void writeData(DataOutput dop) throws IOException {
		dop.writeBytes(this.getParamBody());
	}
	public int dataLength() {
		return this.getParamBody().getBytes().length;
	}
	
	private void wirteCrc(DataOutput dop){
		
	}


	@Override
	public String getParamBody() {
		// TODO Auto-generated method stub
		return this.paramBody;
	}


	@Override
	public int getCommandCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
