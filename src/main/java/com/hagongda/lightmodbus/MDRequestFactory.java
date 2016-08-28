package com.hagongda.lightmodbus;

import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.command.ICommand;
import com.hagongda.lightmodbus.message.AlarmRequest;
import com.hagongda.lightmodbus.message.AuthServerRequest;
import com.hagongda.lightmodbus.message.FunctionalRequest;
import com.hagongda.lightmodbus.message.HeartBeatRequest;
import com.hagongda.lightmodbus.message.MDRequest;

public class MDRequestFactory {
	static MDRequestFactory instance;
	public MDRequest buildFrom(int comandCode){
		 MDRequest request = null;
		 switch (comandCode) {
		 case GateWayCommandCode.AUTH_GRPS:
		   request = new AuthServerRequest(); break;
		 case GateWayCommandCode.HEART_BEAT:
			   request = new HeartBeatRequest(); break;
		 case GateWayCommandCode.ALARM:
			   request = new AlarmRequest(); break;
		 case GateWayCommandCode.SET_LOCATION:
		 case GateWayCommandCode.SET_GROUP:
		 case GateWayCommandCode.SET_TIME:
		 case GateWayCommandCode.SET_TIME_SLOT:
			   request = new FunctionalRequest(comandCode); break;
		  default:
			  request = null;
		 }
		 return request;
	}
	
	
	public MDRequest buildFrom(ICommand command){
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
