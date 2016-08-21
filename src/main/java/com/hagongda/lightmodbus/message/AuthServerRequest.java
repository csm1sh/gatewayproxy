package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.util.MDUtil;


public class AuthServerRequest extends MDRequest{
	String password = "123456";
	String phoneNum = "18563912825";
	String authCode;
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	static final int AUTH_CODE_LENGTH = 16;  
	@Override
	public MDResponse createResponse() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AuthServerRequest(){
		super();
		this.setDataLength(AUTH_CODE_LENGTH);
		this.setComm_code( GateWayCommandCode.AUTH_SERVER);
	}

	@Override
	public void writeData(DataOutput dout) throws IOException {
		dout.writeBytes(MDUtil.MD5(phoneNum).concat(password));
	}

	@Override
	public void readData(DataInput din) throws IOException {
		byte[] md5 = new byte[AUTH_CODE_LENGTH];
		din.readFully(md5);
		authCode = new String(md5);
	}

}
