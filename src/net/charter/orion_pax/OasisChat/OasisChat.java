package net.charter.orion_pax.OasisChat;


import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class OasisChat extends JavaPlugin {

	ArrayList staff = new ArrayList();
	String aquaprefix = (char)27+"[1;36m";
	String aquasufix = (char)27+"[22;39m";
	int amount;
	
	@Override
    public void onEnable() {
		this.saveDefaultConfig();
		amount = this.getConfig().getInt("amount");
		Bukkit.getPluginManager().registerEvents(new OasisChatListener(this), this);
		
    	getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
    	
    	getCommand("a").setExecutor(new OasisChatCommand(this));
    	getCommand("staff").setExecutor(new OasisChatCommand(this));
    	getCommand("enableme").setExecutor(new OasisChatCommand(this));
    	getCommand("disableme").setExecutor(new OasisChatCommand(this));
    	getCommand("reloadchat").setExecutor(new OasisChatCommand(this));
    }
	
	@Override
	public void onDisable(){
		getLogger().info(aquaprefix+"OasisChat has been disabled!"+aquasufix);
	}
	
}
