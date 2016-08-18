package com.growte.lightmodbus;

import java.util.HashMap;
import java.util.Map;

public class GPRSHandlerPool {
	private static GPRSHandlerPool instance; 
	private final Map<String, GPRSConnectionHandler> pool = new HashMap<String, GPRSConnectionHandler>();
	private GPRSHandlerPool(){
		
	}
	
	public static GPRSHandlerPool getInstance(){
		if(instance == null){
			instance = new GPRSHandlerPool();
		}
		return instance;
	}
	
	public void register(String phoneNumber, GPRSConnectionHandler handler){
		synchronized (pool) {
			pool.put(phoneNumber, handler);
		}
	}
	
	public GPRSConnectionHandler get(String phoneNumber){
	  synchronized (pool) {
	    return pool.get(phoneNumber);
		}
	}
	
}
