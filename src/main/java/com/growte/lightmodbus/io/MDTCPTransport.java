package com.growte.lightmodbus.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.growte.lightmodbus.MDRequestFactory;
import com.growte.lightmodbus.message.MDMessage;
import com.growte.lightmodbus.message.MDRequest;
import com.growte.lightmodbus.message.MDResponse;
import com.growte.lightmodbus.util.MDUtil;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.io.BytesInputStream;

public class MDTCPTransport {

	  //instance attributes
	  private DataInputStream m_Input;	  //input stream
	  private DataOutputStream m_Output;	 //output stream
	  private BytesInputStream m_ByteIn;

	  /**
	   * Constructs a new <tt>ModbusTransport</tt> instance,
	   * for a given <tt>Socket</tt>.
	   * <p>
	   * @param socket the <tt>Socket</tt> used for message transport.
	   */
	  public MDTCPTransport(Socket socket) {
	    try {
	      setSocket(socket);
	    } catch (IOException ex) {
	      if(Modbus.debug) System.out.println("ModbusTCPTransport::Socket invalid.");
	      //@commentstart@
	      throw new IllegalStateException("Socket invalid.");
	      //@commentend@
	    }
	  }//constructor

	  /**
	   * Sets the <tt>Socket</tt> used for message transport and
	   * prepares the streams used for the actual I/O.
	   *
	   * @param socket the <tt>Socket</tt> used for message transport.
	   * @throws IOException if an I/O related error occurs.
	   */
	  public void setSocket(Socket socket) throws IOException {
	    prepareStreams(socket);
	  }//setSocket

	  public void close() throws IOException {
	    m_Input.close();
	    m_Output.close();
	  }//close

	  public void writeMessage(MDMessage msg)
	      throws ModbusIOException {
	    try {
	      msg.writeTo((DataOutput) m_Output);
	      m_Output.flush();
	      //write more sophisticated exception handling
	    } catch (Exception ex) {
	      throw new ModbusIOException("I/O exception - failed to write.");
	    }
	  }//write

	  public MDRequest readRequest()
	      throws ModbusIOException {

	    //System.out.println("readRequest()");
	    try {

	      MDRequest req = null;
	      synchronized (m_ByteIn) {
	        //use same buffer
	        byte[] buffer = m_ByteIn.getBuffer();

	        //read to byte length of message
	        if (m_Input.read(buffer, 0, 6) == -1) {
	          throw new EOFException("Premature end of stream (Header truncated).");
	        }
	        //extract length of bytes following in message
	        int bf = MDUtil.registerToShort(buffer, 4);
	        // extract command code
	        int commCode = MDUtil.byteToInt(buffer, 1);
	        if (Modbus.debug) System.out.println("Request Command code:" + commCode);
	        //
	        //read rest
	        if (m_Input.read(buffer, 6, bf) == -1) {
	          throw new ModbusIOException("Premature end of stream (Message truncated).");
	        }
	        System.out.println(MDUtil.toHex(buffer));
	        // the total length of Package beside crc code;
	        int totalLength = 6 + bf;
	        m_ByteIn.reset(buffer, totalLength);
	        //m_ByteIn.skip(7);
	        //int functionCode = m_ByteIn.readUnsignedByte();
	        m_ByteIn.reset();
	        System.out.println("position:" + m_ByteIn.getPosition());
	        req = MDRequestFactory.getInstacce().buildFrom(commCode);
	        System.out.println("request build done:" + MDUtil.toHex(m_ByteIn.getBuffer()));
	        req.readFrom(m_ByteIn);
	        System.out.println("request build done:" + MDUtil.toHex(buffer));
	      }
	      return req;
	    } catch (EOFException eoex) {
	      throw new ModbusIOException(true);
	    } catch (SocketException sockex) {
	      //connection reset by peer, also EOF
	      throw new ModbusIOException(true);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new ModbusIOException("I/O exception - failed to read.");
	    }
	  }//readRequest

	  public MDResponse readResponse()
	      throws ModbusIOException {
	    //System.out.println("readResponse()");

	    try {

	      MDResponse res = null;
	      synchronized (m_ByteIn) {
	        //use same buffer
	        byte[] buffer = m_ByteIn.getBuffer();

	        //read to byte length of message
	        if (m_Input.read(buffer, 0, 6) == -1) {
	          throw new ModbusIOException("Premature end of stream (Header truncated).");
	        }
	        //extract length of bytes following in message
	        int bf = MDUtil.registerToShort(buffer, 4);
	        
	        // extract command code
	        int commCode = MDUtil.byteToInt(buffer, 1);
	        if (Modbus.debug) System.out.println("Response Command code:" + commCode);
	        
	        int totalLength = 6 + bf;
	        //read rest
	        if (m_Input.read(buffer, 6, bf) == -1) {
	          throw new ModbusIOException("Premature end of stream (Message truncated).");
	        }
	        m_ByteIn.reset(buffer, totalLength);
	        m_ByteIn.reset();
	        res = MDResponse.createMDResponse(commCode); 
	        res.readFrom(m_ByteIn);
	      }
	      return res;
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      throw new ModbusIOException("I/O exception - failed to read.");
	    }
	  }//readResponse

	  /**
	   * Prepares the input and output streams of this
	   * <tt>ModbusTCPTransport</tt> instance based on the given
	   * socket.
	   *
	   * @param socket the socket used for communications.
	   * @throws IOException if an I/O related error occurs.
	   */
	  private void prepareStreams(Socket socket) throws IOException {

	    m_Input = new DataInputStream(
	        new BufferedInputStream(socket.getInputStream())
	    );
	    m_Output = new DataOutputStream(
	        new BufferedOutputStream(socket.getOutputStream())
	    );
	    m_ByteIn = new BytesInputStream(Modbus.MAX_MESSAGE_LENGTH);
	  }//prepareStreams
}
