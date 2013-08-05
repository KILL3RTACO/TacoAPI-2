package com.kill3rtaco.tacoapi.obj;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.TacoMessage;
import com.kill3rtaco.tacoapi.api.messages.InvalidPermissionsMessage;
import com.kill3rtaco.tacoapi.api.messages.InvalidSubCommandMessage;


/**
 * Class to help with chatting
 * @author KILL3RTACO
 *
 */
public class ChatObject {

	private String name;
	private ConsoleCommandSender console;
	
	public ChatObject(String name){
		this.name = name;
		this.console = Bukkit.getServer().getConsoleSender();
	}
	
	public void sendInvalidPermissionsMessage(Player player){
		sendPlayerMessageNoHeader(player, new InvalidPermissionsMessage());
	}
	
	public void sendInvalidSubCommandMessage(Player player, String attempt){
		sendPlayerMessageNoHeader(player, new InvalidSubCommandMessage(attempt));
	}
	
	public void sendInvalidSubCommandMessage(CommandSender sender, String attempt){
		if(sender instanceof Player){
			sendInvalidSubCommandMessage((Player) sender, attempt);
			return;
		}
		outNoHeader(new InvalidSubCommandMessage(attempt));
	}
	
	public void sendPlayerMessage(Player player, String message){
		message = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f " + message);
		player.sendMessage(message);
	}
	
	public void sendPlayerMessageNoHeader(Player player, String message){
		message = TacoAPI.getChatUtils().formatMessage(message);
		player.sendMessage(message);
	}
	
	public void sendPlayerMessage(Player player, TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f " + message);
		player.sendMessage(msg);
	}
	
	public void sendPlayerMessageNoHeader(Player player, TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage(message.getMessage());
		player.sendMessage(msg);
	}
	
	public void sendGlobalMessage(String message){
		message = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f " + message);
		TacoAPI.plugin.getServer().broadcastMessage(message);
	}
	
	public void sendGlobalMessageNoHeader(String message){
		message = TacoAPI.getChatUtils().formatMessage(message);
		TacoAPI.plugin.getServer().broadcastMessage(message);
	}
	
	public void sendGlobalMessage(TacoMessage message){
		String msg = TacoAPI.getChatUtils().formatMessage("&7[&9" + name + "&7]&f " + message);
		TacoAPI.plugin.getServer().broadcastMessage(msg);
	}
	
	public void sendGlobalMessageNoHeader(TacoMessage message){
		TacoAPI.plugin.getServer().broadcastMessage(message.getMessage());
	}
	
	public void out(String message){
		outNoHeader("[" + name + "] " + message);
	}
	
	public void outNoHeader(String message){
		outNoHeader(message, Level.INFO);
	}
	
	public void outNoHeader(String message, Level logLevel){
		if(console == null) console = Bukkit.getServer().getConsoleSender();
		if(console != null){
			message = TacoAPI.getChatUtils().formatMessage(message);
			console.sendMessage(message);
		}else{
			message = TacoAPI.getChatUtils().removeColorCodes(message);
			Logger logger = Logger.getLogger("Minecraft");
			logger.log(logLevel, message);
		}
	}
	
	public void outSevere(String message){
		outNoHeader("&4[" + name + "] &r" + message, Level.SEVERE);
	}
	
	public void outWarn(String message){
		outNoHeader("&e[" + name + "] &r" + message, Level.WARNING);
	}
	
	public void out(TacoMessage message){
		outNoHeader(message);
	}
	
	public void outNoHeader(TacoMessage message){
		outNoHeader(message.getMessage());
	}
	
	public void outSevere(TacoMessage message){
		outSevere(message.getMessage());
	}
	
	public void outWarn(TacoMessage message){
		outWarn(message.getMessage());
	}

}
