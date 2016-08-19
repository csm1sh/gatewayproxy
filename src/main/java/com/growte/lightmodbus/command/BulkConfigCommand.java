package com.growte.lightmodbus.command;

import java.io.DataOutput;
import java.io.IOException;

import com.growte.devicebean.LightInfo;
import com.growte.lightmodbus.code.GateWayCommandCode;

public class BulkConfigCommand implements Writable {
	LightInfo lightInfo;
	int functionCode = GateWayCommandCode.BULK_CONFIG;
	int regNum = 6;
	int byteNum = 0x0c;
	
	public BulkConfigCommand(){
	    
	}
	
	
	public void writeData(DataOutput dop) throws IOException {
		dop.writeByte(functionCode);
	}
	public int dataLength() {
		return 0;
	}
	
	private void wirteCrc(DataOutput dop){
		
	}
	
	public void setLightInfo(LightInfo info){
		this.lightInfo = info;
	}

}
