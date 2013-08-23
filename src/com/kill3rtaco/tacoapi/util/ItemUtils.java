package com.kill3rtaco.tacoapi.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.TacoAPI;


/**
 * A class that helps with item handling, mainly used for ItemMail.
 * @author KILL3RTACO
 *
 */
public class ItemUtils {

	
	/**
	 * An enum holding extra names that can be used as aliases for creating an ItemStack.
	 */
	//One of the only times I will break my convention of not using internal classes/enums/interfaces
	public enum DisplayName{
		
		OAK_PLANK(5, 0),
		SPRUCE_PLANK(5, 1, "pine_plank", "spruce_plank"),
		BIRCH_PLANK(5, 2),
		JUNGLE_PLANK(5, 3),
		OAK_SAPLING(6, 0),
		SPRUCE_SAPLING(6, 1, "pine_sapling"),
		BIRCH_SAPLING(6, 2),
		JUNGLE_SAPLING(6, 3),
		OAK_LOG(17, 0, "log"),
		SPRUCE_LOG(17, 1),
		BIRH_LOG(17, 2),
		JUNGLE_LOG(17, 3),
		OAK_LEAVES(18, 0),
		SPRUCE_LEAVES(18, 1, "pine_leaves"),
		BIRCH_LEAVES(18, 2),
		JUNGLE_LEAVES(18, 3),
		WHITE_WOOL(35, 0, "white", "wool"),
		ORANGE_WOOL(35, 1, "orange"),
		MAGENTA_WOOL(35, 2, "magenta"),
		LIGHT_BLUE_WOOL(35, 3, "light_blue"),
		YELLOW_WOOL(35, 4, "yellow"),
		LIME_GREEN_WOOL(35, 5, "light_green_wool", "light_green", "lime_green"),
		PINK_WOOL(35, 6, "pink", "lightish_red"),
		GRAY_WOOL(35, 7, "grey_wool", "grey",  "gray_wool", "gray"),
		LIGHT_GRAY_WOOL(35, 8, "light_grey_wool", "light_grey", "light_gray"),
		CYAN_WOOL(35, 9, "cyan"),
		PURPLE_WOOL(35, 10, "purple"),
		BLUE_WOOL(35, 11, "blue"),
		BROWN_WOOL(35, 12, "brown"),
		GREEN_WOOL(35, 13, "green"),
		RED_WOOL(35, 14, "red"),
		BLACK_WOOL(35, 15, "black"),
		WHITE_STAINED_CLAY(159, 0, "white_clay"),
		ORANGE_STAINED_CLAY(159, 1, "orange_clay"),
		MAGENTA_STAINED_CLAY(159, 2, "magenta_clay"),
		LIGHT_BLUE_STAINED_CLAY(159, 3, "light_blue_clay"),
		YELLOW_STAINED_CLAY(159, 4, "yellow_clay"),
		LIME_GREEN_STAINED_CLAY(159, 5, "light_green_stained_clay", "light_green_clay", "lime_green_clay", "lime_stained_clay", "lime_clay"),
		PINK_STAINED_CLAY(159, 6, "pink_clay"),
		GRAY_STAINED_CLAY(159, 7, "gray_clay", "grey_stained_clay", "grey_clay"),
		LIGHT_GRAY_STAINED_CLAY(159, 8, "light_gray_clay", "grey_stained_clay", "grey_clay"),
		CYAN_STAINED_CLAY(159, 9, "cyan_clay"),
		PURPLE_STAINED_CLAY(159, 10, "purple_clay"),
		BLUE_STAINED_CLAY(159, 11, "blue_clay"),
		BROWN_STAINED_CLAY(159, 12, "brown_clay"),
		GREEN_STAINED_CLAY(159, 13, "green_clay"),
		RED_STAINED_CLAY(159, 14, "red_clay"),
		BLACK_STAINED_CLAY(159, 15, "black_Clay"),
		WHITE_CARPET(171, 0, "carpet"),
		ORANGE_CARPET(171, 1),
		MAGENTA_CARPET(171, 2),
		LIGHT_BLUE_CARPET(171, 3),
		YELLOW_CARPET(171, 4),
		LIME_GREEN_CARPET(171, 5, "light_green_carpet"),
		PINK_CARPET(171, 6),
		GRAY_CARPET(171, 7, "grey_carpet"),
		LIGHT_GRAY_CARPET(171, 8, "light_grey_carpet"),
		CYAN_CARPET(171, 9),
		PURPLE_CARPET(171, 10),
		BLUE_CARPET(171, 11),
		BROWN_CARPET(171, 12),
		GREEN_CARPET(171, 13),
		RED_CARPET(171, 14),
		BLACK_CARPET(171, 15),
		
