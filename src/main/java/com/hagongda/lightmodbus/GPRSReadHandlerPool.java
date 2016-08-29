package com.hagongda.lightmodbus;

import java.util.HashMap;
import java.util.Map;

public class GPRSReadHandlerPool {
	private static GPRSReadHandlerPool instance; 
	private final Map<String, GPRSConnectionReadHandler> pool = new HashMap<String, GPRSConnectionReadHandler>();
	
	private GPRSReadHandlerPool(){ }
	
	public static GPRSReadHandlerPool getInstance(){
		if(instance == null){
			instance = new GPRSReadHandlerPool();
		}
		return instance;
	}
	
	public void register(String phoneNumber, GPRSConnectionReadHandler handler){
		synchronized (pool) {
			pool.put(phoneNumber, handler);
		}
	}
	
	public GPRSConnectionReadHandler get(String phoneNumber){
	  synchronized (pool) {
	    return pool.get(phoneNumber);
		}
	}

}
