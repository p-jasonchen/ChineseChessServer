package com.microrapid.chess;

import java.io.IOException;

import org.xlightweb.IWebSocketConnection;
import org.xlightweb.TextMessage;

public interface  AbstractSocketConnection {
	public int writeMessage(String msg);
	public void onMessage(String msg);
}

class WSocketConnection implements AbstractSocketConnection{
	IWebSocketConnection wsCon;
	public WSocketConnection(IWebSocketConnection con){
		this.wsCon = con;
	}
	@Override
	public int writeMessage(String msg) {
		try {
			this.wsCon.writeMessage(new TextMessage(msg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public void onMessage(String msg) {
		
		
	}
}