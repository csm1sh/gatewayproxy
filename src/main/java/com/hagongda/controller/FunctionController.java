package com.hagongda.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hagongda.lightmodbus.CommandHandle;
import com.hagongda.lightmodbus.GPRSHandlerPool;
import com.hagongda.lightmodbus.MDRequestFactory;
import com.hagongda.lightmodbus.code.GateWayCommandCode;
import com.hagongda.lightmodbus.command.FunctionalCommand;

@RestController
@RequestMapping(value="/v1/{gatewayId}", produces="application/json")
@Scope(value="prototype")
public class FunctionController {
	
	@RequestMapping(value="/test1") 
	public String test(@PathVariable String gatewayId ) throws Exception{
		return OK();
	}
	
	@RequestMapping(value="/light/group", method=RequestMethod.POST) 
	public String group(@PathVariable String gatewayId, @RequestBody String params) throws Exception{
		forward(gatewayId, new FunctionalCommand(GateWayCommandCode.SET_GROUP, params));
		return OK();
	}
	
	@RequestMapping(value="/light/position", method=RequestMethod.POST) 
	public String position(@PathVariable String gatewayId, @RequestBody String params) throws Exception{
		forward(gatewayId, new FunctionalCommand(GateWayCommandCode.SET_LOCATION, params));
		return OK();
	}
	
	@RequestMapping(value="/light/time", method= RequestMethod.POST) 
	public String setTime(@PathVariable String gatewayId, @RequestBody String params) throws Exception{
		forward(gatewayId,  new FunctionalCommand(GateWayCommandCode.SET_TIME, params));
		return OK();
	}
	
	/**读取控制器时间
	 * @param gatewayId
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/light/time", method= RequestMethod.GET) 
	public String getTime(@PathVariable String gatewayId, @RequestBody String params) throws Exception{
		forward(gatewayId,   new FunctionalCommand(GateWayCommandCode.READ_TIME, params));
		return OK();
	}
	
	
	/** 设置分段时间
	 * @param gatewayId
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/light/segMinus", method= RequestMethod.POST) 
	public String setMinus(@PathVariable String gatewayId, @RequestBody String params) throws Exception{
		forward(gatewayId,  new FunctionalCommand(GateWayCommandCode.SET_LOCATION, params));
		return OK();
	}
	
	
	
	private CommandHandle forward(String gatewayId, FunctionalCommand command) throws Exception{
		CommandHandle handler = GPRSHandlerPool.getInstance().get(gatewayId);
		if(handler == null){
			throw new InvalidGatewayException();
		}else if(handler.isBusy()){
			throw new GatewayBusyException();
		}
		MDRequestFactory.getInstacce().buildFrom(command);
		return handler;
	}
	
	@ExceptionHandler(GatewayBusyException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	public String gatewayBusy() {
	    return "{status: 'gateway is busy, request later!'}";
	}
	
	@ExceptionHandler(InvalidGatewayException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String invalidGateWay() {
	    return "{status: 'Invalid Gateway!'}";
	}
	
	private String OK(){
		return "{ status: 'OK'}";
	}
	
	private String FAILED(){
		return "{ status: 'FAILED'}";
	}
	 
}
