package com.kill3rtaco.tacoapi.api;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.util.PageBuilder;


/**
 * Represents a command. The name of this class should use the pattern [CommandName]CommandHandler. e.g. if the command in the
 * plugin.yml was 'give' then the class name should be 'GiveCommandHandler'
 * @author KILL3RTACO
 * @see TacoCommand
 * @see TacoHelpCommand
 *
 */
public abstract class TacoCommandHandler implements CommandExecutor{
	
	private String cmdName, description, permission;
	private ArrayList<TacoCommand> commands;
	
	public TacoCommandHandler(String cmdName, String description, String permission){
		this.commands = new ArrayList<TacoCommand>();
		this.cmdName = cmdName;
		this.description = description;
		this.permission = permission;
		registerCommands();
	}

	/**
	 * Method used to register subcommands
	 */
	protected abstract void registerCommands();
	
	
	private void showHelp(Player player, int page){
		Collections.sort(commands);
		PageBuilder help = new PageBuilder("&b" + TacoAPI.getChatUtils().toProperCase(cmdName) + " Help", "&3");
		help.append("&b/" + cmdName + "&7: &b" + description);
		help.append("&b/" + cmdName + " &3<help/?> [page]&7: &bShows help");
		for(TacoCommand tc : commands){
			if(TacoAPI.getPermAPI().hasPermission(player, tc.getPermission()))
				help.append("&b/" + cmdName + " &3" + tc.getName() + " " + tc.getArgs() + "&7: &b" + tc.getDescription());
		}
		help.showPage(player, page);
	}
	
	private void showHelp(CommandSender sender, int page){
		if(sender instanceof Player){
			showHelp((Player) sender, page);
			return;
		}
		Collections.sort(commands);
		PageBuilder help = new PageBuilder("&b" + TacoAPI.getChatUtils().toProperCase(cmdName) + " Help", "&3");
		help.append("&b/" + cmdName + "&7: &b" + description);
		help.append("&b/" + cmdName + " &3<help/?> [page]&7: &bShows help");
		for(TacoCommand tc : commands){
			String delimiter = "&b/&3";
			String aliases = (tc.getAliases().length > 1 ? TacoAPI.getChatUtils().join(tc.getAliases(), delimiter) : "");
			help.append("&b/" + cmdName + " &3" + tc.getName() + aliases  + " " + tc.getArgs() + "&7: &b" + tc.getDescription());
		}
		help.showPage(sender, page);
	}
	
	/**
	 * Registers a subcommand for this command. A subcommand is the first argument in a command
	 * @param command - The TacoCommand to register
	 */
	protected void registerCommand(TacoCommand command){
		commands.add(command);
	}
	
	/**
	 * Default Bukkit onCommand() call. This is used to filter the command being used, and will run if found.
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0){
			if(sender instanceof Player){
				if(TacoAPI.getPermAPI().hasPermission((Player) sender, permission))
					onPlayerCommand((Player) sender);
				else
					TacoAPI.getChatAPI().sendInvalidPermissionsMessage((Player) sender);
			}else{
				if(!onConsoleCommand()){
					TacoAPI.getChatAPI().outNoHeader("&cThis command must be run by a player");
				}
			}
		}else{
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
				if(args.length == 1){ // "help"
					showHelp(sender, 1);
				}else{ // "help [page]"
					try{
						int page = Integer.parseInt(args[1]);
						showHelp(sender, page);
					} catch (NumberFormatException e){
						showHelp(sender, 1);
					}
				}
			}else{
				runCommand(sender, args);
			}
		}
		return true;
	}
	
	private void runCommand(CommandSender sender, String[] args){
		String subcommand = args[0];
		TacoCommand tc = getCommandByAlias(subcommand);
		args = TacoAPI.getChatUtils().removeFirstArg(args);
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(tc != null){
				if(TacoAPI.getPermAPI().hasPermission(player, tc.getPermission()))
					tc.onPlayerCommand(player, args);
				else
					TacoAPI.getChatAPI().sendInvalidPermissionsMessage(player);
			}else{
				TacoAPI.getChatAPI().sendInvalidSubCommandMessage((Player) sender, subcommand);
			}
		}else{
			if(tc != null){
				if(!tc.onConsoleCommand(args)){
					TacoAPI.getChatAPI().outNoHeader("&cThis command must be run by a player");
				}
			}else{
				TacoAPI.getChatAPI().sendInvalidSubCommandMessage(sender, subcommand);
			}
		}
	}
	
	/**
	 * Gets the name of this command.
	 * @return The name of this {@link TacoCommandHandler TacoCommandHandler}
	 */
	public String getName(){
		return cmdName;
	}
	
	/**
	 * Called when this command is run with no arguments by the console.
	 * @return false if this command must be run by a player
	 */
	protected abstract boolean onConsoleCommand();
	
	/**
	 * Called when this command is run with no arguments by a player.
	 */
	protected abstract void onPlayerCommand(Player player);
	
	/**
	 * Get a command by giving an alias
	 * @param alias the alias used to get the TacoCommand
	 * @return the TacoCommand found, or null if there wasn't one
	 */
	public TacoCommand getCommandByAlias(String alias){
		for(TacoCommand tc : commands)
			if(tc.hasAlias(alias)) return tc;
		return null;
	}
}
