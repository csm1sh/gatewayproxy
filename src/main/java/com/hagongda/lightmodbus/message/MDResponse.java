package com.hagongda.lightmodbus.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.hagongda.lightmodbus.code.GateWayCommandCode;


public abstract class MDResponse  extends MDMessageImpl {


	  /**
	   * Utility method to set the raw data of the message.
	   * Should not be used except under rare circumstances.
	   * <p>
	   * @param msg the <tt>byte[]</tt> resembling the raw modbus
	   *        response message.
	   */
	  protected void setMessage(byte[] msg) {
	    try {
	      readData(
	          new DataInputStream(
	              new ByteArrayInputStream(msg)
	          )
	      );
	    } catch (IOException ex) {

	    }
	  }//setMessage

	  /**
	   * Factory method creating the required specialized <tt>MDResponse</tt>
	   * instance.
	   *
	   * @param functionCode the function code of the response as <tt>int</tt>.
	   * @return a MDResponse instance specific for the given function code.
	   */
	  public static MDResponse createMDResponse(int functionCode) {
	    MDResponse response = null;

	    switch (functionCode) {
	      case GateWayCommandCode.AUTH_GRPS:
	        response = new AuthServerResponse(); break;
	      default:
	        response = new ExceptionResponse();
	    }
	    return response;
	  }//createMDResponse

}
