package com.hagongda.lightmodbus.command;

import java.io.DataOutput;
import java.io.IOException;

public class FunctionalCommand implements ICommand {

	int command_code;
	String paramBody = null;;
	
	public FunctionalCommand(int commandCode, String paramBody){
		this.command_code = commandCode;
		this.paramBody = paramBody;
	}
	
	public void writeData(DataOutput dop) throws IOException {
		dop.writeBytes(this.paramBody);
	}


	@Override
	public String getParamBody() {
		return this.paramBody;
	}

	@Override
	public int getCommandCode() {
		return this.command_code;
	}

}
