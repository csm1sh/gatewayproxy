package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.hagongda.devicebean.GatewayAuth;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.util.Json2Object;


public class AuthServerRequest extends MDRequest{

    /**
      */
    private final Logger logger = Logger.getLogger(AuthServerRequest.class);
    GatewayAuth gatewayAuth = new GatewayAuth();
	@Override
	public MDResponse createResponse() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AuthServerRequest(){
		super();
		//set default 
		this.setDataLength(this.getDataLength());
		this.setComm_code( GateWayCommandCode.AUTH_GRPS);
	}
	
    public int getDataLength() {
        return gatewayAuth.toJson().getBytes().length;
      }

	@Override
	public void writeData(DataOutput dout) throws IOException {
		dout.writeBytes(gatewayAuth.toJson());
	}

	@Override
	public void readData(DataInput din) throws IOException {
		byte[] authParambody = new byte[this.getDataLength()];
		din.readFully(authParambody);
		gatewayAuth = new Json2Object<GatewayAuth>(){}.toMap(new String(authParambody));
		//din.readByte();
		//logger.info(gatewayAuth.toJson());
	}
	
    public GatewayAuth getGatewayAuth(){
        return this.gatewayAuth;
    }

    @Override
    public String getParamBody()
    {
        // TODO Auto-generated method stub
        return gatewayAuth.toJson();
    }
	

}
