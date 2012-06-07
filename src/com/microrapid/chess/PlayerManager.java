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
	PlayerInfo enemy = null;
	public PlayerInfo(String playerId, AbstractSocketConnection socketCon){
		this.playId = playerId;
		this.socketCon = socketCon;
	}
	
	
}

 public class PlayerManager{
	//playerId as key
	private HashMap<String, PlayerInfo> playerInfoMap = new HashMap<String, PlayerInfo>();	
	//playerId as value
	private HashMap<AbstractSocketConnection, String> conPlayerMap = new HashMap<AbstractSocketConnection, String>();
	
	private HashMap<String, PlayerInfo> infoMap = new HashMap<String, PlayerInfo>();
	
	private Vector<String> vPlayerId = Constants.V_PLAYER_ID;
	
	public String addPlayerSocketConnection(AbstractSocketConnection con){
		if(vPlayerId.isEmpty())
			return null;
		String playerID = vPlayerId.remove(0);
		notifyAllPlayers(ChessProtocal.PLAYER_IN, playerID);
		pushAllPlayers(playerID,con);
		conPlayerMap.put(con, playerID);
		playerInfoMap.put(playerID, new PlayerInfo(playerID, con));		
		return playerID;
	}
	public void removePlayerSocketConnection(AbstractSocketConnection con){
		String playerID = conPlayerMap.remove(con);
		if(null != playerID){
			playerInfoMap.remove(playerID);
			vPlayerId.add(playerID);
		}
		notifyAllPlayers(ChessProtocal.PLAYER_OUT, playerID);
		
	}
	
	public AbstractSocketConnection getPlayerSocketConnection(String playerId){
		PlayerInfo info =  playerInfoMap.get(playerId);
		if(null != info) 
			return info.socketCon;
		return null;
	}
	
	public String getPlayerId(AbstractSocketConnection con){
		return conPlayerMap.get(con);
	}
	
	public PlayerInfo getEnemyPlayerInfo(AbstractSocketConnection con){
		String playerId = conPlayerMap.get(con);
		PlayerInfo info = null;
		if( (null != playerId) && null != (info = playerInfoMap.get(playerId)))
			return info.enemy;
		return null;
	}
	
	public AbstractSocketConnection getEnemyAbstractSocketConnection(AbstractSocketConnection con){
		PlayerInfo enemy = getEnemyPlayerInfo(con);
		if(null != enemy)
			return enemy.socketCon;
		return null;
	}
	
	
	
	private void notifyAllPlayers(int cmd, String playerID){
		JSONObject setPlayerIdObj=new JSONObject();
		setPlayerIdObj.put(Constants.CMD, cmd);
		setPlayerIdObj.put(Constants.PLAYER_ID, playerID);
		String jsonString = setPlayerIdObj.toJSONString();		
		
		Collection<PlayerInfo> infos = playerInfoMap.values();
		AbstractSocketConnection con;
		if(null != infos && infos.size() > 0){
			for(PlayerInfo info : infos){
				con = info.socketCon;
				Log.i("notifyAllPlayers: " , "to player " + conPlayerMap.get(con)+ "\t" + jsonString);
				con.writeMessage(jsonString);
			}
		}		
	}
	
	private void pushAllPlayers(String myId, AbstractSocketConnection con){
		JSONObject setPlayerIdObj=new JSONObject();
		setPlayerIdObj.put(Constants.CMD, ChessProtocal.PLAYER_IN);		
		String jsonString;
		Set<String> players = playerInfoMap.keySet();
		if(null != players && players.size() > 0){
			for(String playerId : players){
				setPlayerIdObj.put(Constants.PLAYER_ID, playerId);
				jsonString = setPlayerIdObj.toJSONString();	
				Log.i("pushAllPlayers: ", "for player " + myId +  "\t" + jsonString);
				con.writeMessage(jsonString);
			}
		}		
	}
	public void setEnemy(String myId, String enemyId) {
		PlayerInfo myInfo = playerInfoMap.get(myId);
		PlayerInfo enemyInfo = playerInfoMap.get(enemyId);
		myInfo.enemy = enemyInfo;
		enemyInfo.enemy = myInfo;
		
	}
	
	public void resetEnemy(String myId, String enemyId) {
		PlayerInfo myInfo = playerInfoMap.get(myId);
		PlayerInfo enemyInfo = playerInfoMap.get(enemyId);
		myInfo.enemy = null;
		enemyInfo.enemy = null;
	}
	
	
	
}
