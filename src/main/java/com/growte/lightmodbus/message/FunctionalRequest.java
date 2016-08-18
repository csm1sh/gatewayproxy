package com.growte.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.growte.lightmodbus.code.ModBusCommCode;
import com.growte.lightmodbus.command.Writable;

public class FunctionalRequest extends MDRequest {
	public Writable command;
	public FunctionalRequest( Writable command) {
		super();
		this.command = command;
		this.setDataLength(command.dataLength());
		this.setComm_code( ModBusCommCode.COMM_REREDIRECT);
	}

	@Override
	public MDResponse createResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		command.writeData(dout);
	}

	@Override
	public void readData(DataInput din) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
