package com.kill3rtaco.tacoapi.util;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;

/**
 * A class to help make page managing easier.
 * @author KILL3RTACO
 *
 */
public class PageBuilder {
	
	private ArrayList<String>	elements;
	private int					pages			= 0, elementsPerPage = 5;
	private String				title, subtitle;
	private String				titleContainer	= "&f";
	
	public PageBuilder(String title, String titleContainerColor) {
		elements = new ArrayList<String>();
		this.title = title;
		this.titleContainer = titleContainerColor;
	}
	
	/**
	 * Set the subtitle. The subtitle is display under the title.
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	/**
	 * Add a line.
	 * @param element The element to add
	 */
	public void append(String element) {
		elements.add(element);
		if(pages == 0) {
			pages++;
			return;
		}
		pages = elements.size() / elementsPerPage + (elements.size() % elementsPerPage != 0 ? 1 : 0);
	}
	
	public int getElementsPerPage() {
		return elementsPerPage;
	}
	
	/**
	 * Tests if there are no pages.
	 * @return true if there are no pages
	 */
	public boolean hasNoPages() {
		return pages == 0;
	}
	
	/**
	 * Test if the page exists.
	 * @param page
	 * @return true if the page exists
	 */
	public boolean hasPage(int page) {
		return page > 0 && page <= pages;
	}
	
	/**
	 * Get the amount of pages this PageBuilder has
	 * @return the amount of pages
	 */
	public int pages() {
		int pages = elements.size() / 10;
		if(elements.size() % 10 > 0)
			pages++;
		return pages;
	}
	
	/**
	 * Remove a line.
	 * @param element - The line to be removed
	 */
	public void remove(String element) {
		if(elements.contains(element))
			elements.remove(element);
	}
	
	/**
	 * Show a page to a player
	 * @param player - The player to send to
	 * @param page - The page to show
	 */
	public void showPage(Player player, int page) {
		if(!hasPage(page))
			page = 1;
		int start = elementsPerPage * (page - 1);
		TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, titleContainer + "====[&f" + title + " Page " + page + "/" + pages + titleContainer + "]====");
		if(subtitle != null && !subtitle.isEmpty())
			TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, subtitle);
		for(int i = start; i < start + elementsPerPage; i++) {
			if(i + 1 > elements.size())
				break;
			TacoAPI.getChatAPI().sendPlayerMessageNoHeader(player, elements.get(i));
		}
	}
	
	public void showPage(CommandSender sender, int page) {
		if(sender instanceof Player) {
			showPage((Player) sender, page);
			return;
		}
		if(!hasPage(page))
			page = 1;
		int start = elementsPerPage * (page - 1);
		TacoAPI.getChatAPI().outNoHeader(titleContainer + "====[&f" + title + " Page " + page + "/" + pages + titleContainer + "]====");
		if(subtitle != null && !subtitle.isEmpty())
			TacoAPI.getChatAPI().outNoHeader(subtitle);
		for(int i = start; i < start + elementsPerPage; i++) {
			if(i + 1 > elements.size())
				break;
			TacoAPI.getChatAPI().outNoHeader(elements.get(i));
		}
	}
	
	public void setElementsPerPage(int elements) {
		elementsPerPage = elements;
	}
	
	public void setTitleContainerColor(String color) {
		titleContainer = color;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
