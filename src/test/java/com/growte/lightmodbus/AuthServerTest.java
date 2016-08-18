package com.growte.lightmodbus;

import java.net.InetAddress;

import com.growte.lightmodbus.io.MDTCPMasterConnection;
import com.growte.lightmodbus.io.MDTcpTransaction;
import com.growte.lightmodbus.message.AuthServerRequest;
import com.growte.lightmodbus.message.MDResponse;

import net.wimpi.modbus.Modbus;

public class AuthServerTest {
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
			con.setPort(port);
			con.connect();

			if (Modbus.debug)
				System.out.println("Connected to " + addr.toString() + ":" + con.getPort());

			// 3. Prepare the request
			AuthServerRequest req = new AuthServerRequest();

			if (Modbus.debug)
				System.out.println("Request: " + req.getHexMessage());
			 //4. Prepare the transaction
		      trans = new MDTcpTransaction(con);
		      trans.setRequest(req);
		      trans.setReconnecting(false);
		      trans.execute();
		      MDResponse res = trans.getResponse();
		      if (Modbus.debug) System.out.println("Response: " + res.getHexMessage() );
			//con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}// main

	private static void printUsage() {
		System.out.println(
				"java net.wimpi.modbus.cmd.DITest <address{:<port>} [String]> <register [int16]> <bitcount [int16]> {<repeat [int]>}");
	}// printUsage
}
