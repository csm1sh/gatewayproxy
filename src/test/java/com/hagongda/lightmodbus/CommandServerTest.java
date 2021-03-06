package com.hagongda.lightmodbus;

import java.net.InetAddress;
import org.apache.log4j.Logger;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.io.MDTCPMasterConnection;
import com.hagongda.lightmodbus.io.MDTcpTransaction;
import com.hagongda.lightmodbus.message.AuthServerRequest;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;
import net.wimpi.modbus.Modbus;

public class CommandServerTest {
	 static final Logger logger = Logger.getLogger(CommandServerTest.class);
		public static void main(String[] args) {
			MDTCPMasterConnection con = null;
			MDTcpTransaction trans = null;
			int port = Modbus.DEFAULT_PORT;
			try {
				// 2. Open the connection
				InetAddress addr = InetAddress.getByName("localhost");
				con = new MDTCPMasterConnection(addr);
				con.setTimeout(1000000);
				con.setPort(port);
				con.connect();
				logger.info("simulator::Connected to " + addr.toString() + ":" + con.getPort());
				// 3. Prepare the request
				AuthServerRequest authReq = (AuthServerRequest)MDRequestFactory.getInstacce().buildFrom(GateWayCommandCode.AUTH_GRPS);
				logger.info("simulator::send Request: " + authReq.getMessage());
				//4. Prepare the transaction
			    trans = new MDTcpTransaction(con);
			    trans.setRequest(authReq);
			    trans.setReconnecting(false);
			    trans.execute();
			    MDResponse res = trans.getResponse();
			    logger.info("simulator::send auth Response: " + res.getMessage() );
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
