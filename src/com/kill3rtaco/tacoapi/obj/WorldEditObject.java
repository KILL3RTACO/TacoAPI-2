package com.kill3rtaco.tacoapi.obj;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;


import com.kill3rtaco.tacoapi.TacoAPI;
import com.sk89q.worldedit.DisallowedItemException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.InvalidItemException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.UnknownItemException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.blocks.ClothColor;
import com.sk89q.worldedit.blocks.NoteBlock;
import com.sk89q.worldedit.blocks.SignBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class WorldEditObject {
	
	private WorldEditPlugin we;
	
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
	
	/**
	 * Convert a Location to a Vector
	 * @param location The location to convert
	 * @return
	 */
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
	
	public BaseBlock getBlock(String arg, LocalWorld world) throws UnknownItemException, DisallowedItemException {
        BlockType blockType;
        arg = arg.replace("_", " ");
        arg = arg.replace(";", "|");
        String[] blockAndExtraData = arg.split("\\|");
        String[] typeAndData = blockAndExtraData[0].split(":", 2);
        String testID = typeAndData[0];
        int blockId = -1;
        
        int data = -1;
        
        // Attempt to parse the item ID or otherwise resolve an item/block
        // name to its numeric ID
        try {
            blockId = Integer.parseInt(testID);
            blockType = BlockType.fromID(blockId);
        } catch (NumberFormatException e) {
            blockType = BlockType.lookup(testID);
            if (blockType == null) {
                
            }
        }
        
        if ((blockId == -1) && (blockType == null)) {
            // Maybe it's a cloth
            ClothColor col = ClothColor.lookup(testID);
            
            if (col != null) {
                blockType = BlockType.CLOTH;
                data = col.getID();
            } else {
                throw new UnknownItemException(arg);
            }
        }
        
        // Read block ID
        if (blockId == -1) {
            blockId = blockType.getID();
        }
        
        if (!world.isValidBlockType(blockId)) { throw new UnknownItemException(arg); }
        
        if (data == -1) { // Block data not yet detected
            // Parse the block data (optional)
            try {
                data = typeAndData.length > 1 ? Integer.parseInt(typeAndData[1]) : 0;
                if ((data > 15) || ((data < 0) && !(data == -1))) {
                    data = 0;
                }
            } catch (NumberFormatException e) {
                if (blockType != null) {
                    switch (blockType) {
                        case CLOTH:
                            ClothColor col = ClothColor.lookup(typeAndData[1]);
                            
                            if (col != null) {
                                data = col.getID();
                            } else {
                                throw new InvalidItemException(arg, "Unknown cloth color '" + typeAndData[1] + "'");
                            }
                            break;
                        
                        case STEP:
                        case DOUBLE_STEP:
                            BlockType dataType = BlockType.lookup(typeAndData[1]);
                            
                            if (dataType != null) {
                                switch (dataType) {
                                    case STONE:
                                        data = 0;
                                        break;
                                    
                                    case SANDSTONE:
                                        data = 1;
                                        break;
                                    
                                    case WOOD:
                                        data = 2;
                                        break;
                                    
                                    case COBBLESTONE:
                                        data = 3;
                                        break;
                                    case BRICK:
                                        data = 4;
                                        break;
                                    case STONE_BRICK:
                                        data = 5;
                                        
                                    default:
                                        throw new InvalidItemException(arg, "Invalid step type '" + typeAndData[1] + "'");
                                }
                            } else {
                                throw new InvalidItemException(arg, "Unknown step type '" + typeAndData[1] + "'");
                            }
                            break;
                        
                        default:
                            throw new InvalidItemException(arg, "Unknown data value '" + typeAndData[1] + "'");
                    }
                } else {
                    throw new InvalidItemException(arg, "Unknown data value '" + typeAndData[1] + "'");
                }
            }
        }
        
        // Check if the item is allowed
        if (blockType != null) {
            switch (blockType) {
                case SIGN_POST:
                case WALL_SIGN:
                    // Allow special sign text syntax
                    String[] text = new String[4];
                    text[0] = blockAndExtraData.length > 1 ? blockAndExtraData[1] : "";
                    text[1] = blockAndExtraData.length > 2 ? blockAndExtraData[2] : "";
                    text[2] = blockAndExtraData.length > 3 ? blockAndExtraData[3] : "";
                    text[3] = blockAndExtraData.length > 4 ? blockAndExtraData[4] : "";
                    return new SignBlock(blockType.getID(), data, text);
                    
                case MOB_SPAWNER:

                case NOTE_BLOCK:
                    if (blockAndExtraData.length > 1) {
                        byte note = Byte.parseByte(blockAndExtraData[1]);
                        if ((note < 0) || (note > 24)) {
                            throw new InvalidItemException(arg, "Out of range note value: '" + blockAndExtraData[1] + "'");
                        } else {
                            return new NoteBlock(data, note);
                        }
                    } else {
                        return new NoteBlock(data, (byte) 0);
                    }
                    
                default:
                    return new BaseBlock(blockId, data);
            }
        } else {
            return new BaseBlock(blockId, data);
        }
    }
	
	public int setAreaWithBlock(String world, Location minPoint, Location maxPoint, String blockId) {
        LocalSession session = this.getLocalSession();
        RegionSelector selector = session.getRegionSelector(getLocalWorld(world));
        selector.selectPrimary(toVector(minPoint));
        selector.selectSecondary(toVector(maxPoint));
        EditSession editSession = new EditSession(getLocalWorld(world), -1);
        Pattern pattern = null;
        try {
            pattern = new SingleBlockPattern(this.getBlock(blockId, getLocalWorld(world)));
        } catch (UnknownItemException e) {
            e.printStackTrace();
        } catch (DisallowedItemException e) {
            e.printStackTrace();
        }
        int affected = -1;
        
        if (pattern instanceof SingleBlockPattern) {
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