		//items
		BLACK_DYE(351, 0, "ink_sac", "ink_sack"),
		RED_DYE(351, 1, "rose_red_dye", "rose_red"),
		GREEN_DYE(351, 2, "cactus_green_dye", "catus_green"),
		BROWN_DYE(351, 3, "cocoa_beans"),
		BLUE_DYE(351, 4, "lapis_lazuli"),
		PURPLE_DYE(351, 5),
		CYAN_DYE(351, 6),
		LIGHT_GRAY_DYE(351, 7, "light_grey_dye"),
		GRAY_DYE(351, 8, "grey_dye"),
		PINK_DYE(351, 9, "lightish_red_dye"),
		LIME_GREEN_DYE(351, 10, "light_green_dye"),
		YELLOW_DYE(351, 11, "dandelion_yellow_dye", "dandelion_yellow"),
		LIGHT_BLUE_DYE(351, 12),
		MAGENTA_DYE(351, 13),
		ORANGE_DYE(351, 14),
		WHITE_DYE(351, 15, "bonemeal_dye", "bonemeal"),
		CREEPER_SPAWN_EGG(383, 50, "spawn_creeper"),
		SKELETON_SPAWN_EGG(383, 51, "spawn_skeleton"),
		SPIDER_SPAWN_EGG(383, 52, "spawn_spider"),
		GIANT_SPAWN_EGG(383, 53, "spawn_giant"),
		ZOMBIE_SPAWN_EGG(383, 54, "spawn_zombie"),
		SLIME_SPAWN_EGG(383, 55, "spawn_slime"),
		GHAST_SPAWN_EGG(383, 56, "spawn_ghast"),
		ZOMBIE_PIGMAN_SPAWN_EGG(383, 57, "pigzombie_spawn_egg", "spawn_pigzombie", "spawn_zombie_pigman"),
		ENDERMAN_SPAWN_EGG(383, 58, "spawn_enderman"),
		CAVE_SPIDER_SPAWN_EGG(383, 59, "spawn_cave_spider"),
		SILVERFISH_SPAWN_EGG(383, 60, "spawn_silverfish"),
		BLAZE_SPAWN_EGG(383, 61, "spawn_blaze"),
		MAGMA_CUBE_SPAWN_EGG(383, 62, "spawn_magma_cube"),
		ENDER_DRAGON_SPAWN_EGG(383, 63, "enderdragon_spawn_egg", "spawn_enderdragon", "spawn_ender_dragon"),
		WITHER_SPAWN_EGG(383, 64, "spawn_wither"),
		BAT_SPAWN_EGG(383, 65, "spawn_bat"),
		WITCH_SPAWN_EGG(383, 66, "spawn_witch"),
		PIG_SPAWN_EGG(383, 90, "spawn_pig"),
		SHEEP_SPAWN_EGG(383, 91, "spawn_sheep"),
		COW_SPAWN_EGG(383, 92, "spawn_cow"),
		CHICKEN_SPAWN_EGG(383, 93, "spawn_chicken"),
		SQUID_SPAWN_EGG(383, 94, "spawn_squid"),
		WOLF_SPAWN_EGG(383, 95, "spawn_wolf"),
		MOOSHROOM_SPAWN_EGG(383, 96, "spawn_mooshroom"),
		SNOW_GOLEM_SPAWN_EGG(383, 97, "spawn_snow_golem"),
		OCELOT_SPAWN_EGG(383, 98, "spawn_ocelot"),
		IRON_GOLEM_SPAWN_EGG(383, 99, "spawn_iron_golem"),
		HORSE_SPAWN_EGG(383, 100, "spawn_horse"),
		VILLAGER_SPAWN_EGG(383, 120, "spawn_villager"),
		SKELETON_HEAD(397, 0),
		WITHER_SKELETON_HEAD(383, 1),
		ZOMBIE_HEAD(383, 2),
		HUMAN_HEAD(383, 3, "steve_head"),
		CREEPER_HEAD(383, 4);
		
