package com.kill3rtaco.tacoapi.api.ncommands;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.tacoapi.api.TacoPermissionHandler;
import com.kill3rtaco.tacoapi.util.ChatUtils;
import com.kill3rtaco.tacoapi.util.PageBuilder;

/**
 * New class to help with command handling. Functionality based off of WorldEdit
 * @author KILL3RTACO
 *
 */
public class CommandManager implements CommandExecutor, TabCompleter {
	
	private JavaPlugin							_plugin;
	private HashMap<Method, String>				_methods			= new HashMap<Method, String>();
	private ArrayList<String>					_registeredCommands	= new ArrayList<String>();
	private ArrayList<TacoPermissionHandler>	_permHandlers		= new ArrayList<TacoPermissionHandler>();
	private HashMap<String, PageBuilder>		_help				= new HashMap<String, PageBuilder>();
	private ArrayList<TabCompleteHook>			_tcHooks;
	
	public CommandManager(JavaPlugin plugin) {
		_plugin = plugin;
	}
	
	/**
	 * Add a permission handler. When a command is run and permission handlers are present,
	 * the CommandManager checks if the registered command has denoted by "prefix:permission"
	 * @param handler The handler to add
	 */
	public void addPermissionHandler(TacoPermissionHandler handler) {
		_permHandlers.add(handler);
	}
	
	protected boolean handlePerm(Player player, String perm) {
		if(player == null || perm.isEmpty() || perm == null) {
			return true;
		}
		for(TacoPermissionHandler h : _permHandlers) {
			String prefix = h.getPrefix() + ":";
			if(perm.startsWith(prefix) && perm.length() > prefix.length()) {
				return h.handlePerm(player, perm.substring(prefix.length()));
			}
		}
		return TacoAPI.getPermAPI().hasPermission(player, perm);
	}
	
	/**
	 * Register a class populated with commands. Each command should be a static method with one of
	 * the following conditions met:
	 * <ul>
	 * 	<li>The method has the annotations "ParentCommand" and "Command"</li>
	 * 	<li>The method has the annotation "SimpleCommand"</li>
	 * </ul>
	 * If neither of the conditions are met, or the method is not static, the method will be silently ignored.
	 * @param clazz The class to register
	 */
	public void reg(Class<?> clazz) {
		for(Method m : clazz.getDeclaredMethods()) {
			if(!Modifier.isStatic(m.getModifiers())) {
				continue;
			}
			boolean parentPresent = m.isAnnotationPresent(ParentCommand.class);
			boolean commandPresent = m.isAnnotationPresent(Command.class);
			boolean simplePresent = m.isAnnotationPresent(SimpleCommand.class);
//			TacoAPI.plugin.chat.out(m.getName() + " -> parent: " + parentPresent + " command: " + commandPresent + " simple: " + simplePresent);
//			TacoAPI.plugin.chat.out("numAnnotatons: " + m.getAnnotations().length + " test - " + (simplePresent || (parentPresent && commandPresent)));
			if(simplePresent || (parentPresent && commandPresent)) {
				String command = simplePresent ? m.getAnnotation(SimpleCommand.class).name() : m.getAnnotation(ParentCommand.class).value();
				if(!commandRegistered(command)) {
					_plugin.getCommand(command).setExecutor(this);
				}
				_methods.put(m, command);
			}
		}
	}
	
