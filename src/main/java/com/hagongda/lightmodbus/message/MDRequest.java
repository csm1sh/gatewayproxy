package com.hagongda.lightmodbus.message;

import com.hagongda.lightmodbus.code.GateWayCommandCode;

import net.wimpi.modbus.msg.IllegalFunctionRequest;

public abstract class MDRequest extends MDMessageImpl{

	  /**
	   * Returns the <tt>ModbusResponse</tt> that
	   * correlates with this <tt>ModbusRequest</tt>.
	   * <p>
	   * @return the corresponding <tt>ModbusResponse</tt>.
	   *
	  public abstract ModbusResponse getResponse();
	  */

	  /**
	   * Returns the <tt>ModbusResponse</tt> that
	   * represents the answer to this <tt>ModbusRequest</tt>.
	   * <p>
	   * The implementation should take care about assembling
	   * the reply to this <tt>ModbusRequest</tt>.
	   * <p>
	   * @return the corresponding <tt>ModbusResponse</tt>.
	   */
	  public abstract MDResponse createResponse();

	  /**
	   * Factory method for creating exception responses with the
	   * given exception code.
	   *
	   * @param EXCEPTION_CODE the code of the exception.
	   * @return a ModbusResponse instance representing the exception
	   *         response.
	   */
	  public MDResponse createExceptionResponse(int EXCEPTION_CODE) {
	    ExceptionResponse response =
	        new ExceptionResponse(this.getFunctionCode(), EXCEPTION_CODE);
	      response.setTransactionID(this.getTransactionID());
	    return response;
	  }//createExceptionResponse

	  /**
	   * Factory method creating the required specialized <tt>ModbusRequest</tt>
	   * instance.
	   *
	   * @param functionCode the function code of the request as <tt>int</tt>.
	   * @return a ModbusRequest instance specific for the given function type.
	   */
	  public static MDRequest createModbusRequest(int functionCode) {
	    MDRequest request = null;

	    switch (functionCode) {
	/*      case Modbus.READ_MULTIPLE_REGISTERS:
	        request = new ReadMultipleRegistersRequest();
	        break;
	      case Modbus.READ_INPUT_DISCRETES:
	        request = new ReadInputDiscretesRequest();
	        break;
	      case Modbus.READ_INPUT_REGISTERS:
	        request = new ReadInputRegistersRequest();
	        break;
	      case Modbus.READ_COILS:
	        request = new ReadCoilsRequest();
	        break;
	      case Modbus.WRITE_MULTIPLE_REGISTERS:
	        request = new WriteMultipleRegistersRequest();
	        break;
	      case Modbus.WRITE_SINGLE_REGISTER:
	        request = new WriteSingleRegisterRequest();
	        break;
	      case Modbus.WRITE_COIL:
	        request = new WriteCoilRequest();
	        break;
	      case Modbus.WRITE_MULTIPLE_COILS:
	        request = new WriteMultipleCoilsRequest();
	        break;
	      default:
	        request = new IllegalFunctionRequest(functionCode);
	        break;*/
	    }
	    return request;
	  }//createModbusRequest
	  
	  
	  public static MDRequest createModbusRequest(int commCode , int functionCode) {
		    MDRequest request = null;

		    switch (commCode) {
		     case GateWayCommandCode.AUTH_SERVER:
		        request = new AuthServerRequest();
		        break;
		      default:
		        request = null;
		        break;
		    }
		    return request;
		  }//createModbusRequest
}
