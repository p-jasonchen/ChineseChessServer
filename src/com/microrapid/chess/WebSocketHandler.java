package com.microrapid.chess;

import java.io.IOException;

import org.xlightweb.BadMessageException;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IWebSocketConnection;
import org.xlightweb.IWebSocketHandler;
import org.xlightweb.TextMessage;

public class WebSocketHandler implements IWebSocketHandler, IHttpRequestHandler {
	
	private final static String TAG = "WebSocketHandler";
	@Override
	public void onConnect(IWebSocketConnection webStream) throws IOException {
		Log.i(TAG, "on connect, id : " + webStream.getId());
	}

	@Override
	public void onDisconnect(IWebSocketConnection webStream) throws IOException {
		Log.e(TAG, "on disconnect, id : " + webStream.getId());
	}

	@Override
	public void onMessage(IWebSocketConnection webStream) throws IOException {
		Log.i(TAG, "on message, id : " + webStream.getId());
		
		TextMessage msg = webStream.readTextMessage();
		Log.i(TAG, msg.toString());
	}

	@Override
	public void onRequest(IHttpExchange exchange) throws IOException, BadMessageException {

	}
	
}
