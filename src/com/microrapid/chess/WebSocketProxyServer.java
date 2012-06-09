package com.microrapid.chess;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
			server = new HttpServer(Constants.PROXY_SERVER_IP, Constants.PROXY_SERVER_PORT, new MyRequestHandler());
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
		
		/*
		HttpClient httpClient = new HttpClient();             
		httpClient.setConnectTimeoutMillis(60 * 1000);  
		String url = "ws://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT;
		try {
			webSocketConnection = httpClient.openWebSocketConnection(url,"Sample", new WebSocketHandler());
			IHttpResponseHeader responseHeader = webSocketConnection.getUpgradeResponseHeader(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/  
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
			setPlayerIdObj.put(Constants.CMD, ChessProtocal.SET_PLAYER_ID);
			setPlayerIdObj.put(Constants.PLAYER_ID, playerID);
			String jsonString = setPlayerIdObj.toJSONString();
			Log.i(TAG, jsonString);
			webStream.writeMessage(new TextMessage(jsonString));
			if(null == playerID){
				webStream.close();
			}
		}
	
		@Override
		public void onDisconnect(IWebSocketConnection webStream) throws IOException {
			// TODO Auto-generated method stub
			Log.i(TAG, "onDisconnect, id : " + webStream.getId());
			WSocketConnection wsCon = socketMap.get(webStream);
			if(null != wsCon){
				playerManager.removePlayerSocketConnection(wsCon);
				socketMap.remove(webStream);
			}
		}
	
		@Override
		public void onMessage(IWebSocketConnection webStream) throws IOException {
			TextMessage msg = webStream.readTextMessage();
			String s = msg.toString();
			Log.i(TAG, "onMessage, id : " + webStream.getId() + "\tmessage : " + s);
			WSocketConnection myCon = socketMap.get(webStream);
			String myId = playerManager.getPlayerId(myCon);			
			JSONObject jsonObj = (JSONObject) JSONValue.parse(s);
			if(null ==jsonObj)
				return;
			Long cmdLong =  (Long)jsonObj.get(Constants.CMD);
			if(null == cmdLong)
				return;
			long cmd = cmdLong;
			switch((int)cmd){
				case ChessProtocal.GAME_REQUEST:{
					String playerId = (String) jsonObj.get(Constants.PLAYER_ID);					
					AbstractSocketConnection con = playerManager.getPlayerSocketConnection(playerId);
					if(null != con){						
						jsonObj.put(Constants.PLAYER_ID, myId);
						String jsonString = jsonObj.toJSONString();
						con.writeMessage(jsonString);
						playerManager.setEnemy(myId, playerId);
					}
					break;
				}
				case ChessProtocal.GAME_RESPONSE:{
					String playerId = (String) jsonObj.get(Constants.PLAYER_ID);					
					AbstractSocketConnection con = playerManager.getPlayerSocketConnection(playerId);
					if(null != con){					
						String jsonString = jsonObj.toJSONString();
						con.writeMessage(jsonString);
						long result =  (Long)(jsonObj.get("result"));
						if(result == 0){
							playerManager.resetEnemy(myId, playerId);
						}
					}
					break;
				}
					
				case ChessProtocal.MOVE_PIECE:{
					AbstractSocketConnection enemyCon = playerManager.getEnemyAbstractSocketConnection(myCon);
					if(null != enemyCon){
						String jsonString = jsonObj.toJSONString();
						Log.i(TAG, "writeMessage : " + s);
						enemyCon.writeMessage(jsonString);
					}
					break;
				}
				case ChessProtocal.HEART_BEAT:{
					Log.i(TAG, "HEART_BEAT packet from : " + myId);break;
				}
			}
			
			
			
			/*
			if(null != webSocketConnection){
				webSocketConnection.writeMessage(msg);
			}else{
				Log.e(TAG, "webSocketConnection is null");
			}*/
		}
	 } 
}
