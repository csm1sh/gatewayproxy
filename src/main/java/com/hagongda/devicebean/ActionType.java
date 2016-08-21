package com.hagongda.devicebean;

public interface ActionType {
	public final static Integer ONOFF = 1;
	public final static Integer JOIN_GROUP = 2;
	public final static Integer UPGRADE = 3;
	public final static Integer CHANGE_MODE = 4;
	public final static Integer CHANGE_TYPE = 5;
	public final static Integer CHANGE_DIMMING = 6;
	public final static Integer GET_PROPERTIES = 7;
	public final static Integer GET_NENGHAO = 8;
	public final static Integer CHANGE_TIME = 9;
	
	public int getActionType();
}
