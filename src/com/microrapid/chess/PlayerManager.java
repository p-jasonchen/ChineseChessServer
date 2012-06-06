package com.microrapid.chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONObject;

class PlayerInfo {
	String playId;
	String nickName;
	AbstractSocketConnection socketCon;
	
}

class RoundInfo{
	
}

 public class PlayerManager{
	//playerId as key
	HashMap<String, AbstractSocketConnection> playerConMap = new HashMap<String, AbstractSocketConnection>();	
	//playerId as value
	HashMap<AbstractSocketConnection, String> conPlayerMap = new HashMap<AbstractSocketConnection, String>();
	
	HashMap<String, PlayerInfo> infoMap = new HashMap<String, PlayerInfo>();
	
	Vector<String> vPlayerId = Constants.V_PLAYER_ID;
	
	public String addPlayerSocketConnection(AbstractSocketConnection con){
		String playerID = vPlayerId.remove(0);
		notifyAllPlayers(ChessProtocal.PLAYER_IN, playerID);
		pushAllPlayers(playerID,con);
		conPlayerMap.put(con, playerID);
		playerConMap.put(playerID, con);
		return playerID;
	}
	public void removePlayerSocketConnection(AbstractSocketConnection con){
		String playerID = conPlayerMap.remove(con);
		if(null != playerID){
			playerConMap.remove(playerID);
			vPlayerId.add(playerID);
		}
		notifyAllPlayers(ChessProtocal.PLAYER_OUT, playerID);
		
	}
	
	public AbstractSocketConnection getPlayerSocketConnection(String playId){
		return playerConMap.get(playId);
	}
	
	private void notifyAllPlayers(int cmd, String playerID){
		JSONObject setPlayerIdObj=new JSONObject();
		setPlayerIdObj.put("cmd", cmd);
		setPlayerIdObj.put("playerID", playerID);
		String jsonString = setPlayerIdObj.toJSONString();		
		
		Collection<AbstractSocketConnection> cons = playerConMap.values();
		if(null != cons && cons.size() > 0){
			for(AbstractSocketConnection con : cons){
				Log.i("notifyAllPlayers: " , "to player " + conPlayerMap.get(con)+ "\t" + jsonString);
				con.writeMessage(jsonString);
			}
		}		
	}
	
	private void pushAllPlayers(String myId, AbstractSocketConnection con){
		JSONObject setPlayerIdObj=new JSONObject();
		setPlayerIdObj.put("cmd", ChessProtocal.PLAYER_IN);		
		String jsonString;
		Set<String> players = playerConMap.keySet();
		if(null != players && players.size() > 0){
			for(String playerId : players){
				setPlayerIdObj.put("playerID", playerId);
				jsonString = setPlayerIdObj.toJSONString();	
				Log.i("pushAllPlayers: ", "for player " + myId +  "\t" + jsonString);
				con.writeMessage(jsonString);
			}
		}		
	}
	
	
	
}
