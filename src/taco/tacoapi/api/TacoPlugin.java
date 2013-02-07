package taco.tacoapi.api;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import taco.tacoapi.api.metrics.Metrics;
import taco.tacoapi.obj.ChatObject;

public abstract class TacoPlugin extends JavaPlugin {

	private ArrayList<Integer> tasks = new ArrayList<Integer>();
	public static ChatObject chat;
	
	public void onEnable(){
		chat = new ChatObject(getDescription().getName());
		onStart();
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
