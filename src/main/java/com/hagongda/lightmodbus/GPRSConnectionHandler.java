package com.hagongda.lightmodbus;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.wimpi.modbus.ModbusIOException;

import org.apache.log4j.Logger;

import com.hagongda.devicebean.GatewayAuth;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.io.MDTCPSlaveConnection;
import com.hagongda.lightmodbus.io.MDTCPTransport;
import com.hagongda.lightmodbus.message.AuthServerRequest;
import com.hagongda.lightmodbus.message.AuthServerResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;

public class GPRSConnectionHandler implements Runnable, CommandHandle {
    final Lock lock = new ReentrantLock();    //注意这个地方

	 final private static Logger logger = Logger.getLogger(GPRSConnectionHandler.class); 
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
	    	  if(request != null && request.getComm_code() == GateWayCommandCode.AUTH_GRPS){
	    		  response = handleAuth(request);
	    	  }else if(request !=null && request.getComm_code() == GateWayCommandCode.HEART_BEAT){
	    		  handleHB(request);
	    	  }else{
	    	      logger.info("Server::Recived functional response:" + request.getMessage());
	    	      //response = request.createResponse();
	    	  }
	        /*DEBUG*/

	        //System.out.println("Response:" + response.getHexMessage());
	        if(response != null){
	        	logger.info("Server::Send Response:" + response.getMessage());
	        	m_Transport.writeMessage(response);
	        }
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
		  GatewayAuth auth = authRequest.getGatewayAuth();
		  if(!authService.auth(auth)){
			  response = request.createExceptionResponse(GatewayAuth.Failed());
		  }else{ 
		    AuthServerResponse authResponse = new AuthServerResponse(GatewayAuth.Ok());
		    response = authResponse;
			GPRSHandlerPool.getInstance().register(auth.getPhoneNum(), this);
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
