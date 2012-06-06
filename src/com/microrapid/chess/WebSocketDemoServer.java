package com.microrapid.chess;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xlightweb.HttpResponse;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IWebSocketConnection;
import org.xlightweb.IWebSocketHandler;
import org.xlightweb.TextMessage;
import org.xlightweb.server.HttpServer;

public class WebSocketDemoServer {
	
	public WebSocketDemoServer(){
		

		 
		  // creates a server
		HttpServer server;
		try {
			server = new HttpServer("localhost", Constants.SERVER_PORT, new MyRequestHandler());
			// setting some properties 
			server.setMaxTransactions(400);
			server.setRequestTimeoutMillis(5 * 60 * 1000);  
			 
			// executing the server 
			server.start();  // (blocks forever)
			 
			  // or server.start();  (blocks until the server has been started)
			  //...
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
	}
	
	
	public  static void main(String[] args){
		new WebSocketDemoServer();
	}


	class MyRequestHandler implements IHttpRequestHandler, IWebSocketHandler   {
		private final static String TAG = "MyRequestHandler";
		
	    public void onRequest(IHttpExchange exchange) throws IOException {
	       IHttpRequest request = exchange.getRequest();       
	       exchange.send(new HttpResponse(200, "text/plain", "it works"));
	    }
	
		@Override
		public void onConnect(IWebSocketConnection webStream) throws IOException {
			// TODO Auto-generated method stub
			Log.i(TAG, "onconnect, id : " + webStream.getId());
		}
	
		@Override
		public void onDisconnect(IWebSocketConnection webStream) throws IOException {
			// TODO Auto-generated method stub
			Log.i(TAG, "onDisconnect, id : " + webStream.getId());
		}
	
		@Override
		public void onMessage(IWebSocketConnection webStream) throws IOException {
			TextMessage msg = webStream.readTextMessage();
			String s = msg.toString();
			Log.i(TAG, "onMessage, id : " + webStream.getId() + "\tmessage : " + s);
		}
	 } 
}
