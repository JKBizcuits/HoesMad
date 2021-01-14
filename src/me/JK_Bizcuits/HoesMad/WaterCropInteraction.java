/**
 * WaterCropInteraction just takes into account if water (or lava) flows near a crop.
 * If water (or lava) flows near a crop, it will stop before reaching, preserving the crop.
 * @author JK_Bizcuits
 * @version 420.1
 */

package me.JK_Bizcuits.HoesMad;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class WaterCropInteraction implements Listener {

	Main plugin;
	
	public WaterCropInteraction(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onWaterBlockBreak(BlockFromToEvent event) {
		plugin.getConfig().getConfigurationSection("crops").getKeys(false).forEach(crop ->{
			for(BlockFace face: BlockFace.values()) {
				if(crop.equalsIgnoreCase(event.getToBlock().getRelative(face).getType().toString())) {//if water or lava is about to hit a crop it stops
					event.setCancelled(true);
				}
			}	
		});
	}//end onWaterBlockBreak
}