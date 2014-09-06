package com.kill3rtaco.tacoapi.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.database.Database;
import com.kill3rtaco.tacoapi.database.DatabaseException;
import com.kill3rtaco.tacoapi.database.QueryResults;
import com.kill3rtaco.tacoapi.util.ChatUtils;

/**
 * Class for managing and testing if features for a plugin are disabled or not
 * @author KILL3RTACO
 *
 */
public class DisableManager {
	
	private String					_tableName;
	private static Database			data	= TacoAPI.getDB();
	private Map<String, String>		_messages;
	private Map<String, Boolean>	_disabled;
	
	public DisableManager(String tableName) {
		_tableName = tableName;
		_messages = new HashMap<String, String>();
		_disabled = new HashMap<String, Boolean>();
		createTable();
	}
	
	private void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + _tableName + " (" +
				"`id` INT(16) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
				"`key` VARCHAR(100) NOT NULL UNIQUE," +
				"`disabled` INT(1) NOT NULL DEFAULT 1," +
				"`message` VARCHAR(100) NOT NULL," +
				"`time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
		
		//module - sql
		data.write(sql);
	}
	
	public String getTableName() {
		return _tableName;
	}
	
	public String getDisabledMessage(String key) {
		if(key.isEmpty()) {
			return "";
		}
		if(_disabled.containsKey(key)) {
			return _messages.get(key);
		}
		String sql = "SELECT `message` FROM " + _tableName + " WHERE `key`=? AND `disabled`=?";
		QueryResults q = data.read(sql, key, 1);
		try {
			if(q != null && q.hasRows()) {
				String message = q.getString(0, "message");
				_messages.put(key, message);
			} else {
				_messages.put(key, "");
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return _messages.get(key);
	}
	
	public String getDisabledMessage(String key, String... others) {
		List<String> keys = new ArrayList<String>(Arrays.asList(others));
		keys.add(key);
		for(String k : keys) {
			String message = getDisabledMessage(k);
			if(!message.isEmpty()) {
				return message;
			}
		}
		return "";
	}
	
	public boolean isDisabled(String key, String... others) {
		return !getDisabledMessage(key, others).isEmpty();
	}
	
	public boolean isDisabled(CommandSender sender, String key, String... others) {
		String message = getDisabledMessage(key, others);
		if(!message.isEmpty()) {
			message = new ChatUtils().formatMessage("&c" + message);
			sender.sendMessage(message);
			return true;
		}
		return false;
	}
	
	public boolean isDisabled(CommandContext context, String key, String... others) {
		return isDisabled(context.getSender(), key, others);
	}
	
	public void disable(String key, String message) {
		setDisabled(key, true, message);
	}
	
	public void enable(String key) {
		setDisabled(key, false, "");
	}
	
	private void setDisabled(String key, boolean disabled, String message) {
		int d = disabled ? 1 : 0;
		String sql = "INSERT INTO " + _tableName + " (`key`, `disabled`, `message`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `disabled`=?, `message`=?, `time`=NOW()";
		data.write(sql, key, d, message, d, message);
		_disabled.clear();
		_messages.clear();
	}
	
}
