package taco.tacoapi.api.messages;

import taco.tacoapi.api.TacoMessage;

/**
 * A message telling the player that a command was given too many arguments.
 * @author KILL3RTACO
 *
 */
public class TooManyArgumentsMessage extends TacoMessage{
	
	public TooManyArgumentsMessage(String usage){
		this.message = "&cToo many arguments&7: &6"+ usage;
	}

}
