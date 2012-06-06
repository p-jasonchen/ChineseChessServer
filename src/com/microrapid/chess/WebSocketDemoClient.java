package com.microrapid.chess;

import java.io.IOException;

import org.xlightweb.IHttpResponseHeader;
import org.xlightweb.IWebSocketConnection;
import org.xlightweb.TextMessage;
import org.xlightweb.client.HttpClient;

public class WebSocketDemoClient {
	public WebSocketDemoClient(){
		HttpClient httpClient = new HttpClient();             
		httpClient.setConnectTimeoutMillis(60 * 1000);  
		IWebSocketConnection webSocketConnection;
		String url = "ws://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT;
		try {
			webSocketConnection = httpClient.openWebSocketConnection(url,"Sample", new WebSocketHandler());
			IHttpResponseHeader responseHeader = webSocketConnection.getUpgradeResponseHeader(); 
			webSocketConnection.writeMessage(new TextMessage("hello world"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		
	}
	
	public  static void main(String[] args){
		new WebSocketDemoClient();
	}
}
