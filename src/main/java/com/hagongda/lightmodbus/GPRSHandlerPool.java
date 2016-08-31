package com.hagongda.lightmodbus;

import java.util.Hashtable;
import java.util.Map;

public class GPRSHandlerPool {
	private static GPRSHandlerPool instance; 
	private final Map<String, GPRSConnectionHandler> pool = new Hashtable<String, GPRSConnectionHandler>();

	private GPRSHandlerPool(){}
	
	public static GPRSHandlerPool getInstance(){
		if(instance == null){
			instance = new GPRSHandlerPool();
		}
		return instance;
	}
	
	public void register(String phoneNumber, GPRSConnectionHandler handler){
		pool.put(phoneNumber, handler);
	}
		
	public GPRSConnectionHandler get(String phoneNumber){
		return pool.get(phoneNumber);
	}
	
}
