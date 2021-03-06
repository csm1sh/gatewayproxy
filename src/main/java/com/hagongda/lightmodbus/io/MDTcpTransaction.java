package com.hagongda.lightmodbus.io;

import com.hagongda.lightmodbus.exceptions.MDSlaveException;
import com.hagongda.lightmodbus.message.ExceptionResponse;
import com.hagongda.lightmodbus.message.MDRequest;
import com.hagongda.lightmodbus.message.MDResponse;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.util.AtomicCounter;
import net.wimpi.modbus.util.Mutex;

public class MDTcpTransaction {

	  //class attributes
	  private static AtomicCounter c_TransactionID =
	      new AtomicCounter(Modbus.DEFAULT_TRANSACTION_ID);

	  //instance attributes and associations
	  private MDTCPMasterConnection m_Connection;
	  private MDTCPTransport m_IO;
	  private MDRequest m_Request;
	  private MDResponse m_Response;
	  private boolean m_ValidityCheck =
	      Modbus.DEFAULT_VALIDITYCHECK;
	  private boolean m_Reconnecting = Modbus.DEFAULT_RECONNECTING;
	  private int m_Retries = Modbus.DEFAULT_RETRIES;

	  private Mutex m_TransactionLock = new Mutex();

	  /**
	   * Constructs a new <tt>ModbusTCPTransaction</tt>
	   * instance.
	   */
	  public MDTcpTransaction() {
	  }//constructor

	  /**
	   * Constructs a new <tt>ModbusTCPTransaction</tt>
	   * instance with a given <tt>MDRequest</tt> to
	   * be send when the transaction is executed.
	   * <p/>
	   *
	   * @param request a <tt>MDRequest</tt> instance.
	   */
	  public MDTcpTransaction(MDRequest request) {
	    setRequest(request);
	  }//constructor

	  /**
	   * Constructs a new <tt>ModbusTCPTransaction</tt>
	   * instance with a given <tt>TCPMasterConnection</tt> to
	   * be used for transactions.
	   * <p/>
	   *
	   * @param con a <tt>TCPMasterConnection</tt> instance.
	   */
	  public MDTcpTransaction(MDTCPMasterConnection con) {
	    setConnection(con);
	  }//constructor

	  /**
	   * Sets the connection on which this <tt>ModbusTransaction</tt>
	   * should be executed.<p>
	   * An implementation should be able to
	   * handle open and closed connections.<br>
	   * <p/>
	   *
	   * @param con a <tt>TCPMasterConnection</tt>.
	   */
	  public void setConnection(MDTCPMasterConnection con) {
	    m_Connection = con;
	    m_IO = con.getModbusTransport();
	  }//setConnection

	  public void setRequest(MDRequest req) {
	    m_Request = req;
	  }//setRequest

	  public MDRequest getRequest() {
	    return m_Request;
	  }//getRequest

	  public MDResponse getResponse() {
	    return m_Response;
	  }//getResponse

	  public int getTransactionID() {
	    return c_TransactionID.get();
	  }//getTransactionID

	  public void setCheckingValidity(boolean b) {
	    m_ValidityCheck = b;
	  }//setCheckingValidity

	  public boolean isCheckingValidity() {
	    return m_ValidityCheck;
	  }//isCheckingValidity

	  /**
	   * Sets the flag that controls whether a
	   * connection is openend and closed for
	   * <b>each</b> execution or not.
	   * <p/>
	   *
	   * @param b true if reconnecting, false otherwise.
	   */
	  public void setReconnecting(boolean b) {
	    m_Reconnecting = b;
	  }//setReconnecting

	  /**
	   * Tests if the connection will be openend
	   * and closed for <b>each</b> execution.
	   * <p/>
	   *
	   * @return true if reconnecting, false otherwise.
	   */
	  public boolean isReconnecting() {
	    return m_Reconnecting;
	  }//isReconnecting

	  public int getRetries() {
	    return m_Retries;
	  }//getRetries

	  public void setRetries(int num) {
	    m_Retries = num;
	  }//setRetries

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
	        try {
	          m_Connection.connect();
	          m_IO = m_Connection.getModbusTransport();
	        } catch (Exception ex) {
	          throw new ModbusIOException("Connecting failed.");
	        }
	      }

	      //4. Retry transaction m_Retries times, in case of
	      //I/O Exception problems.
	      int retryCounter = 0;

	      while (retryCounter <= m_Retries) {
	        try {
	          //toggle and set the id
	          m_Request.setTransactionID(c_TransactionID.increment());
	          //3. write request, and read response
	          m_IO.writeMessage(m_Request);
	          //read response message
	          m_Response = m_IO.readResponse();
	          break;
	        } catch (ModbusIOException ex) {
	          if (retryCounter == m_Retries) {
	            throw new ModbusIOException("Executing transaction failed (tried " + m_Retries + " times)");
	          } else {
	            retryCounter++;
	            continue;
	          }
	        }
	      }

	      //5. deal with "application level" exceptions
	      if (m_Response instanceof ExceptionResponse) {
	        throw new MDSlaveException(
	            ((ExceptionResponse) m_Response).getExceptionMsg()
	        );
	      }

	      //6. close connection if reconnecting
	      if (isReconnecting()) {
	        m_Connection.close();
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
	  }//assertExecuteable

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
