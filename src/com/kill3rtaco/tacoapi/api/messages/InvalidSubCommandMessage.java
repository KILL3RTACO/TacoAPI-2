package com.kill3rtaco.tacoapi.api.messages;

import com.kill3rtaco.tacoapi.api.TacoMessage;

/**
 * A message telling the player that a subcommand doesn't exist.
 * @author KILL3RTACO
 *
 */
public class InvalidSubCommandMessage extends TacoMessage {

	public InvalidSubCommandMessage(String attempt){
		this.message = "&cYou entered an &6invalid&c subcommand&7:&6 " + attempt;
	}
	
}
