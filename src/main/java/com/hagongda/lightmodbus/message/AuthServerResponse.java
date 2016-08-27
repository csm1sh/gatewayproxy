package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.util.MDUtil;

public class AuthServerResponse
    extends MDResponse
{

  
  String statusMsg;
  public  AuthServerResponse(){
	  super();
	 this.setComm_code( GateWayCommandCode.AUTH_GRPS);
 }
    
  public AuthServerResponse(String statusMsg){
    super();
    this.statusMsg = statusMsg;
    this.setDataLength(statusMsg.getBytes().length);
    this.setComm_code( GateWayCommandCode.AUTH_GRPS);
  }

  @Override
  public void writeData(DataOutput dout) throws IOException
  {
    dout.writeBytes(this.statusMsg); 
  }

  @Override
  public void readData(DataInput din) throws IOException
  {
		byte[] msg = new byte[this.getDataLength()];
		din.readFully(msg);
		statusMsg = new String(msg);
  }

@Override
public String getParamBody()
{
    return statusMsg;
}

}
