package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.command.FunctionalCommand;
import com.hagongda.lightmodbus.command.ICommand;

public class FunctionalRequest extends MDRequest {
	public ICommand command;
	public FunctionalRequest( ICommand command) {
		super();
		this.command = command;
		this.setComm_code(command.getCommandCode());
		this.setDataLength(command.getParamBody().getBytes().length);
	}
	
	public FunctionalRequest( int command) {
		super();
		this.setComm_code(command);
	}

	@Override
	public MDResponse createResponse() {
		FunctionalResponse response =  new FunctionalResponse(this.getComm_code());
		response.setTransactionID(this.getTransactionID());
		return response;
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		dout.writeBytes(command.getParamBody());
	}

	@Override
	public void readData(DataInput din) throws IOException {
		byte[] authParambody = new byte[this.getDataLength()];
		din.readFully(authParambody);
		command = new FunctionalCommand(this.getComm_code(), new String(authParambody));
	}

    @Override
    public String getParamBody()
    {
        // TODO Auto-generated method stub
        return this.command.getParamBody();
    }

}
