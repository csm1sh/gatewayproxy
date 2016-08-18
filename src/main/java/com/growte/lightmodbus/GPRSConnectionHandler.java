package com.growte.lightmodbus;

import java.io.IOException;
import java.util.Date;

import com.growte.lightmodbus.code.ExceptionCode;
import com.growte.lightmodbus.code.ModBusCommCode;
import com.growte.lightmodbus.io.MDTCPSlaveConnection;
import com.growte.lightmodbus.io.MDTCPTransport;
import com.growte.lightmodbus.message.AuthServerRequest;
import com.growte.lightmodbus.message.AuthServerResponse;
import com.growte.lightmodbus.message.MDRequest;
import com.growte.lightmodbus.message.MDResponse;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;

public class GPRSConnectionHandler implements Runnable, CommandHandle {

	 private MDTCPSlaveConnection m_Connection;
	 private MDTCPTransport m_Transport;
	 private IAuthService authService = AuthServiceProvider.getInstance().getDefault();
	 private Date lastHBTime = new Date();
	 private boolean running = true;

	  /**
	   * Constructs a new <tt>TCPConnectionHandler</tt> instance.
	   *
	   * @param con an incoming connection.
	   */
	  public GPRSConnectionHandler(MDTCPSlaveConnection con) {
	    setConnection(con);
	  }//constructor

	  /**
	   * Sets a connection to be handled by this <tt>
	   * TCPConnectionHandler</tt>.
	   *
	   * @param con a <tt>TCPSlaveConnection</tt>.
	   */
	  public void setConnection(MDTCPSlaveConnection con) {
	    m_Connection = con;
	    m_Transport = m_Connection.getModbusTransport();
	  }//setConnection

	  public void run() {
	    try {
	      do {
	    	  MDResponse response = null;
	    	  //1. read the request
	    	  MDRequest request = m_Transport.readRequest();
	    	  if(request != null && request.getComm_code() == ModBusCommCode.AUTH_SERVER){
	    		  response = handleAuth(request);
	    	  }else if(request !=null && request.getComm_code() == ModBusCommCode.HEART_BEAT){
	    		  handleHB(request);
	    	  }else{
	    	    if (Modbus.debug) System.out.println("Request:" + request.getHexMessage());
	    	    response = request.createResponse();
	    	  }
	        /*DEBUG*/
	        if (Modbus.debug) System.out.println("Response:" + response.getHexMessage());

	        //System.out.println("Response:" + response.getHexMessage());
	        if(response != null)
	          m_Transport.writeMessage(response);
	      } while (running);
	    } catch (ModbusIOException ex) {
	    	ex.printStackTrace();
	      if (!ex.isEOF()) {
	        //other troubles, output for debug
	        ex.printStackTrace();
	      }
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    finally {
	      try {
	        m_Connection.close();
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }

	    }
	  }//run

	private MDResponse handleAuth(MDRequest request) {
		MDResponse response = null;
		AuthServerRequest authRequest= ((AuthServerRequest)request);
		  String authCode= authRequest.getAuthCode();
		  String phoneNum = authService.auth(authCode);
		  if(phoneNum == null){
			  response = request.createExceptionResponse(ExceptionCode.NOT_AHTHED);
		  }else{ 
		    AuthServerResponse authResponse = new AuthServerResponse();
		    authResponse.setAuthCode(authCode);
		    response = authResponse;
			GPRSHandlerPool.getInstance().register(authRequest.getAuthCode(), this);
		  }
		return response;
	}
	
  private void handleHB(MDRequest request)
  {
    lastHBTime = new Date();
  }

  public boolean isExpired()
  {
    return new Date().getTime() - lastHBTime.getTime() > 30 * 1000;
  }

  //read request from message queue consumer
  public void sendRequest(MDRequest request)
  {
     try {
		m_Transport.writeMessage(request);
	} catch (ModbusIOException e) {
		e.printStackTrace();
	}
  }

  public void destroy() throws IOException
  {
    running = false;
    m_Connection.close();
  }
	  

}
