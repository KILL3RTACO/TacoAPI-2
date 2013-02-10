package taco.tacoapi.obj;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;

import taco.tacoapi.TacoAPI;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class WorldEditObject {
	
	private static WorldEditPlugin we;
	
	public WorldEditObject(WorldEditPlugin worldEdit){
		we = worldEdit;
	}
	
	private LocalConfiguration getLocalConfig(){
		return we.getWorldEdit().getConfiguration();
	}
	
	private LocalSession getLocalSession(){
		return new LocalSession(getLocalConfig());
	}
	
	private LocalWorld getLocalWorld(String world){
		return new BukkitWorld(TacoAPI.plugin.getServer().getWorld(world));
	}
	
	public Vector toVector(Location location){
		return new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/**
	 * Pastes a schematic at a desired Location
	 * @param world - The world to paste the schematic
	 * @param schematic - The schematic to paste
	 * @param location - Where to paste the schematic
	 */
	public void pasteSchematic(String world, String schematic, Location location){
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
	public void pasteSchematicAtVector(String world, String schematic, Vector location) throws DataException, IOException, EmptyClipboardException{
		LocalSession session = getLocalSession();
		LocalWorld localWorld = getLocalWorld(world);
		Vector position = WorldVector.toBlockPoint(localWorld, location.getX(), location.getY(), location.getZ());
		
		File schema = new File(schematic);
		session.setClipboard(SchematicFormat.MCEDIT.load(schema));
		
		BlockBag blocks = null;
		
		EditSession es = new EditSession(localWorld, session.getBlockChangeLimit(), blocks);
		es.setFastMode(session.hasFastMode());
		es.setMask(session.getMask());
		try{
			session.getClipboard().paste(es, position, false);
		} catch(MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}

}
