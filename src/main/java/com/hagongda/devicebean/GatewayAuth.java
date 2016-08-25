package com.hagongda.devicebean;

import java.io.Serializable;

import com.hagongda.lightmodbus.util.Json2Object;

public class GatewayAuth implements Serializable
{
    private static final long serialVersionUID = -2094545719705126806L;
    
    private String            phoneNum             = "18563232234";
    private String            md5_passwd       = "4077C71671EA79B00C0EA05A6AE9EE3F";

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getMd5_passwd()
    {
        return md5_passwd;
    }

    public void setMd5_passwd(String md5_passwd)
    {
        this.md5_passwd = md5_passwd;
    }

    public String toJson()
    {
        return Json2Object.object2Json(this);
    }
    
    public static String Ok(){
        return "{status: 'OK'}";
    }
    
    public static String Failed(){
        return "{status: 'FAILED'}";
    }
}
