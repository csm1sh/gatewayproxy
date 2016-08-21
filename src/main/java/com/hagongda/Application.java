package com.hagongda;

import java.net.InetAddress;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hagongda.lightmodbus.io.MDTcpListener;

import net.wimpi.modbus.Modbus;

@SpringBootApplication
public class Application {

    private Logger logger = Logger.getLogger(Application.class);
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @PostConstruct
    public void startSouthAPI(){
    	logger.info("starting south API.....");
    	MDTcpListener listener = null;
	    int port = Modbus.DEFAULT_PORT;
	    try {
	      //3. create a listener with 10 threads in pool
	      listener = new MDTcpListener(10, InetAddress.getByName("localhost"));
	      listener.setPort(port);
	      listener.start();
	    } catch (Exception ex) {
	    	logger.fatal("South API listerner failed to starup!!!!!");
	    }
    }
}
