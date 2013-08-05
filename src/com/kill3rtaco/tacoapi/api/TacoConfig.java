package com.kill3rtaco.tacoapi.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Represents a Config file.
 * @author KILL3RTACO
 *
 */
public abstract class TacoConfig {

	protected YamlConfiguration config;
	private File file;
	
	public TacoConfig(File file){
		this.file = file;
		reload();
	}
	
	/**
	 * Sets the default values for the config file.
	 */
	protected abstract void setDefaults();
	
	/**
	 * Adds a default value to the config file if and only if the path does not exist.
	 * @param path - The path in which the value is set
	 * @param value - The value to set
	 */
	public void addDefaultValue(String path, Object value){
		if(!config.contains(path)){
			if(value instanceof String){
				config.set(path, (String)value);
			}else{
				config.set(path, value);
			}
		}
	}
	
	/**
	 * Reload the config values
	 */
	public void reload(){
		this.config = YamlConfiguration.loadConfiguration(file);
		setDefaults();
		save();
	}
	
	/**
	 * Saves the config file.
	 */
	public void save(){
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a string from the config file.
	 * @param path - The path which the String is taken from
	 * @return The String found
	 */
	public String getString(String path){
		return config.getString(path);
	}
	
	public List<String> getStringList(String path){
		return config.getStringList(path);
	}
	
	public double getDouble(String path){
		return config.getDouble(path);
	}
	
	public List<Double> getDoubleList(String path){
		return config.getDoubleList(path);
	}
	
	/**
	 * Gets an int from the config file.
	 * @param path - The path which the int is taken from
	 * @return The int found
	 */
	public int getInt(String path){
		return config.getInt(path);
	}
	
	public List<Integer> getIntList(String path){
		return config.getIntegerList(path);
	}
	
	/**
	 * Gets a boolean from the config file.
	 * @param path - The path which the boolean is taken from
	 * @return The boolean found
	 */
	public boolean getBoolean(String path){
		return config.getBoolean(path);
	}
	
	public List<Boolean> getBooleanList(String path){
		return config.getBooleanList(path);
	}
	
	public void set(String path, Object value){
		config.set(path, value);
		save();
	}
	
	public void setBoolean(String path, boolean value){
		config.set(path, value);
		save();
	}
	
	public void setString(String path, String value){
		config.set(path, value);
		save();
	}
	
	public void setInt(String path, int value){
		config.set(path, value);
		save();
	}
	
	public void setDouble(String path, double value){
		config.set(path, value);
		save();
	}
	
	public boolean contains(String path){
		return config.contains(path);
	}
	
	public Map<String, Object> getMeta(){
		return config.getValues(true);
	}
	
}
