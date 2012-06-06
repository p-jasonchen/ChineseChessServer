package com.microrapid.chess;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.xlightweb.HttpResponse;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpResponseHeader;
import org.xlightweb.IWebSocketConnection;
import org.xlightweb.IWebSocketHandler;
import org.xlightweb.TextMessage;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;

public class WebSocketProxyServer {
	HttpServer server;
	IWebSocketConnection webSocketConnection;
	PlayerManager playerManager = new PlayerManager();
	HashMap<IWebSocketConnection,WSocketConnection> socketMap = new HashMap<IWebSocketConnection,WSocketConnection>();
	
	public WebSocketProxyServer(){
		try {
			server = new HttpServer(Constants.PROXY_SERVER_PORT, new MyRequestHandler());
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
		
		
		HttpClient httpClient = new HttpClient();             
		httpClient.setConnectTimeoutMillis(60 * 1000);  
		String url = "ws://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT;
		try {
			webSocketConnection = httpClient.openWebSocketConnection(url,"Sample", new WebSocketHandler());
			IHttpResponseHeader responseHeader = webSocketConnection.getUpgradeResponseHeader(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public  static void main(String[] args){
		new WebSocketProxyServer();
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
			WSocketConnection wsCon = new WSocketConnection(webStream);
			socketMap.put(webStream, wsCon);					
			String playerID = playerManager.addPlayerSocketConnection(wsCon);
			
			JSONObject setPlayerIdObj=new JSONObject();
			setPlayerIdObj.put("cmd", ChessProtocal.SET_PLAYER_ID);
			setPlayerIdObj.put("playerID", playerID);
			String jsonString = setPlayerIdObj.toJSONString();
			Log.i(TAG, jsonString);
			webStream.writeMessage(new TextMessage(jsonString));
		}
	
		@Override
		public void onDisconnect(IWebSocketConnection webStream) throws IOException {
			// TODO Auto-generated method stub
			Log.i(TAG, "onDisconnect, id : " + webStream.getId());
			WSocketConnection wsCon = socketMap.get(webStream);
			if(null != wsCon)
				playerManager.removePlayerSocketConnection(wsCon);
		}
	
		@Override
		public void onMessage(IWebSocketConnection webStream) throws IOException {
			TextMessage msg = webStream.readTextMessage();
			String s = msg.toString();
			Log.i(TAG, "onMessage, id : " + webStream.getId() + "\tmessage : " + s);
			if(null != webSocketConnection){
				webSocketConnection.writeMessage(msg);
			}else{
				Log.e(TAG, "webSocketConnection is null");
			}
		}
	 } 
}