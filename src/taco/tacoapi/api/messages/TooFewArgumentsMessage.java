package taco.tacoapi.api.messages;

import taco.tacoapi.api.TacoMessage;

/**
 * A message telling the player that a command was given too few arguments.
 * @author KILL3RTACO
 *
 */
public class TooFewArgumentsMessage extends TacoMessage{
	
	public TooFewArgumentsMessage(String usage){
		this.message = "&cToo few arguments&7: &6"+ usage;
	}

}
