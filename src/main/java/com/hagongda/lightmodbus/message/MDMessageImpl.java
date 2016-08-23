package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.hagongda.lightmodbus.util.MDUtil;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.BytesOutputStream;

public abstract class MDMessageImpl implements MDMessage{
	//instance attributes
	  private int m_TransactionID = Modbus.DEFAULT_TRANSACTION_ID;
	  private int m_DataLength;
	  private int comm_code;

	 public int getComm_code() {
		return comm_code;
	}

	public void setComm_code(int comm_code) {
		this.comm_code = comm_code;
	}

	public int getTransactionID() {
	    return m_TransactionID;
	  }//getTransactionID

	  /**
	   * Sets the transaction identifier of this
	   * <tt>ModbusMessage</tt>.<p>
	   * The identifier should be a 2-byte (short) non negative
	   * integer value valid in the range of 0-65535.<br>
	   * <p>
	   * @param tid the transaction identifier as <tt>int</tt>.
	   */
	  public void setTransactionID(int tid) {
	    m_TransactionID = tid;
	    //setChanged(true);
	  }//setTransactionID

	  public int getDataLength() {
	    return m_DataLength;
	  }//getDataLength

	  /**
	   * Sets the length of the data appended
	   * after the protocol header.<p>
	   * Note that this library, a bit in contrast to the
	   * specification, counts the unit identifier and the
	   * function code to the header, because it is part
	   * of each and every message. Thus this message will
	   * append two (2) to the passed in integer value.
	   * <p>
	   * @param length the data length as <tt>int</tt>.
	   */
	  public void setDataLength(int length) {
	    //should be below 255, check!
	    m_DataLength = length;
	  }//setData



	  /*** Data ********************************************/

	  /*** Transportable ***********************************/

	  /**
	   * Writes this message to the given <tt>DataOutput</tt>.
	   *
	   * @param dout a <tt>DataOutput</tt> instance.
	   * @throws IOException if an I/O related error occurs.
	   */
	  public void writeTo(DataOutput dout)
	      throws IOException {
		dout.writeByte(0xFA);
		dout.writeByte(this.getComm_code());
	    dout.writeShort(getTransactionID());
	    dout.writeShort(getDataLength());
	    writeData(dout);
	    dout.writeByte(calCrc());
	  }//writeTo
	  
	  protected  int calCrc() throws IOException{
		  BytesOutputStream m_ByteOut =
			      new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
		  m_ByteOut.writeByte(0xFA);
		  m_ByteOut.writeByte(this.getComm_code());
		  m_ByteOut.writeShort(getTransactionID());
		  m_ByteOut.writeShort(getDataLength());
		  writeData(m_ByteOut);
		  byte[] data = m_ByteOut.getBuffer();
		  int crc = MDUtil.calculateCRC(data);
		  return crc;

	  }

	  /**
	   * Writes the subclass specific data to the given DataOutput.
	   *
	   * @param dout the DataOutput to be written to.
	   * @throws IOException if an I/O related error occurs.
	   */
	  public abstract void writeData(DataOutput dout)
	      throws IOException;

	  public void readFrom(DataInput din)
	      throws IOException {
		  din.readByte();
		  this.setComm_code(din.readByte());
	      setTransactionID(din.readUnsignedShort());
	      m_DataLength = din.readUnsignedShort();
	    //setFunctionCode(din.readUnsignedByte());
	    readData(din);
	  }//readFrom

	  /**
	   * Reads the subclass specific data from the given DataInput instance.
	   *
	   * @param din the DataInput to read from.
	   * @throws IOException if an I/O related error occurs.
	   */
	  public abstract void readData(DataInput din)
	      throws IOException;

	  public int getOutputLength() {
	    int l = 2 + getDataLength();
	    l = l + 6;
	    return l;
	  }//getOutputLength

	  /*** END Transportable *******************************/

	  /**
	   * Returns the this message as hexadecimal string.
	   *
	   * @return the message as hex encoded string.
	   */
	  public String getHexMessage() {
	    return MDUtil.toHex(this);
	  }//getHexMessage
	  
	   public String getMessage() {
	        return MDUtil.toString(this);
	      }//getHexMessage
}
