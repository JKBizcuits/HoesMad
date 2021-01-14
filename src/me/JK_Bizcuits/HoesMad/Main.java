/**
 * It's the Main. It enables the commands and listeners and whatever the plugin will do.
 * @author JK_Bizcuits
 * @version 420.1
 */

package me.JK_Bizcuits.HoesMad;


import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	File file;
	
	@Override
	public void onEnable() {
		file = new File(getDataFolder(), "trampleList.yml");//making the trample list if it doesn't exist
		if (file.exists() == false) {
		      file.getParentFile().mkdirs();
		      try {
		         file.createNewFile();
		      } catch (IOException ex) {
		         ex.printStackTrace();
		      }
		   }
		
		this.getCommand("Trample").setExecutor(new TrampleCommand(this));//enable trample command
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new PlayerCropInteraction(this), this);//enable PlayerCropInteraction
		this.getServer().getPluginManager().registerEvents(new PistonCropInteraction(this), this);//enable PistonCropInteraction
		this.getServer().getPluginManager().registerEvents(new WaterCropInteraction(this), this);//enable WaterCropInteraction
		this.getServer().getPluginManager().registerEvents(new TrampleInteraction(this), this);//enable TrampleInteraction
	}
	
	@Override
	public void onDisable() {
		
	}
	
	/*
	 * Just a getter for the trampleList.yml
	 */
	public File getFile() {
		return file;
	}
	
}
