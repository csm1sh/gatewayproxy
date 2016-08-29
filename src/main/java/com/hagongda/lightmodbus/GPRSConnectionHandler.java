package com.hagongda.lightmodbus;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hagongda.lightmodbus.io.MDTCPSlaveConnection;
import com.hagongda.lightmodbus.io.MDTcpSlaveTransaction;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;

public class GPRSConnectionHandler implements CommandHandle {
     private final static Log logger = LogFactory.getLog(GPRSConnectionHandler.class); 
     private MDTCPSlaveConnection m_Connection;
	 private Map<Integer, RequestSender> reqSenderPool = new Hashtable<Integer, RequestSender>();
	 private static final int WAITING_NUM = 2;
	 private ExecutorService executor = Executors.newCachedThreadPool();

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
	    return reqSenderPool.size() > WAITING_NUM;
    }

	@Override
	public void sendRequest(MDRequest request) {
		if (isBusy()) {
			return;
		}		
		logger.debug("Send request=" + request.getMessage());
		MDTcpSlaveTransaction trans = new MDTcpSlaveTransaction(m_Connection);
		trans.setRequest(request);
		RequestSender sender = new RequestSender(trans);
		reqSenderPool.put(trans.getTransactionID(), sender);
		Future<MDResponse> task = executor.submit(sender);
		try {			
			logger.debug("Read response=" + task.get().getMessage());
		} catch (InterruptedException e) {
			logger.error("Waiting response failed.", e);
		} catch (ExecutionException e) {
			logger.error("Waiting response failed.", e);
		} finally {
			reqSenderPool.remove(trans.getTransactionID());
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