		private int id, damage;
		private String[] aliases;
		
		private DisplayName(int id, int damage, String... aliases){
			this.id = id;
			this.damage = damage;
			this.aliases = aliases;
		}
		
		/**
		 * Tests if this has the given alias.
		 * @param alias The alias to test
		 * @return true if this has the alias given.
		 */
		public boolean hasAlias(String alias){
			if(name().equalsIgnoreCase(alias)) return true;
			for(String s : aliases)
				if(s.equalsIgnoreCase(alias)) return true;
			return false;
		}
		
		public String getName(){
			return TacoAPI.getChatUtils().toProperCase(name().replaceAll("_", " "));
		}
		
		/**
		 * Gets the id for this DisplayName
		 * @return The id value
		 */
		public int getId(){
			return id;
		}
		
		/**
		 * Gets the damage value for this DisplayName
		 * @return The damage value
		 */
		public int getDamage(){
			return damage;
		}
		
		/**
		 * Gets a DisplayName object using the given name as an alias
		 * @param name the alias to use
		 * @return The DisplayName found, if exists.
		 */
		public static DisplayName getDisplayName(String name){
			for(DisplayName dn : DisplayName.values())
				if(dn.hasAlias(name)) return dn;
			return null;
		}
		
		public static DisplayName getDisplayName(int id, int damage){
			for(DisplayName dn : DisplayName.values())
				if(dn.getId() == id && dn.getDamage() == damage) return dn;
			return null;
		}
		
	}
	
	public enum EnchantDisplayName {
		PROTECTION(0),
		FIRE_PROTECTION(1),
		FEATHER_FALLING(2),
		BLAST_PROTECTION(3),
		PROJECTILE_PROTECTION(4),
		RESPIRATION(5),
		AQUA_AFFINITY(6),
		THORNS(7),
		SHARPNESS(16),
		SMITE(17),
		BANE_OF_ARTHROPODS(18),
		KNOCKBACK(19),
		FIRE_ASPECT(20),
		LOOTING(21),
		EFFICIENCY(32),
		SILK_TOUCH(33),
		UNBREAKING(34),
		FORTUNE(35),
		POWER(48),
		PUNCH(49),
		FLAME(50),
		INFINITY(51);
		
		private int id;
		
		private EnchantDisplayName(int id){
			this.id = id;
		}
		
		public int getId(){
			return id;
		}
		
		public String getName(){
			return TacoAPI.getChatUtils().toProperCase(name().replace("_", " "));
		}
		
		public static EnchantDisplayName getDisplayName(int id){
			for(EnchantDisplayName name : EnchantDisplayName.values()){
				if(id == name.getId()) return name;
			}
			return null;
		}
		
	}
	
	/**
	 * Creates an ItemStack using the given name
	 * @param name The name of the item to use
	 * @param amount The amount of the item to make.
	 * @return The ItemStack created, if created. Otherwise null.
	 */
	public ItemStack createItemStack(String name, int amount){
		short damage = 0;
		String alias;
		if(name.contains(":")){
			String[] split = name.split(":");
			alias = split[0];
			if(TacoAPI.getChatUtils().isNum(split[1])){
				damage = (short) Integer.parseInt(split[1]);
			}
		}else{
			alias = name;
		}
		Material mat = getMaterial(alias);
		if(mat == null){
			DisplayName dn = DisplayName.getDisplayName(alias);
			if(dn == null) return null;
			return new ItemStack(dn.getId(), amount, (short) dn.getDamage()); //extra damage at the end of the string will not be applied
		}else{
			return new ItemStack(mat, amount, (short) damage);
		}
	}
	
