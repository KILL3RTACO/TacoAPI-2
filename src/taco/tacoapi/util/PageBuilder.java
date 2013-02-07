package taco.tacoapi.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import taco.tacoapi.TacoAPI;

/**
 * A class to help make page managing easier.
 * @author KILL3RTACO
 *
 */
public class PageBuilder {

	private ArrayList<String> elements;
	private int pages = 0, elementsPerPage = 5;
	private String title;
	private ChatColor titleContainer = ChatColor.WHITE;
	
	public PageBuilder() {
		elements = new ArrayList<String>();
	}
	
	/**
	 * Add a line.
	 * @param element The element to add
	 */
	public void append(String element){
		elements.add(element);
		if(pages == 0){
			pages++;
			return;
		}
		pages = elements.size() / elementsPerPage + (elements.size() % elementsPerPage != 0 ? 1 : 0);
	}
	
	/**
	 * Tests if there are no pages.
	 * @return true if there are no pages
	 */
	public boolean hasNoPages(){
		return pages == 0;
	}
	
	/**
	 * Test if the page exists.
	 * @param page
	 * @return true if the page exists
	 */
	public boolean hasPage(int page){
		return page > 0 && page <= pages;
	}
	
	/**
	 * Remove a line.
	 * @param element - The line to be removed
	 */
	public void remove(String element){
		if(elements.contains(element)) elements.remove(element);
	}
	
	/**
	 * Show a page to a player
	 * @param player - The player to send to
	 * @param page - The page to show
	 */
	public void showPage(Player player, int page){
		if(!hasPage(page)) page = 1;
		int start = elementsPerPage * (page - 1);
		TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, titleContainer + "====[&f" + title + titleContainer + "]====");
		for(int i=start; i<start+elementsPerPage; i++){
			TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, elements.get(i));
		}
	}
	
	public void showPage(CommandSender sender, int page){
		if(sender instanceof Player){
			showPage((Player) sender, page);
			return;
		}
		if(!hasPage(page)) page = 1;
		int start = elementsPerPage * (page - 1);
		TacoAPI.getChatAPI().out(titleContainer + "====[&f" + title + titleContainer + "]====");
		for(int i=start; i<start+elementsPerPage; i++){
			TacoAPI.getChatAPI().out(elements.get(i));
		}
	}
	
	public void setTitleContainerColor(char color){
		if(ChatColor.getByChar(color) == null) setTitleContainerColor(ChatColor.getByChar(color));
		else setTitleContainerColor(ChatColor.WHITE);
	}
	
	public void setTitleContainerColor(ChatColor color){
		titleContainer = color;
	}

	public void setTitle(String title){
		this.title = title;
	}
	
}
