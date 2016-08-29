package com.hagongda.lightmodbus.io;

import com.hagongda.lightmodbus.exceptions.MDSlaveException;
import com.hagongda.lightmodbus.message.ExceptionResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.util.AtomicCounter;
import net.wimpi.modbus.util.Mutex;

public class MDTcpSlaveTransaction {
	  private static AtomicCounter c_TransactionID =
	      new AtomicCounter(Modbus.DEFAULT_TRANSACTION_ID);
	  private MDTCPSlaveConnection m_Connection;
	  private MDTCPTransport m_IO;
	  private MDRequest m_Request;
	  private MDResponse m_Response;
	  private boolean m_ValidityCheck = Modbus.DEFAULT_VALIDITYCHECK;
	  private Mutex m_TransactionLock = new Mutex();

	  public MDTcpSlaveTransaction(MDTCPSlaveConnection con) {
	    setConnection(con);
	  }

	  public void setConnection(MDTCPSlaveConnection con) {
	    m_Connection = con;
	    m_IO = con.getModbusTransport();
	  }

	  public void setRequest(MDRequest req) {
	    m_Request = req;
	  }

	  public MDRequest getRequest() {
	    return m_Request;
	  }

	  public MDResponse getResponse() {
	    return m_Response;
	  }

	  public int getTransactionID() {
	    return c_TransactionID.get();
	  }

	  public void setCheckingValidity(boolean b) {
	    m_ValidityCheck = b;
	  }

	  public boolean isCheckingValidity() {
	    return m_ValidityCheck;
	  }

	  public void execute() throws ModbusIOException,
	      MDSlaveException,
	      ModbusException {

	    //1. check that the transaction can be executed
	    assertExecutable();

	    try {
	      //2. Lock transaction
	      /**
	       * Note: The way this explicit synchronization is implemented at the moment,
	       * there is no ordering of pending threads. The Mutex will simply call notify()
	       * and the JVM will handle the rest.
	       */
	      m_TransactionLock.acquire();

	      //3. open the connection if not connected
	      if (!m_Connection.isConnected()) {
	    	  throw new ModbusIOException("Connecting is not alive.");
	      }
	      m_IO = m_Connection.getModbusTransport();

	      //4. Retry transaction m_Retries times, in case of
	      //I/O Exception problems.
	      int retryCounter = 0;
	      //toggle and set the id
          m_Request.setTransactionID(c_TransactionID.increment());
          //3. write request, and read response
          m_IO.writeMessage(m_Request);
          //read response message
          m_Response = m_IO.readResponse();

	      //5. deal with "application level" exceptions
	      if (m_Response instanceof ExceptionResponse) {
	        throw new MDSlaveException(
	            ((ExceptionResponse) m_Response).getExceptionMsg()
	        );
	      }

	      //7. Check transaction validity
	      if (isCheckingValidity()) {
	        checkValidity();
	      }

	    } catch (InterruptedException ex) {
	      throw new ModbusIOException("Thread acquiring lock was interrupted.");
	    } finally {
	      m_TransactionLock.release();
	    }
	  }//execute

	  /**
	   * Asserts if this <tt>ModbusTCPTransaction</tt> is
	   * executable.
	   *
	   * @throws ModbusException if the transaction cannot be asserted
	   *                         as executable.
	   */
	  private void assertExecutable()
	      throws ModbusException {
	    if (m_Request == null ||
	        m_Connection == null) {
	      throw new ModbusException(
	          "Assertion failed, transaction not executable"
	      );
	    }
	  }

	  /**
	   * Checks the validity of the transaction, by
	   * checking if the values of the response correspond
	   * to the values of the request.
	   * Use an override to provide some checks, this method will only return.
	   *
	   * @throws ModbusException if this transaction has not been valid.
	   */
	  protected void checkValidity() throws ModbusException {
	  }//checkValidity

}
