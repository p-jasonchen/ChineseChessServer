package com.deskapp.websocket;

public class Log {
	public static void i(String tag, String log){
		System.out.println("[" + tag + "]:" + log);
	}
	
	public static void e(String tag, String log){
		System.out.println("[" + tag + "]:" + log);
	}
}
