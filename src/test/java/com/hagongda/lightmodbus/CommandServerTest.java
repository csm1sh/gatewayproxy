package com.hagongda.lightmodbus;

import java.net.InetAddress;
import org.apache.log4j.Logger;
import com.hagongda.lightmodbus.io.MDTCPMasterConnection;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;
import net.wimpi.modbus.Modbus;

public class CommandServerTest {
	 static final Logger logger = Logger.getLogger(AuthServerTest.class);
		public static void main(String[] args) {
			MDTCPMasterConnection con = null;
			InetAddress addr = null;
			int port = Modbus.DEFAULT_PORT;
			try {
				con = new MDTCPMasterConnection(addr);
				con.setTimeout(1000000);
				con.setPort(port);
				con.connect();
				logger.info("simulator::Connected to " + addr.toString() + ":" + con.getPort());
				while(true){
			    	  Thread.currentThread().sleep(1000);
			    	  MDRequest functionRequst = con.getModbusTransport().readRequest();
			    	  logger.info("simulator::recived function request =" + functionRequst.getMessage());
			    	  MDResponse response = functionRequst.createResponse();
			    	  logger.info("simulator::sending function response =" + response.getMessage());
			    	  con.getModbusTransport().writeMessage(response);
			    }			      
			} catch (Exception ex) {
				ex.printStackTrace(); 
			}finally{
				con.close();
			}
		}// main
}
