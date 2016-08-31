package com.hagongda.lightmodbus;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.io.MDTCPSlaveConnection;
import com.hagongda.lightmodbus.io.MDTCPTransport;
import com.hagongda.lightmodbus.message.AlarmRequest;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;
import net.wimpi.modbus.ModbusIOException;

public class GPRSConnectionReadHandler extends BaseConnectionHandler implements Runnable {
	private final Log logger = LogFactory.getLog(GPRSConnectionReadHandler.class);
	private MDTCPSlaveConnection m_Connection;
	private MDTCPTransport m_Transport;
	private boolean running = true;
	private Queue<AlarmRequest> alarmQueue = new LinkedList<AlarmRequest>();
	private String gatewayId;

	/**
	   * Constructs a new <tt>TCPConnectionHandler</tt> instance.
	   *
	   * @param con an incoming connection.
	   */
	public GPRSConnectionReadHandler( MDTCPSlaveConnection con) {
	    setConnection(con);
	}

	/**
	 * Sets a connection to be handled by this <tt>
	 * TCPConnectionHandler</tt>.
	 *
	 * @param con a <tt>TCPSlaveConnection</tt>.
	 */
	public void setConnection(MDTCPSlaveConnection con) {
	    m_Connection = con;
	    m_Transport = m_Connection.getModbusTransport();
	}
	
	public MDTCPSlaveConnection getConnection() {
		return m_Connection;
	}

	@Override
	public void run() {
		try {
			do {
				MDRequest request = m_Transport.readRequest();
				MDResponse response = null;
				if (request != null) {
					int commandCode = request.getComm_code();
					switch (commandCode) {
						case GateWayCommandCode.AUTH_GRPS:
							response = auth(request);
							break;
						case GateWayCommandCode.HEART_BEAT:
							handleHB(request);
							break;
						case GateWayCommandCode.ALARM:
							handleAlarm(request);
							break;
						default:
							response = request.createExceptionResponse("Unsupported request.");
							break;
					}
						
				}
				// System.out.println("Response:" + response.getHexMessage());
				if (response != null) {
					logger.debug("Server::Send Response:" + response.getMessage());
					m_Transport.writeMessage(response);
				}
			} while (running);
		} catch (ModbusIOException ex) {
			ex.printStackTrace();
			if (!ex.isEOF()) {
				// other troubles, output for debug
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				m_Connection.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}		
	}
	
	private void handleHB(MDRequest request){
	    
	}
	
	private void handleAlarm(MDRequest request) {
		AlarmRequest alarmReq = (AlarmRequest)request;
		if (!alarmQueue.offer(alarmReq)) {
			logger.error(gatewayId + ": alarm queue is full.");
		}
	}
	
	public void destroy() throws IOException {
		running = false;
	    m_Connection.close();
	}

	@Override
	public void registeSelfAfterAuth(String gatewayId) {
		GPRSReadHandlerPool.getInstance().register(gatewayId, this);		
	}

}
