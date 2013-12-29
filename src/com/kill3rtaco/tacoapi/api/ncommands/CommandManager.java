package com.kill3rtaco.tacoapi.api.ncommands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.tacoapi.api.TacoPermissionHandler;
import com.kill3rtaco.tacoapi.util.ChatUtils;
import com.kill3rtaco.tacoapi.util.PageBuilder;

public class CommandManager implements CommandExecutor {
	
	private JavaPlugin							_plugin;
	private HashMap<Method, String>				_methods					= new HashMap<Method, String>();
	private ArrayList<String>					_registeredParentCommands	= new ArrayList<String>();
	private ArrayList<TacoPermissionHandler>	_permHandlers				= new ArrayList<TacoPermissionHandler>();
	private HashMap<String, PageBuilder>		_help						= new HashMap<String, PageBuilder>();
	
	public CommandManager(JavaPlugin plugin) {
		_plugin = plugin;
	}
	
	public void addPermissionHandler(TacoPermissionHandler handler) {
		_permHandlers.add(handler);
	}
	
	public boolean testPerm(Player player, String perm) {
		if(player == null) {
			return true;
		}
		for(TacoPermissionHandler h : _permHandlers) {
			if(h.handlePerm(player, perm)) {
				return true;
			}
		}
		return player.hasPermission(perm);
	}
	
	public void reg(Class<?> clazz) {
		//TacoAPI.plugin.chat.out("Registering commands for Class \"" + clazz.getSimpleName() + "\" ...");
		for(Method m : clazz.getDeclaredMethods()) {
			if(!m.isAnnotationPresent(ParentCommand.class) || !m.isAnnotationPresent(Command.class)) {
				continue;
			}
			String parent = m.getAnnotation(ParentCommand.class).value();
			if(!parentCommandRegistered(parent)) {
				_plugin.getCommand(parent).setExecutor(this);
			}
			_methods.put(m, parent);
			//TacoAPI.plugin.chat.out("Registered /" + parent + " " + m.getAnnotation(Command.class).name());
		}
	}
	
	public boolean parentCommandRegistered(String name) {
		for(String s : _registeredParentCommands) {
			if(s.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Method> getMethodsFor(String parent) {
		ArrayList<Method> methods = new ArrayList<Method>();
		for(Method m : _methods.keySet()) {
			if(m.getAnnotation(ParentCommand.class).value().equalsIgnoreCase(parent)) {
				methods.add(m);
			}
		}
		Collections.sort(methods, new Comparator<Method>() {
			
			@Override
			public int compare(Method m1, Method m2) {
				String name1 = m1.getAnnotation(Command.class).name();
				String name2 = m2.getAnnotation(Command.class).name();
				return name1.compareTo(name2);
			}
			
		});
		return methods;
	}
	
	private boolean hasAlias(String alias, String name, String[] aliases) {
		if(alias.equalsIgnoreCase(name)) {
			return true;
		}
		if(aliases == null) {
			return false;
		}
		for(String s : aliases) {
			if(s.equalsIgnoreCase(alias)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		String command = args.length > 0 ? args[0] : "";
		args = removeFirstArg(args);
		for(Method m : getMethodsFor(label)) {
			Command c = m.getAnnotation(Command.class);
			if(hasAlias(command, c.name(), c.aliases())) {
				if(c.onlyPlayer() && player == null) {
					String msg = new ChatUtils().formatMessage("&cThis command must be run by console");
					sender.sendMessage(msg);
					return true;
				} else if(c.onlyConsole() && player != null) {
					String msg = new ChatUtils().formatMessage("&cThis command cannot be run by a player");
					player.sendMessage(msg);
					return true;
				}
				try {
					if(m.isAnnotationPresent(CommandPermission.class)
							&& !testPerm(player, m.getAnnotation(CommandPermission.class).value())) {
						String msg = new ChatUtils().formatMessage("&cYou don't have permission");
						player.sendMessage(msg);
						return true;
					}
					m.invoke(null, player, new CommandArguments(args));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		if(command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")) {
			showHelp(cmd.getName(), sender, 1);
		} else {
			String msg = new ChatUtils().formatMessage("&cCommand \"&e/" + label + " " + command + "&c\" not found");
			sender.sendMessage(msg);
		}
		return true;
	}
	
	public void showHelp(String label, CommandSender sender, int page) {
		PageBuilder pages = _help.get(label);
		if(pages == null) {
			pages = new PageBuilder("&b/" + label + " Help", "&3");
			ArrayList<Method> methods = getMethodsFor(label);
			String helpLine = "&b/" + label + " &3help&b/&3? [page]&7: &bShows help";
			pages.append(helpLine);
			for(Method m : methods) {
				Command c = m.getAnnotation(Command.class);
				String aliasString = getAliasString(c.name(), c.aliases());
				String line = "&b/" + label;
				if(!aliasString.isEmpty()) {
					line += " &3" + aliasString;
				}
				if(!c.args().isEmpty()) {
					line += " &3" + c.args();
				}
				line += "&7: " + c.desc();
				pages.append(line);
				if(c.name().isEmpty() && !hasCommand(methods, "help")) {
					pages.remove(helpLine); //remove existing help line
					pages.append(helpLine);
				}
			}
			_help.put(label, pages);
		}
		if(!pages.hasPage(page)) {
			page = 1;
		}
		pages.showPage(sender, page);
	}
	
	private boolean hasCommand(ArrayList<Method> methods, String command) {
		for(Method m : methods) {
			Command c = m.getAnnotation(Command.class);
			if(hasAlias(command, c.name(), c.aliases())) {
				return true;
			}
		}
		return false;
	}
	
	public String getAliasString(String name, String[] aliases) {
		String str = "&3" + name;
		if(aliases == null || aliases.length == 0) {
			return str;
		}
		for(int i = 0; i < aliases.length; i++) {
			if(i - 1 < aliases.length) {
				str += aliases[i];
			} else {
				str += "&b/&3" + aliases[i];
			}
		}
		return str;
	}
	
	private String[] removeArgs(String[] array, int startIndex) {
		if(array.length == 0)
			return array;
		if(array.length < startIndex)
			return new String[0];
		
		String[] newSplit = new String[array.length - startIndex];
		System.arraycopy(array, startIndex, newSplit, 0, array.length - startIndex);
		return newSplit;
	}
	
	private String[] removeFirstArg(String[] array) {
		return removeArgs(array, 1);
	}
	
	public int commandsRegistered() {
		return _methods.size();
	}
}
