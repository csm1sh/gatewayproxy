package com.hagongda.lightmodbus;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hagongda.lightmodbus.code.ExceptionCode;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.io.MDTCPSlaveConnection;
import com.hagongda.lightmodbus.io.MDTCPTransport;
import com.hagongda.lightmodbus.message.AuthServerRequest;
import com.hagongda.lightmodbus.message.AuthServerResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;

public class GPRSConnectionHandler implements Runnable, CommandHandle {

	 private MDTCPSlaveConnection m_Connection;
	 private MDTCPTransport m_Transport;
	 private IAuthService authService = AuthServiceProvider.getInstance().getDefault();
	 private Date lastHBTime = new Date();
	 private boolean running = true;
	 private int waitingCount = 2;

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
	    	  if(request != null && request.getComm_code() == GateWayCommandCode.AUTH_SERVER){
	    		  response = handleAuth(request);
	    	  }else if(request !=null && request.getComm_code() == GateWayCommandCode.HEART_BEAT){
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
	  Lock lock = new ReentrantLock();    //注意这个地方
      lock.lock();
	 try {
		m_Transport.writeMessage(request);
	} catch (ModbusIOException e) {
		e.printStackTrace();
	}finally{
		lock.unlock();
	}
  }

  public void destroy() throws IOException
  {
    running = false;
    m_Connection.close();
  }
  
  public boolean isBusy(){
	  return waitingCount > 2;
  }
	  

}
