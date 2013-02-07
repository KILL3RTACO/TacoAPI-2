package taco.tacoapi.api.messages;

import taco.tacoapi.api.TacoMessage;

/**
 * A message telling the player that a player doesn't exist or isn't online
 * @author KILL3RTACO
 *
 */
public class PlayerNotOnlineMessage extends TacoMessage {

	public PlayerNotOnlineMessage(String player){
		this.message = "&cPlayer &6" + player + " &c does not exist or is not online";
	}
	
}
