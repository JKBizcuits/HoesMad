/**
 * TrampleCommand is for the "trample" command that adds or removes players from the trample list through their uuid.
 * All players can add or remove themselves from the list but only moderators and up can change a different player's status.
 * A different player can have his uuid added/removed when his name is typed following "/trample".
 * Uses trampleList.yml in HoesMad folder
 * @author JK_Bizcuits
 * @version 420.1
 */

package me.JK_Bizcuits.HoesMad;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TrampleCommand implements CommandExecutor {
	
	FileConfiguration trampleList;
	Main plugin;
	
	public TrampleCommand(Main main) {
		plugin = main;
		trampleList = YamlConfiguration.loadConfiguration(plugin.getFile());
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("trample")) {// if the command is trample
			if(sender instanceof Player) {//if the sender is a player
				Player player = (Player) sender;
				if(args.length == 0) {//if no name follows /trample
					listCheck(player.getUniqueId().toString());
					return true;
				}
				if(args.length == 1) {//if the command includes a name
					if(player.hasPermission("trample")) {//if the player has permission
						if(Bukkit.getPlayerExact(args[0])==null){//if the name isn't online
							sender.sendMessage("not a player (online atleast)");
							return true;
						}
						listCheck(Bukkit.getPlayerExact(args[0]).getUniqueId().toString());
						return true;
					}
					else {
						player.sendMessage("You do not have permission for this command");
						return true;
					}
				}
				else {
					player.sendMessage("You can only change one player at a time");
					return true;
				}
			}
			else {
				sender.sendMessage("Begone non-player!");
				return true;
			}
		}
		return false;
	}//end onCommand
	
	/*
	 * Check if player is on list
	 * If player is on list, remove the player
	 * If the player is not on the list, add the player
	 */
	
	public void listCheck(String player) {
		if(trampleList.contains(player)) {
			trampleList.set(player, null);		
		}
		else {
			trampleList.createSection(player);
		}
		save();
	}//end listCheck
	
	
	/*
	 * Save changes to trampleList.yml
	 */
	
	public void save() {
	      try {
			trampleList.save(plugin.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}//end save
}
