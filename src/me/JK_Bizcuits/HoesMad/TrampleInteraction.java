/**
 * TrampleInteraction will listen for players that jump on farming blocks. 
 * If the player is on the trample list then the player cannot break farmland blocks by jumping on them
 * List of players can be found on trampleList.yml in HoesMad Folder
 * @author JK_Bizcuits
 * @version 420.1
 */
package me.JK_Bizcuits.HoesMad;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class TrampleInteraction implements Listener {
	
	Main plugin;
	FileConfiguration trampleList;
	
	public TrampleInteraction(Main main) {
		plugin = main;	
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.PHYSICAL && (event.getClickedBlock().getType() == Material.FARMLAND)) {//if the event is a jump and block is farmland
			trampleList = YamlConfiguration.loadConfiguration(plugin.getFile());//get updated trampleList
			if(trampleList.contains(event.getPlayer().getUniqueId().toString())) {//if player is on the list then cancel breaking farmland from jump
							event.setCancelled(true);
			}
		}
	}//end onPlayerInteract
}
