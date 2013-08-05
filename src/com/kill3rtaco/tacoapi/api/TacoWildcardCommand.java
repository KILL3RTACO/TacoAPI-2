package com.kill3rtaco.tacoapi.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;


/**
 * TacoWilcardCommands are like TacoCommandHandlers, they are split between whether a player ran the command or
 * the console did. These are used when the subcommand can be anything, or nothing at all.
 * @author KILL3RTACO
 *
 */
public abstract class TacoWildcardCommand implements CommandExecutor {

	public String cmdName, arguments, description, permission;
	
	public TacoWildcardCommand(String commandName, String arguments, String description, String permission){
		this.cmdName = commandName;
		this.arguments = arguments;
		this.description = description;
		this.permission = permission;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player;
		if(sender instanceof Player){
			player = (Player) sender;
			if(permission == null || permission.equalsIgnoreCase("") || player.hasPermission(permission)){
				if(args.length == 0){
					onPlayerCommand(player);
				}else{
					String subcommand = args[0];
					if(subcommand.equalsIgnoreCase("?") || subcommand.equalsIgnoreCase("help")){
						String command = "&b/" + cmdName + " &3" + args + "&7: &b" + description;
						TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, command);
					}else{
						args = TacoAPI.getChatUtils().removeFirstArg(args);
						onPlayerCommand(player, subcommand, args);
					}
				}
			}else{
				TacoAPI.getChatAPI().sendInvalidPermissionsMessage(player);
			}
		}else{
			if(args.length == 0){
				onConsoleCommand();
			}else{
				String subcommand = args[0];
				args = TacoAPI.getChatUtils().removeFirstArg(args);
				onConsoleCommand(subcommand, args);
			}
		}
		return true;
	}
	
	/**
	 * Called when the console runs this command with no arguments
	 */
	public abstract void onConsoleCommand();
	
	/**
	 * Called when the console runs this command
	 * @param subcommand The first argument
	 * @param args The rest of the command
	 */
	public abstract void onConsoleCommand(String subcommand, String[] args);
	
	/**
	 * Called when a player runs this command with no arguments
	 * @param player The player that ran the command
	 */
	public abstract void onPlayerCommand(Player player);
	
	/**
	 * Called when a player runs this command
	 * @param player The player that ran the command
	 * @param subcommand the first argument
	 * @param args The rest of the command
	 */
	public abstract void onPlayerCommand(Player player, String subcommand, String[] args);
	
	public String getName(){
		return cmdName;
	}
	
}
