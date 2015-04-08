package edu.bupt.rs;

import redis.clients.jedis.Jedis;

public class RedisConnection {
	private String host = "localhost";
	private int port = 6379;
	Jedis jedis = null;
			
	public RedisConnection(){
		jedis = new Jedis(host, port);
	}
	
	public RedisConnection(String host,int port){
		jedis = new Jedis(host, port);
	}
}
