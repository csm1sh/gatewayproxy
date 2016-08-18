package com.growte.lightmodbus;

import java.net.InetAddress;

import com.growte.lightmodbus.io.MDTcpListener;

import net.wimpi.modbus.Modbus;

public class LightMasterDamon {
	public static void main(String[] args){
		MDTcpListener listener = null;
		    int port = Modbus.DEFAULT_PORT;
		  
		    try {
		      if(args != null && args.length ==1) {
		        port = Integer.parseInt(args[0]);
		      }
		      System.out.println("jModbus Modbus Slave (Server)");
		      //3. create a listener with 3 threads in pool
		      if (Modbus.debug) System.out.println("Listening...");
		      listener = new MDTcpListener(3, InetAddress.getByName("localhost"));
		      listener.setPort(port);
		      listener.start();

		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
	}
}
