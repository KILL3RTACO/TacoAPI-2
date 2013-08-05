package com.kill3rtaco.tacoapi.api;

import com.kill3rtaco.tacoapi.TacoAPI;

/**
 * Represents a message that get be sent to a player. When {@code getMessage()} is called, the message is automatically
 * formatted from color codes.
 * @author KILL3RTACO
 *
 */
public abstract class TacoMessage {

	protected String message;
	
	/**
	 * Gets the message for this particular TacoMessage.
	 * @return The message to be displayed
	 */
	public String getMessage(){
		return TacoAPI.getChatUtils().formatMessage(message);
	}
	
	public String toString(){
		return getMessage();
	}
	
}
