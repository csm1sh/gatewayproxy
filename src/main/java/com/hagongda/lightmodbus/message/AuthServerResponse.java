package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.util.MDUtil;

public class AuthServerResponse
    extends MDResponse
{

  static final int AUTH_CODE_LENGTH = 16;  
  private String authCode;
  
  public AuthServerResponse(){
    super();
    this.setDataLength(AUTH_CODE_LENGTH);
    this.setComm_code( GateWayCommandCode.AUTH_SERVER);
  }

  @Override
  public void writeData(DataOutput dout) throws IOException
  {
    dout.writeBytes(this.authCode); 
  }

  @Override
  public void readData(DataInput din) throws IOException
  {
		byte[] md5 = new byte[AUTH_CODE_LENGTH];
		din.readFully(md5);
		authCode = new String(md5);

  }
  
  public void setAuthCode(String authCode){
    this.authCode = authCode;
  }

}
