/**
 * PistonCropInteraction just takes into account if a piston is about to break a crop
 * If a piston faces a crop it will not activate (but it can remain next to a crop and work if it faces a different direction.
 * @author JK_Bizcuits
 * @version 420.1
 */

package me.JK_Bizcuits.HoesMad;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonCropInteraction implements Listener {
	
	Main plugin;
	
	public PistonCropInteraction(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onPistonBlockBreak(BlockPistonExtendEvent event) {
		plugin.getConfig().getConfigurationSection("crops").getKeys(false).forEach(crop ->{
			for(BlockFace face: BlockFace.values()) {
				if(crop.equalsIgnoreCase(event.getBlock().getRelative(face).getType().toString())) {//if piston and crop are next to each other
					if(face==event.getDirection()) {//if piston faces crop, stop event
						event.setCancelled(true);
					}
				}
			}	
		});
	}//end onPistonBlockBreak
}
