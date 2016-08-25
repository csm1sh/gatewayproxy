package com.hagongda.devicebean;

import com.hagongda.lightmodbus.util.Json2Object;

public class User
{
private String user;
private String md5_passwd;
public String getUser()
{
    return user;
}
public void setUser(String user)
{
    this.user = user;
}
public String getMd5_passwd()
{
    return md5_passwd;
}
public void setMd5_passwd(String md5_passwd)
{
    this.md5_passwd = md5_passwd;
}


}
