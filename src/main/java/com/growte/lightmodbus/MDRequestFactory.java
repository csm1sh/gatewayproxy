package com.growte.lightmodbus;

import com.growte.lightmodbus.code.ModBusCommCode;
import com.growte.lightmodbus.command.Writable;
import com.growte.lightmodbus.message.AlarmRequest;
import com.growte.lightmodbus.message.AuthServerRequest;
import com.growte.lightmodbus.message.FunctionalRequest;
import com.growte.lightmodbus.message.HeartBeatRequest;
import com.growte.lightmodbus.message.MDRequest;

public class MDRequestFactory {
	static MDRequestFactory instance;
	public MDRequest buildFrom(int comandCode){
		 MDRequest request = null;
		 switch (comandCode) {
		 case ModBusCommCode.AUTH_SERVER:
		   request = new AuthServerRequest(); break;
		 case ModBusCommCode.HEART_BEAT:
			   request = new HeartBeatRequest(); break;
		 case ModBusCommCode.ALARM:
			   request = new AlarmRequest(); break;
		  default:
			  request = null;
		 }
		 return request;
	}
	
	
	public MDRequest buildFrom(Writable command){
	  return new FunctionalRequest(command);
	}
	
	private MDRequestFactory(){
		
	}
	
	public static MDRequestFactory getInstacce(){
		if(instance == null){
			instance = new MDRequestFactory();
		}
		return instance;
	}

}
