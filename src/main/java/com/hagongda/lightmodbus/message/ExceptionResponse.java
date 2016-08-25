package com.hagongda.lightmodbus.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.wimpi.modbus.Modbus;

public class ExceptionResponse
extends MDResponse {

//instance attributes
private String m_ExceptionMsg = "-1";

/**
* Constructs a new <tt>ExceptionResponse</tt> instance.
*/
public ExceptionResponse() {
}//constructor

/**
* Constructs a new <tt>ExceptionResponse</tt> instance with
* a given function code. Adds the exception offset automatically.
*
* @param fc the function code as <tt>int</tt>.
*/
public ExceptionResponse(int fc) {
    setComm_code(fc);
}//constructor

/**
* Constructs a new <tt>ExceptionResponse</tt> instance with
* a given function code and an exception code. The function
* code will be automatically increased with the exception offset.
*
*
* @param fc the function code as <tt>int</tt>.
* @param exc the exception code as <tt>int</tt>.
*/
public ExceptionResponse(int fc, String msg) {
//exception code, unitid and function code not counted.
setDataLength(1);
setComm_code(fc);
m_ExceptionMsg = msg;
}//constructor

/**
* Returns the Modbus exception code of this
* <tt>ExceptionResponse</tt>.
* <p>
* @return the exception code as <tt>int</tt>.
*/
public String getExceptionMsg() {
return m_ExceptionMsg;
}//getExceptionCode

public void writeData(DataOutput dout)
  throws IOException {
dout.writeBytes(getExceptionMsg());
}//writeData

public void readData(DataInput din)
  throws IOException {
    byte[] msg = new byte[this.getDataLength()];
 din.readFully(msg);
 m_ExceptionMsg = new String(msg);
}//readData

@Override
public String getParamBody()
{
    // TODO Auto-generated method stub
    return m_ExceptionMsg;
}

}//ExceptionResponse
