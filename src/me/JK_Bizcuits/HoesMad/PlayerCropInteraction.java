/**
 * PlayerCropInteraction will listen for players that clock on crops to destroy them. 
 * If the player has a hoe, then the crops will drop items. Otherwise there won't be any drops.
 * List of affected crops are in config.yml.
 * @author JK_Bizcuits
 * @version 420.1
 */
package me.JK_Bizcuits.HoesMad;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerCropInteraction implements Listener {
	Main plugin;
	int x;//will be used to calculate drop amount in block break event
	boolean properTool = false;//flag to check if proper tool is used
	boolean fullyGrown = false;//flag to check if crop is grown
	
	public PlayerCropInteraction(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		properTool = false;//make sure tool flag starts as false
		plugin.getConfig().getConfigurationSection("crops").getKeys(false).forEach(crop ->{//check crops section of config.yml
			if(crop.equalsIgnoreCase(event.getBlock().getType().toString())) {//check if block equals the crop
				World world = event.getPlayer().getWorld();
				Random r = new Random();
				event.setDropItems(false);//stop items from dropping
				ItemStack[] items = new ItemStack[plugin.getConfig().getStringList("crops." + crop).size()];
				x=0;
				checkTool(event);//check if there's a tool present and which tool
				BlockData cropAge = event.getBlock().getBlockData();//checking if crop is ripe enough
				int age = 0;
				int maxAge = 1;
				if (cropAge instanceof Ageable) {
				    Ageable ageable = ((Ageable) cropAge);
				    age = ageable.getAge();
				    maxAge = ageable.getMaximumAge();    
				}
				switch(crop) {//check the type of crop and then spawning drops accordingly
				case "BEETROOTS":
					if(age == maxAge) {
						fullyGrown = true;
						spawnDrops(prepareDrops(crop, items, r, false), world, event);
					}
					break;
				case "CARROTS":
					if(age == maxAge) {
						fullyGrown = true;
						spawnDrops(prepareDrops(crop, items, r, false), world, event);
					}
					break;
				case "POTATOES":
					if(age == maxAge) {
						fullyGrown = true;
						spawnDrops(prepareDrops(crop, items, r, false), world, event);
					}
					break;
				case "WHEAT":
					if(age == maxAge) {
						fullyGrown = true;
						spawnDrops(prepareDrops(crop, items, r, false), world, event);
					}
					break;
				case "MELON":
					spawnDrops(prepareDrops(crop, items, r, false), world, event);
					break;
				case "PUMPKIN":
					spawnDrops(prepareDrops(crop, items, r, false), world, event);
					break;
				case "PUMPKIN_STEM":
					spawnDrops(prepareDrops(crop, items, r, true), world, event);
					break;
				case "ATTACHED_PUMPKIN_STEM":
					spawnDrops(prepareDrops(crop, items, r, true), world, event);
					break;
				case "ATTACHED_MELON_STEM":
					spawnDrops(prepareDrops(crop, items, r, true), world, event);
					break;
				case "MELON_STEM":
					spawnDrops(prepareDrops(crop, items, r, true), world, event);
					break;
				default:
					break;
				}			
			}
		});
	}//end BlockBreakEvent
	
	/*
	 * checks if the crop would only drop seeds (like a melon stem)
	 * if there's a tool, the x should be positive so as to make a drop possible
	 * if there are two drops (a crop and seed) then seeds follow crops and so the x will be assigned a random variable on the second run
	 */	
	public ItemStack[] prepareDrops(String crop, ItemStack[] items, Random r, boolean seedOnly) {	
		ItemStack item = null;
		int position = 0;
		if(seedOnly == true) {
			x=r.nextInt(3) + 1;
		}
		for(String drop: plugin.getConfig().getStringList("crops." + crop)) {
			try {//just in case there's an error in the config, a dirt block is dropped instead
				if(x>0) {
					item = new ItemStack(Material.matchMaterial(drop), (1*x) );
				}
			}
			catch(Exception e){
				item = new ItemStack(Material.DIRT);
			}
			items[position] = item;
			position++;
			x=r.nextInt(3) + 1;
		}
		return items;
	}//end prepareDrops
	
	/*
	 * just makes sure that both flags are positive before spawning the drops
	 */
	public void spawnDrops(ItemStack[] items, World world, BlockBreakEvent event) {
		for(ItemStack y: items) {
			if(properTool == true && fullyGrown == true) {
				world.dropItemNaturally(event.getBlock().getLocation(), y);
			}
		}
	}//end spawnDrops
	
	/*
	 * first checks to see if player is holding a tool that is proper for harvest
	 * adds damage to tool
	 * then changes x variable to compensate for appropriate hoe tier and adds 1 for every level of fortune
	 */
	public void checkTool(BlockBreakEvent event) {
		for(String tool: plugin.getConfig().getStringList("tools")){//check tools section of config.yml
			if(tool.equals(event.getPlayer().getInventory().getItemInMainHand().getType().toString())) {//check if player has listed tool
				properTool=true;
				ItemStack toolInHand = event.getPlayer().getInventory().getItemInMainHand();
				ItemMeta toolInHandMeta = toolInHand.getItemMeta();
		        Damageable damageable = ((Damageable) toolInHandMeta);
		        damageable.setDamage(damageable.getDamage() + 1);
		        if(toolInHand.getType().getMaxDurability()==damageable.getDamage()) {
		        	
		        	event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 50, 1);
		        	event.getPlayer().getInventory().setItemInMainHand(null);
		        	//event.getPlayer().spawnParticle(Particle.ITEM_CRACK, event.getPlayer().getLocation(), 20);
		        }
		        
				toolInHand.setItemMeta(toolInHandMeta);
				if(tool.equals("DIAMOND_HOE")) {//if player has listed tool, change x appropriately
					x=3;
				}
				else if(tool.equals("GOLDEN_HOE") || tool.equals("IRON_HOE")) {
					x=2;
				}
				else {
					x=1;
				}
				x += toolInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}		
		}
	}//end checkTool
}
