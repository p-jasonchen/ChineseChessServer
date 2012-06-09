package com.microrapid.chess;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class Constants {
	public final static int SERVER_PORT = 33601;
	public final static int PROXY_SERVER_PORT = 33601;
	
	
	
	public static String SERVER_IP = "124.73.15.139";
	public static String PROXY_SERVER_IP = "124.73.15.139";

	public final static String CMD = "cmd";
	public final static String PLAYER_ID = "playerId";
	
	public final static Vector<String>  V_PLAYER_ID = new Vector<String>();
	
	static{		
		V_PLAYER_ID.add("玩家1");
		V_PLAYER_ID.add("玩家2");
		V_PLAYER_ID.add("玩家3");
		V_PLAYER_ID.add("玩家4");
		V_PLAYER_ID.add("玩家5");
		V_PLAYER_ID.add("玩家6");
		V_PLAYER_ID.add("玩家7");
		V_PLAYER_ID.add("玩家8");
		V_PLAYER_ID.add("玩家9");
		V_PLAYER_ID.add("玩家10");
		//V_PLAYER_ID.clear();
		
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			PROXY_SERVER_IP = addr.getHostAddress().toString();
			SERVER_IP = PROXY_SERVER_IP;
			String hostname = addr.getHostName().toString();//获得本机名称
			
			Log.i("Constants", "PROXY_SERVER_IP:\t" + PROXY_SERVER_IP + "\tPROXY_NAME:\t" + hostname);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class ChessProtocal{
	public final static int HEART_BEAT = 0;
	public final static int SET_PLAYER_ID  = 1;	
	public final static int MOVE_PIECE = 2;
	public final static int PLAYER_IN = 3;
	public final static int PLAYER_OUT = 4;
	public final static int GAME_REQUEST = 5;	
	public final static int GAME_RESPONSE = 6;	
	
}

