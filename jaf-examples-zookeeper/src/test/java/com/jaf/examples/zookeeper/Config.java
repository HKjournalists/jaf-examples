package com.jaf.examples.zookeeper;

/**
 * TODO
 * 
 * @author liaozhicheng
 * @date 2016年7月14日
 * @since 1.0
 */
public class Config {
	
	public static final String ZK_SERVER = "192.168.1.131:2181";

	public static final String ZK_SERVER_CLUSTER = "192.168.1.131:2181,192.168.1.131:2281,192.168.1.131:2381";

    // 默认超时时间 5s
    public static final int DEFUALT_TIME_OUT = 5000;

}