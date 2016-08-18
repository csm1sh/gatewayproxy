package com.growte.lightmodbus.code;

public interface ModBusCommCode {
  int AUTH_SERVER = 0x01;
  int AUTH_GRPS = 0x02;
  int HEART_BEAT = 0x3;
  int COMM_REREDIRECT = 0x10;
  int ALARM = 0x44; // it is unknow
  int BULK_CONFIG = 0x11;
}
