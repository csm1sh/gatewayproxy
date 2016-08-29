package com.hagongda.lightmodbus;

import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Map;

public class GPRSHandlerPool {
	private static GPRSHandlerPool instance; 
	private final Map<String, GPRSConnectionHandler> pool = new Hashtable<String, GPRSConnectionHandler>();
	private final Map<InetAddress, GPRSConnectionHandler> notAuthedHandlers = new Hashtable<InetAddress, GPRSConnectionHandler>();

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
	
	public void putNotAuthedPool(GPRSConnectionHandler handler) {
		notAuthedHandlers.put(handler.getConnection().getAddress(), handler);
	}
		
	public synchronized GPRSConnectionHandler get(String phoneNumber){
		GPRSConnectionHandler handler =  pool.get(phoneNumber);
		if (handler == null) {
			GPRSConnectionReadHandler readHandler = GPRSReadHandlerPool.getInstance().get(phoneNumber);
			if (readHandler != null) {
				InetAddress address = readHandler.getConnection().getAddress();
				handler = notAuthedHandlers.get(address);
				if (handler != null) {
					pool.put(phoneNumber, handler);
					notAuthedHandlers.remove(address);
				}
			}
		}
		return handler;
	}
	
}
