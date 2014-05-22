package com.kill3rtaco.tacoapi.api.ncommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TabCompletionLists {
	
	private static ArrayList<String>	_entityNames, _mobNames,
										_materialNames, _playerNames;
	
	static {
		_entityNames = new ArrayList<String>();
		_mobNames = new ArrayList<String>();
		_materialNames = new ArrayList<String>();
		for(EntityType e : EntityType.values()) {
			String name = e.toString();
			_entityNames.add(name);
			if(e.isAlive()) {
				_mobNames.add(e.toString());
			}
		}
		sort(_entityNames);
		sort(_mobNames);
		loopAddSort(Material.values(), _materialNames);
	}
	
	public static ArrayList<String> entities() {
		return _entityNames;
	}
	
	public static ArrayList<String> mobs() {
		return _mobNames;
	}
	
	public static ArrayList<String> materials() {
		return _materialNames;
	}
	
	public static ArrayList<String> players() {
		_playerNames = new ArrayList<String>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			_playerNames.add(p.getName());
		}
		sort(_playerNames);
		return _playerNames;
	}
	
	private static <E extends Object> void loopAddSort(E[] array, ArrayList<String> list) {
		for(E e : array) {
			list.add(e.toString());
			sort(list);
		}
	}
	
	public static void sort(ArrayList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			
			@Override
			public int compare(String s, String str) {
				return s.compareTo(str);
			}
			
		});
	}
	
}
