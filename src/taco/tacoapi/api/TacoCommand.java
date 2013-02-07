package taco.tacoapi.api;

import org.bukkit.entity.Player;

/**
 * Represents a subcommand. It is easy to confuse this with a command. A subcommand is the first argument in a command
 * (e.g. {@code /im send Notch 46 64}) 'send' is the subcommand.
 * @author KILL3RTACO
 * @see TacoCommandHandler
 * @see TacoHelpCommand
 *
 */
public abstract class TacoCommand implements Comparable<TacoCommand> {
	
	private String[] aliases;
	private String name, args, description, permission;
	
	public TacoCommand(String name, String[] aliases, String args, String description, String permission){
		this.name = name;
		this.aliases = aliases;
		this.args = args;
		this.description = description;
		this.permission = permission;
	}
	
	/**
	 * This method is called when a player runs this command (automatic).<br/><br/>
	 * If this command is a command used for chat, (e.g. {@code i have a cake}, whereas this command would send
	 * "i have a cake" globally) then the command needs at least one alias. This alias needs to be "*" so the assocaited
	 * {@link TacoCommandHandler} knows that first argument can be anything. There can only be only one {@link TacoCommand}
	 * with any {@link TacoCommandHandler} with an alias of "*".
	 * @param player The player that typed the command
	 * @param args The arguments of the command
	 * @return {@code false} by default, if false is returned the player will be given.
	 * an {@code {@link InvalidPermissionsMessage InvalidPermissionsMessage}}
	 */
	public abstract boolean onPlayerCommand(Player player, String[] args);
	
	/**
	 * This method is called when someone using the console runs this command (automatic).
	 * This method should return false if this command can't be run from console.<br/><br/>
	 * If this command is a command used for chat, (e.g. {@code i have a cake}, whereas this command would send
	 * "i have a cake" globally) then the command needs at least one alias. This alias needs to be "*" so the assocaited
	 * {@link TacoCommandHandler} knows that first argument can be anything. There can only be only one {@link TacoCommand}
	 * with any {@link TacoCommandHandler} with an alias of "*".
	 * @param args The arguments of the command
	 * @return {@code false} by default, if false is returned a message saying "[PluginName] This can't be 
	 * run from console" will printed to console.
	 */
	public abstract boolean onConsoleCommand(String[] args);
	
	@Override
	public int compareTo(TacoCommand command){
		return this.getName().compareTo(command.getName());
	}
	
	/**
	 * Gets the aliases for this command.
	 * @return all acceptable aliases
	 */
	public String[] getAliases(){
		return aliases;
	}
	
	/**
	 * Gets the name of this command.
	 * @return the original name denoted by this command.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the arguments for this command in the form of a String.
	 * @return the arguments used by this command
	 */
	public String getArgs(){
		return args;
	}
	
	/**
	 * Gets the user-friendly description of this command.
	 * @return this command's user-friendly description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Gets the permission required for this command to be run.
	 * @return the required permission
	 */
	public String getPermission(){
		return permission;
	}
	
	/**
	 * Tests if this command has the alias specified.
	 * @param alias the alias to be tested
	 * @return whether or not the command can be run using the alias given
	 */
	public boolean hasAlias(String alias){
		if(name.equals(alias)) return true;
		for(String s : aliases)
			if(s.equalsIgnoreCase(alias)) return true;
		return false;
	}
	
}
