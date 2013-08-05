package com.kill3rtaco.tacoapi.obj;

import java.text.DecimalFormat;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.kill3rtaco.tacoapi.TacoAPI;

import net.milkbowl.vault.economy.Economy;

public class EconObject {

	private Economy econ;
	
	public EconObject() {
		RegisteredServiceProvider<Economy> economyProvider = 
				TacoAPI.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }
	}
	
	/**
	 * Get the balance of a player
	 * @param name The name of the player to check
	 * @return The balance of a player
	 */
	public double getBalance(String name){
		return econ.getBalance(name);
	}
	
	/**
	 * Adds the currency name to the balance of a player.
	 * @param name The name of the player to check
	 * @return The balance of a player, including the currency name
	 */
	public String getFormattedBalance(String name){
		DecimalFormat formatter = new DecimalFormat("#,###.##");
		double bal = getBalance(name);
		if(bal == 1){
			return formatter.format(bal) + " " + currencyName();
		}else{
			return formatter.format(bal) + " " + currencyNamePlural();
		}
	}
	
	/**
	 * Get the name of the Economy plugin in use
	 * @return The name of the economy method
	 */
	public String getEconomyName(){
		return econ.getName();
	}
	/**
	 * Get the name of the currency in use
	 * @return Name of the currency in use
	 */
	public String currencyName(){
		return econ.currencyNameSingular();
	}
	
	/** 
	 * Get the name of the currency in use in plural form
	 * @return Plural name of the currency in use
	 */
	public String currencyNamePlural(){
		return econ.currencyNamePlural();
	}
	
	/**
	 * Give money to a player
	 * @param name The player to give to
	 * @param amount The amount to give
	 */
	public void deposit(String name, double amount){
		econ.depositPlayer(name, amount);
	}
	
	/** 
	 * Take money from a player
	 * @param name The player to take from
	 * @param amount The amount to take
	 */
	public void withdraw(String name, double amount){
		econ.withdrawPlayer(name, amount);
	}
	
	/**
	 * Make one player pay another
	 * @param sender The player paying
	 * @param receiver the player to pay
	 * @param amount The amount to pay
	 * @return Whether the sender can pay the amount specified
	 */
	public boolean pay(String sender, String receiver, double amount){
		if(econ.has(sender, amount)){
			withdraw(sender, amount);
			deposit(receiver, amount);
			return true;
		}else{
			return false;
		}
	}
	
}
