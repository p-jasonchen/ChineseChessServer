package com.microrapid.chess;

import java.util.Vector;

public class Constants {
	public final static int SERVER_PORT = 33600;
	public final static int PROXY_SERVER_PORT = 33601;
	
	public final static String SERVER_IP = "localhost";
	public final static String PROXY_SERVER_IP = "localhost";
	
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

