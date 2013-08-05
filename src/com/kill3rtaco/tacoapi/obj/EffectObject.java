package com.kill3rtaco.tacoapi.obj;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectObject {
	
	/**
	 * Applies a potion effect to a player
	 * @param player The player to apply the effect
	 * @param effectType The effect to apply
	 * @param duration How long the effect should last in seconds
	 * @param strength The strength of the effect
	 */
	public void applyPotionEffect(Player player, PotionEffectType effectType, int duration, int strength){
		player.addPotionEffect(new PotionEffect(effectType, duration * 20, strength));
	}

	/**
	 * Creates an explosion at a location
	 * @param location The location for explosion
	 * @param force The force of the explosion
	 */
	public void createExplosion(Location location, float force){
		location.getWorld().createExplosion(location, force);
	}
	
	/**
	 * Kills a LivingEntity
	 * @param entity The entity to kill
	 */
	public void killEntity(LivingEntity entity){
		entity.setHealth(0);
	}
	
	/**
	 * Ignite an entity for a certain amount of time
	 * @param entity The entity to ignite
	 * @param duration How long it should last
	 */
	public void igniteEntity(Entity entity, int duration){
		entity.setFireTicks(duration);
	}
	
	/**
	 * Removes a potion effect on a player
	 * @param player The player to remove the potion effect from
	 * @param effectType The effect to remove
	 */
	public void removePotionEffect(Player player, PotionEffectType effectType){
		player.removePotionEffect(effectType);
	}
	
	/**
	 * Strike lightning at a location
	 * @param location The location to strike with lightning
	 */
	public void strike(Location location){
		location.getWorld().strikeLightning(location);
	}
	
	/**
	 * Strike lightning at a location, but without the side effects (i.e. damage/fire)
	 * @param location The location to strike
	 */
	public void strikeEffect(Location location){
		location.getWorld().strikeLightningEffect(location);
	}
	
	/**
	 * Show smoke at a location
	 * @param location Where to show the smoke
	 */
	public void showSmoke(Location location){
		showSmoke(location, 20);
	}

	/**
	 * Show smoke at a location
	 * @param location Where to show the smoke
	 * @param iterations Amount of iterations - use wisely
	 */
	public void showSmoke(Location location, int iterations){
		Random random = new Random();
		for(int i=0; i<iterations; i++){
			location.getWorld().playEffect(location, Effect.SMOKE, random.nextInt(9));
		}
	}
	
}
