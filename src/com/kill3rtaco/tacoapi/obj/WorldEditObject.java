package com.kill3rtaco.tacoapi.obj;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitCommandSender;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class WorldEditObject {
	
	private WorldEditPlugin	we;
	
	public WorldEditObject(WorldEditPlugin worldEdit) {
		we = worldEdit;
	}
	
	private LocalConfiguration getLocalConfig() {
		return we.getWorldEdit().getConfiguration();
	}
	
	private LocalSession getLocalSession() {
		return new LocalSession(getLocalConfig());
	}
	
	private LocalWorld getLocalWorld(String world) {
		return new BukkitWorld(TacoAPI.plugin.getServer().getWorld(world));
	}
	
	private LocalWorld getLocalWorld(World world) {
		return new BukkitWorld(world);
	}
	
	/**
	 * Convert a Location to a Vector
	 * @param location The location to convert
	 * @return
	 */
	public Vector toVector(Location location) {
		return new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/**
	 * Pastes a schematic at a desired Location
	 * @param world - The world to paste the schematic
	 * @param schematic - The schematic to paste
	 * @param location - Where to paste the schematic
	 */
	public void pasteSchematic(String world, String schematic, Location location) {
		try {
			pasteSchematicAtVector(world, schematic, toVector(location));
		} catch (EmptyClipboardException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pastes a schematic at a desired Vector
	 * @param world - The world to paste in
	 * @param schematic - The schematic to paste, the must be the full/relative path to the file
	 * @param location - Where to paste the schematic
	 * @throws DataException
	 * @throws IOException
	 * @throws EmptyClipboardException
	 */
	public void pasteSchematicAtVector(String world, String schematic, Vector location) throws DataException, IOException, EmptyClipboardException {
		LocalSession session = getLocalSession();
		LocalWorld localWorld = getLocalWorld(world);
		Vector position = WorldVector.toBlockPoint(localWorld, location.getX(), location.getY(), location.getZ());
		
		File schema = new File(schematic);
		session.setClipboard(SchematicFormat.MCEDIT.load(schema));
		
		BlockBag blocks = null;
		
		EditSession es = new EditSession(localWorld, session.getBlockChangeLimit(), blocks);
		es.setFastMode(session.hasFastMode());
		es.setMask(session.getMask());
		try {
			session.getClipboard().paste(es, position, false);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	
	public BaseBlock getBlock(String id, final String world) throws WorldEditException {
		return getBlock(id, getLocalWorld(world));
	}
	
	public BaseBlock getBlock(String id, final LocalWorld world) throws WorldEditException {
		if(world == null)
			throw new IllegalArgumentException("world cannot be null");
		return we.getWorldEdit().getBlock(new BukkitCommandSender(we, we.getServerInterface(), Bukkit.getConsoleSender()) {
			
			@Override
			public LocalWorld getWorld() {
				return world;
			}
			
		}, id, true);
	}
	
	public Selection getSelection(Player player) {
		return we.getSelection(player);
	}
	
	public void savePlayerClipboardAsSchematic(Player player, File f) throws EmptyClipboardException, IOException, DataException {
		if(f == null) {
			throw new IllegalArgumentException("File cannot be null");
		} else if(f.exists()) {
			throw new IllegalArgumentException("File " + f.getAbsolutePath() + " already exists");
		}
		SchematicFormat format = SchematicFormat.getFormat("mce"); //MCEdit
		format.save(we.getSession(player).getClipboard(), f);
	}
	
	public void savePlayerSelectionAsSchematic(Player player, File f) throws IncompleteRegionException, IOException, DataException {
		LocalSession session = we.getSession(player);
		EditSession editSession = new EditSession(getLocalWorld(player.getWorld()), -1);
		Region region = session.getSelection(getLocalWorld(player.getWorld()));
		Vector min = region.getMinimumPoint();
		Vector max = region.getMaximumPoint();
		Vector pos = session.getPlacementPosition(we.wrapPlayer(player));
		
		CuboidClipboard clipboard = new CuboidClipboard(
				max.subtract(min).add(Vector.ONE),
				min, min.subtract(pos));
		
		clipboard.copy(editSession);
		SchematicFormat format = SchematicFormat.getFormat("mce"); //MCEdit
		format.save(clipboard, f);
	}
	
	public int setAreaWithBlock(String world, Location minPoint, Location maxPoint, String blockId) {
		LocalSession session = this.getLocalSession();
		RegionSelector selector = session.getRegionSelector(getLocalWorld(world));
		selector.selectPrimary(toVector(minPoint));
		selector.selectSecondary(toVector(maxPoint));
		EditSession editSession = new EditSession(getLocalWorld(world), -1);
		Pattern pattern = null;
		try {
			pattern = new SingleBlockPattern(getBlock(blockId, editSession.getWorld()));
		} catch (WorldEditException e) {
			e.printStackTrace();
		}
		int affected = -1;
		
		if(pattern instanceof SingleBlockPattern) {
			try {
				affected = editSession.setBlocks(selector.getRegion(), ((SingleBlockPattern) pattern).getBlock());
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
			} catch (IncompleteRegionException e) {
				e.printStackTrace();
			}
		} else {
			try {
				affected = editSession.setBlocks(selector.getRegion(), pattern);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
			} catch (IncompleteRegionException e) {
				e.printStackTrace();
			}
			
		}
		return affected;
	}
	
}