	/**
	 * Test whether a command was registered via this CommandManager or not
	 * @param name The command to test
	 * @return Whether /{@code name} was registered via this CommandManager
	 */
	public boolean commandRegistered(String name) {
		for(String s : _registeredCommands) {
			if(s.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	protected ArrayList<Method> getMethodsFor(String parent) {
		ArrayList<Method> methods = new ArrayList<Method>();
		for(Method m : _methods.keySet()) {
			boolean simple = m.isAnnotationPresent(SimpleCommand.class);
			String test;
			if(simple) {
				test = m.getAnnotation(SimpleCommand.class).name();
			} else {
				test = m.getAnnotation(ParentCommand.class).value();
			}
			if(test.equalsIgnoreCase(parent)) {
				methods.add(m);
				if(simple) {
					return methods;
				}
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
		//special case - true if subcommand is an integer
		if(name.equalsIgnoreCase("#") || name.equalsIgnoreCase("[#]") || name.equalsIgnoreCase("<#>")) {
			try {
				Integer.parseInt(alias);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
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
	
	/**
	 * The default bukkit onCommand method
	 */
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		String command = args.length > 0 ? args[0] : "";
		String[] newArgs = removeFirstArg(args);
		for(Method m : getMethodsFor(cmd.getName())) {
			boolean simple = m.isAnnotationPresent(SimpleCommand.class);
			boolean onlyConsole, onlyPlayer;
			String name;
			String[] aliases = null;
			if(simple) {
				SimpleCommand sc = m.getAnnotation(SimpleCommand.class);
				onlyConsole = sc.onlyConsole();
				onlyPlayer = sc.onlyPlayer();
				name = sc.name();
			} else {
				Command c = m.getAnnotation(Command.class);
				onlyConsole = c.onlyConsole();
				onlyPlayer = c.onlyPlayer();
				name = c.name();
				aliases = c.aliases();
			}
			boolean hasAlias = simple ? name.equalsIgnoreCase(cmd.getName()) : hasAlias(command, name, aliases);
			if(hasAlias) {
				if(onlyPlayer && player == null) {
					String msg = new ChatUtils().formatMessage("&cThis command must be run by a player");
					sender.sendMessage(msg);
					return true;
				} else if(onlyConsole && player != null) {
					String msg = new ChatUtils().formatMessage("&cThis command must be run by console");
					player.sendMessage(msg);
					return true;
				}
				try {
					if(m.isAnnotationPresent(CommandPermission.class)
							&& !handlePerm(player, m.getAnnotation(CommandPermission.class).value())) {
						String msg = new ChatUtils().formatMessage("&cYou don't have permission");
						player.sendMessage(msg);
						return true;
					}
					CommandContext cargs = simple ? new CommandContext(sender, cmd.getName(), args) : new CommandContext(sender, command, newArgs);
					m.invoke(null, cargs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		if(command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")) {
			int page = 1;
			if(newArgs.length > 0) {
				try {
					page = Integer.parseInt(newArgs[0]);
				} catch (NumberFormatException e) {
					page = 1;
				}
			}
			showHelp(cmd.getName(), sender, page);
		} else {
			String msg = new ChatUtils().formatMessage("&cCommand \"&e/" + label + " " + command + "&c\" not found");
			sender.sendMessage(msg);
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
		//autocomplete subcommand if needed
		ArrayList<String> possible = new ArrayList<String>();
		String parent = cmd.getName();
		ArrayList<Method> methods = getMethodsFor(parent);
		if(args.length == 1) { //format is /cmd sub{incomplete}
			for(Method m : methods) {
				if(m.isAnnotationPresent(SimpleCommand.class)) {
					continue;
				}
				Command c = m.getAnnotation(Command.class);
				String name = c.name();
				String[] aliases = c.aliases();
				if(name.startsWith(alias)) {
					possible.add(name);
				}
				for(String s : aliases) {
					if(s.startsWith(alias)) {
						possible.add(s);
					}
				}
			}
			TabCompletionLists.sort(possible);
			return possible;
		}
		
		//format is /cmd arg{incomplete} : arg index will never be 0 unless SimpleCommand
		
		return new ArrayList<String>();
	}
	
	/**
	 * Show a help page. 
	 * @param label The command label
	 * @param sender The CommandSender to send the help to
	 * @param page The page to view. If the page given does not exist, page will be set to 1
	 */
	public void showHelp(String label, CommandSender sender, int page) {
		PageBuilder pages = _help.get(label);
		if(pages == null) {
			pages = new PageBuilder("&b/" + label + " Help", "&3");
			ArrayList<Method> methods = getMethodsFor(label);
			String helpLine = "&b/" + label + " &3help&b/&3? [page]&7: &bShows help";
			pages.append(helpLine);
			for(Method m : methods) {
				Command c = m.getAnnotation(Command.class);
				if(c.invisible()) {
					continue;
				}
				if(m.isAnnotationPresent(CommandPermission.class)) {
					if(sender instanceof Player) {
						if(!handlePerm((Player) sender, m.getAnnotation(CommandPermission.class).value())) {
							continue;
						}
					}
				}
				String aliasString = getAliasString(c.name(), c.aliases());
				String line = "&b/" + label;
				if(!aliasString.isEmpty()) {
					line += " &3" + aliasString;
				}
				if(!c.args().isEmpty()) {
					line += " &3" + c.args();
				}
				line += "&7: &b" + c.desc();
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
	
	private String getAliasString(String name, String[] aliases) {
		String str = "&3" + name;
		if(aliases == null || aliases.length == 0) {
			return str;
		}
		for(int i = 0; i < aliases.length; i++) {
			str += "&b/&3" + aliases[i];
			if(i < aliases.length - 1)
				str += "&b/&3";
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
	
	/**
	 * Get the amount of commands registered by this CommandManager
	 * @return How many commands were registered by this CommandManager
	 */
	public int commandsRegistered() {
		return _methods.size();
	}
	
	/**
	 * Add a TabComplete hook. This is a useful method to provide automatic tab completion depending on the
	 * arguments index.
	 * @param parent The parent command (begins with "/")
	 * @param sub The subcommand (empty or null if the command is a SimpleCommand)
	 * @param index The index that the list applies to
	 * @param list A list of all possible strings. The {@link TabCompletionLists} class provides some for you.
	 */
	public void addTabCompleteHook(String parent, String sub, int index, List<String> list) {
		_tcHooks.add(new TabCompleteHook(parent, sub, index, list));
	}
	
	class TabCompleteHook {
		
		private String			_parent, _sub;
		private int				_index;
		private List<String>	_list;
		
		public TabCompleteHook(String parent, String sub, int index, List<String> list) {
			_parent = parent;
			_sub = sub;
			_index = index;
			_list = list;
		}
		
		public String getParent() {
			return _parent;
		}
		
		public String getSub() {
			return _sub;
		}
		
		public int getArgIndex() {
			return _index;
		}
		
		public List<String> getList() {
			return _list;
		}
	}
}
