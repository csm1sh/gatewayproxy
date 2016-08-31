package com.hagongda.lightmodbus;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.io.MDTCPSlaveConnection;
import com.hagongda.lightmodbus.io.MDTCPTransport;
import com.hagongda.lightmodbus.io.MDTcpSlaveTransaction;
import com.hagongda.lightmodbus.message.AuthServerResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;
import net.wimpi.modbus.ModbusIOException;

public class GPRSConnectionHandler extends BaseConnectionHandler implements Runnable, CommandHandle {
     private final static Log logger = LogFactory.getLog(GPRSConnectionHandler.class); 
     private MDTCPSlaveConnection m_Connection;
	 private static final int WAITING_NUM = 2;
	 private ExecutorService executor = Executors.newCachedThreadPool();
	 private final static int TIME_OUT_BETWEEN_PROXY_AND_GATEWAY = 50;
	 private AtomicInteger waitingReqCount = new AtomicInteger(0);

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
	  }//setConnection
	  
	public MDTCPSlaveConnection getConnection() {
		return m_Connection;
	}

    public void destroy() throws IOException {
        m_Connection.close();
    }
  
    public boolean isBusy(){
	    return waitingReqCount.get() > WAITING_NUM;
    }

	@Override
	public void sendRequest(MDRequest request) throws TimeoutException {		
		logger.debug("Send request=" + request.getMessage());
		MDTcpSlaveTransaction trans = new MDTcpSlaveTransaction(m_Connection);
		trans.setRequest(request);
		RequestSender sender = new RequestSender(trans);
		waitingReqCount.incrementAndGet();
		Future<MDResponse> task = executor.submit(sender);
		try {
			MDResponse response = task.get(TIME_OUT_BETWEEN_PROXY_AND_GATEWAY, TimeUnit.SECONDS);
			logger.debug("Read response=" + response.getMessage());
		} catch (InterruptedException e) {
			logger.error("Waiting response failed.", e);
		} catch (ExecutionException e) {
			logger.error("Waiting response failed.", e);
		} catch (TimeoutException e){
			throw e;
		} finally {
			waitingReqCount.decrementAndGet();
		}
    }
	
	@Override
	public void registeSelfAfterAuth(String gatewayId) {
		GPRSHandlerPool.getInstance().register(gatewayId, this);
	}

	@Override
	public void run() {
		try {
			boolean hasAuthed = false;
			do {
				MDTCPTransport m_Transport = m_Connection.getModbusTransport();
				MDRequest request = m_Transport.readRequest();
				MDResponse response = null;
				if (request != null) {
					int commandCode = request.getComm_code();
					switch (commandCode) {
						case GateWayCommandCode.AUTH_GRPS:
							response = auth(request);
							if (response instanceof AuthServerResponse) {
								hasAuthed = true;
							}							
							break;
						default:
							response = request.createExceptionResponse("Unsupported request.");
							break;
					}
						
				}
				if (response != null) {
					logger.debug("Server::Send Response:" + response.getMessage());
					m_Transport.writeMessage(response);
				}
			} while (!hasAuthed);
		} catch (ModbusIOException ex) {
			ex.printStackTrace();
			if (!ex.isEOF()) {
				// other troubles, output for debug
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	class RequestSender implements Callable<MDResponse>{
		  private MDTcpSlaveTransaction transaction;
		  
		  public RequestSender(MDTcpSlaveTransaction transaction) {
			  this.transaction = transaction;
		  }
		  
		  public MDTcpSlaveTransaction getTransaction() {
			  return transaction;
		  }
		  
	      @Override
	      public MDResponse call() throws Exception {      	
	    	  transaction.execute();
	    	  return transaction.getResponse();
	      }
    }

}
