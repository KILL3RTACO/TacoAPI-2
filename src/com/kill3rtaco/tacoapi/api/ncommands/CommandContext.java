package com.kill3rtaco.tacoapi.api.ncommands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.util.ChatUtils;

/**
 * Class containing all information when a command is run. It holds the command sender as well as the arguments.
 * This class also contains utility methods such as sending the CommandSender a message.
 * @author KILL3RTACO
 *
 */
public class CommandContext {
	
	protected CommandSender						_sender;
	protected String[]							_args;
	protected String							_sub;
	protected static HashMap<String, String>	_customMessages;
	
	public CommandContext(CommandSender sender, String sub, String[] args) {
		_sender = sender;
		_sub = sub;
		_args = args;
		_customMessages = new HashMap<String, String>();
	}
	
	/**
	 * Add a custom message that can be sent later.
	 * @param identifier A unique identifier for the message, preferably containing the name of the plugin that
	 * will be using it.
	 * @param message The message. This can either be an unformatted message that can be formatted later via
	 * {@code sendMessageToSender(String message, Object... args)}
	 */
	public static void addMessage(String identifier, String message) {
		_customMessages.put(identifier, message);
	}
	
	/** 
	 * Get the subcommand this CommandContext belongs to. If the command called was a SimpleCommand, then
	 * {@code getSub()} returns the command label
	 * @return The calling command
	 */
	public String getSub() {
		return _sub;
	}
	
	/**
	 * Returns the CommandSender object casted as a player. If the condition {@code sender instanceof Player} is
	 * false, this method returns null
	 * @return The player that sent the command, or null if the sender was not a player
	 */
	public Player getPlayer() {
		if(senderIsPlayer()) {
			return (Player) _sender;
		} else {
			return null;
		}
	}
	
	public String[] getArgs(int start) {
		return TacoAPI.getChatUtils().removeArgs(_args, start);
	}
	
	/**
	 * Returns a player object from the String at {@code index}. This yields exactly the same result as
	 * {@code Bukkit.getPlayer(context.getString(index))}
	 * @param index The index to look
	 * @return
	 */
	public Player getPlayer(int index) {
		return Bukkit.getPlayer(_args[index]);
	}
	
	/**
	 * Get the sender of the command
	 * @return The sender of the command
	 */
	public CommandSender getSender() {
		return _sender;
	}
	
	/**
	 * Gets a Material based on the String located at {@code index}
	 * @param index The index to look
	 * @param def A default Material
	 * @return The Material found
	 */
	@SuppressWarnings("deprecation")
	public Material getMaterial(int index, Material def) {
		if(index >= _args.length) {
			return def;
		} else {
			String s = _args[index];
			try {
				int id = Integer.parseInt(s);
				return Material.getMaterial(id);
			} catch (NumberFormatException e) {
				return Material.getMaterial(s.toUpperCase());
			}
		}
	}
	
	/**
	 * Test whether the command sender was the console
	 * @return If the command sender was the console
	 */
	public boolean senderIsConsole() {
		return !senderIsPlayer();
	}
	
	/**
	 * Test whether the command sender was a player
	 * @return If the command sender was a player
	 */
	public boolean senderIsPlayer() {
		return _sender instanceof Player;
	}
	
	/**
	 * Get the arguments of the command. 
	 * @return
	 */
	public String[] getArgs() {
		return _args;
	}
	
	/**
	 * Tests whether the String at index {@code index} matches any of the given strings, ignoring case
	 * @param index
	 * @param otherStrings
	 * @return
	 */
	public boolean eic(int index, String... otherStrings) {
		if(index > _args.length) {
			return false;
		}
		for(String s : otherStrings) {
			if(s.equalsIgnoreCase(_args[index]))
				return true;
		}
		return false;
	}
	
	public void sendMessageToSender(String message) {
		message = new ChatUtils().formatMessage(message);
		_sender.sendMessage(message);
	}
	
	public static void sendMessageToSender(String identifier, Object... args) {
		if(!_customMessages.containsKey(identifier)) {
			throw new IllegalArgumentException("Identifier \"" + identifier + "\" not found");
		}
		String.format(_customMessages.get(identifier), args);
	}
	
	public void notInteger(int index) {
		sendMessageToSender("&e" + _args[index] + " &cis not an integer");
	}
	
	public String getString(int index) {
		return _args[index];
	}
	
	public String getString(int index, String def) {
		return index < _args.length ? _args[index] : def;
	}
	
	public String getJoinedArgs() {
		return getJoinedArgs(0);
	}
	
	public String getJoinedArgs(int initialIndex) {
		return getJoinedArgs(initialIndex, " ");
	}
	
	public String getJoinedArgs(int initialIndex, String delimiter) {
		String result = "";
		for(int i = initialIndex; i < _args.length; i++) {
			result += _args[i] + delimiter;
		}
		return result.trim();
	}
	
	public int getInteger(int index) throws NumberFormatException {
		return Integer.parseInt(_args[index]);
	}
	
	public int getInteger(int index, int def) throws NumberFormatException {
		return index < _args.length ? Integer.parseInt(_args[index]) : def;
	}
	
	public double getDouble(int index) throws NumberFormatException {
		return Double.parseDouble(_args[index]);
	}
	
	public double getDouble(int index, double def) throws NumberFormatException {
		return index < _args.length ? Double.parseDouble(_args[index]) : def;
	}
	
	public int length() {
		return _args.length;
	}
	
	public boolean gt(int length) {
		return length() > length;
	}
	
	public boolean lt(int length) {
		return length() < length();
	}
	
	public boolean eq(int length) {
		return length() == length;
	}
	
	public boolean lteq(int length) {
		return length() <= length;
	}
	
	public boolean gteq(int length) {
		return length() >= length;
	}
	
	public String getPlayerName() {
		return getPlayer().getName();
	}
	
	public boolean senderHasPermission(String perm) {
		if(_sender instanceof Player) {
			return TacoAPI.getPermAPI().hasPermission((Player) _sender, perm);
		} else {
			return true;
		}
	}
	
}