	public Material getMaterial(String material){
		if(TacoAPI.getChatUtils().isNum(material))
			return Material.getMaterial(Integer.parseInt(material));
		else
			return Material.getMaterial(material.toUpperCase());
	}
	
	/**
	 * Tests if a DisplayName exists with the given alias
	 * @param name The alias to use.
	 * @return true if the DisplayName exists
	 */
	public boolean displayNameExists(String name){
		for(DisplayName dn : DisplayName.values())
			if(dn.hasAlias(name)) return true;
		return false;
	}
	
	public boolean isHelmet(Material material){
		return material == Material.LEATHER_HELMET || material == Material.GOLD_HELMET || material == Material.IRON_HELMET ||
				material == Material.CHAINMAIL_HELMET || material == Material.DIAMOND_HELMET;
	}
	
	public boolean isChestPlate(Material material){
		return material == Material.LEATHER_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.IRON_CHESTPLATE ||
				material == Material.CHAINMAIL_CHESTPLATE || material == Material.DIAMOND_CHESTPLATE;
	}
	
	public boolean isLegging(Material material){
		return material == Material.LEATHER_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.IRON_LEGGINGS ||
				material == Material.CHAINMAIL_LEGGINGS || material == Material.DIAMOND_LEGGINGS;
	}
	
	public boolean isBoot(Material material){
		return material == Material.LEATHER_BOOTS || material == Material.GOLD_BOOTS || material == Material.IRON_BOOTS ||
				material == Material.CHAINMAIL_BOOTS || material == Material.DIAMOND_BOOTS;
	}
	
	public boolean isArmor(Material material){
		return isHelmet(material) || isChestPlate(material) || isLegging(material) || isBoot(material);
	}
	
	public boolean isLeatherArmor(Material material){
		return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE || 
				material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS;
	}
	
	public boolean isGoldArmor(Material material){
		return material == Material.GOLD_HELMET || material == Material.GOLD_CHESTPLATE || 
				material == Material.GOLD_LEGGINGS || material == Material.GOLD_BOOTS;
	}
	
	public boolean isIronArmor(Material material){
		return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE || 
				material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS;
	}
	
	public boolean isChainMailArmor(Material material){
		return material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE || 
				material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS;
	}
	
	public boolean isDiamondArmor(Material material){
		return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE || 
				material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
	}
	
	public boolean isPickaxe(Material material){
		return material == Material.WOOD_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.IRON_PICKAXE || material == Material.DIAMOND_PICKAXE;
	}
	
	public boolean isAxe(Material material){
		return material == Material.WOOD_AXE || material == Material.GOLD_AXE || material == Material.IRON_AXE || material == Material.DIAMOND_AXE;
	}
	
	public boolean isShovel(Material material){
		return material == Material.WOOD_SPADE || material == Material.GOLD_SPADE || material == Material.IRON_SPADE || material == Material.DIAMOND_SPADE;
	}
	
	public boolean isHoe(Material material){ //no u da ho
		return material == Material.WOOD_HOE || material == Material.GOLD_HOE || material == Material.IRON_HOE || material == Material.DIAMOND_HOE;
	}
	
	public boolean isTool(Material material){
		return isPickaxe(material) || isAxe(material) || isShovel(material) || isHoe(material) || material == Material.SHEARS;
	}
	
	public boolean isSword(Material material){
		return material == Material.WOOD_SWORD || material == Material.GOLD_SWORD || material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD;
	}
	
	public boolean isWeapon(Material material){
		return isSword(material) || material == Material.BOW;
	}
	
}
