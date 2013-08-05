package com.kill3rtaco.tacoapi.api;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.tacoapi.api.metrics.Metrics;
import com.kill3rtaco.tacoapi.obj.ChatObject;


public abstract class TacoPlugin extends JavaPlugin {

	private ArrayList<Integer> tasks = new ArrayList<Integer>();
	public ChatObject chat;
	private long timeStart, timeEnd;
	
	public void onEnable(){
		timeStart = System.currentTimeMillis();
		chat = new ChatObject(super.getDescription().getName());
		onStart();
		timeEnd = System.currentTimeMillis();
		chat.out("Enabled - " + timeTakenToEnable() + "s");
	}
	
	public void onDisable(){
		cancelAllTasks();
		onStop();
	}
	
	public abstract void onStart();
	
	public abstract void onStop();
	
	/**
	 * Start Metrics (c) Hidendra
	 */
	public void startMetrics(){
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			chat.out("Could not start PluginMetrics");
		}
	}
	
	public double timeTakenToEnable(){
		return (timeEnd - timeStart) / 1000D;
	}
	
	public void registerCommand(TacoCommandHandler handler){
		getCommand(handler.getName()).setExecutor(handler);
	}
	
	public void registerEvents(Listener l){
		getServer().getPluginManager().registerEvents(l, this);
	}
	
	public int registerDelayedTask(Runnable task, long delay){
		int taskId = getServer().getScheduler().scheduleSyncDelayedTask(this, task, delay);
		tasks.add(taskId);
		return taskId;
	}
	
	public int registerRepeatingTask(Runnable task, long delay, long interval){
		int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, task, delay, interval);
		tasks.add(taskId);
		return taskId;
	}
	
	public void cancelTask(int task){
		getServer().getScheduler().cancelTask(task);
		tasks.remove(task);
	}
	
	public void cancelAllTasks(){
		tasks.clear();
		getServer().getScheduler().cancelTasks(this);
	}

}
