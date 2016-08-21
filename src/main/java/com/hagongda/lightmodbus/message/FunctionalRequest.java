package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.command.ICommand;

public class FunctionalRequest extends MDRequest {
	public ICommand command;
	public FunctionalRequest( ICommand command) {
		super();
		this.command = command;
		this.setComm_code(command.getCommandCode());
		this.setDataLength(command.getParamBody().getBytes().length);
	}

	@Override
	public MDResponse createResponse() {
		return null;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		dout.writeBytes(command.getParamBody());
	}

	@Override
	public void readData(DataInput din) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
