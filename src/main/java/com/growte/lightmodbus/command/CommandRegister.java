package com.growte.lightmodbus.command;

import java.util.HashMap;
import java.util.Map;

import com.growte.devicebean.Action;
import com.growte.lightmodbus.command.BulkConfigCommand;

public class CommandRegister {
	static Map<Integer, String> commands = new HashMap<Integer, String>();
	static {
		commands.put(new Integer(Action.CHANGE_TIME), BulkConfigCommand.class.getName());
	}
	
	public static Writable get(int actionType){
		try {
			return ((Writable)Class.forName(commands.get(new Integer(actionType))).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
