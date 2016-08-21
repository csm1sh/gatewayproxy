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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCommandCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
