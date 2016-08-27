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

public class AuthServerTest {
    static final Logger logger = Logger.getLogger(AuthServerTest.class);
	public static void main(String[] args) {
		MDTCPMasterConnection con = null;
		MDTcpTransaction trans = null;
		InetAddress addr = null;
		int port = Modbus.DEFAULT_PORT;
		try {
			try {
				String astr = args[0];
				int idx = astr.indexOf(':');
				if (idx > 0) {
					port = Integer.parseInt(astr.substring(idx + 1));
					astr = astr.substring(0, idx);
				}
				addr = InetAddress.getByName(astr);
			} catch (Exception ex) {
				ex.printStackTrace();
				printUsage();
				System.exit(1);
			}
			// 2. Open the connection
			con = new MDTCPMasterConnection(addr);
			con.setTimeout(1000000);
			con.setPort(port);
			con.connect();
			logger.info("Connected to " + addr.toString() + ":" + con.getPort());
			// 3. Prepare the request
			AuthServerRequest authReq = (AuthServerRequest)MDRequestFactory.getInstacce().buildFrom(GateWayCommandCode.AUTH_GRPS);
			logger.info("send Request: " + authReq.getMessage());
			 //4. Prepare the transaction
		      trans = new MDTcpTransaction(con);
		      trans.setRequest(authReq);
		      trans.setReconnecting(false);
		      trans.execute();
		      MDResponse res = trans.getResponse();
		      logger.info("auth Response: " + res.getMessage() );
		      while(true){
		    	  Thread.currentThread().sleep(1000);
		    	  MDRequest functionRequst = con.getModbusTransport().readRequest();
		    	  logger.info("function request =" + functionRequst.getMessage());
		      }
		} catch (Exception ex) {
			ex.printStackTrace(); 
		}finally{
			con.close();
		}
	}// main

	private static void printUsage() {
		System.out.println(
				"java net.wimpi.modbus.cmd.DITest <address{:<port>} [String]> <register [int16]> <bitcount [int16]> {<repeat [int]>}");
	}// printUsage
}
