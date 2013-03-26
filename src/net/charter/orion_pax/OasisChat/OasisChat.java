package net.charter.orion_pax.OasisChat;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class OasisChat extends JavaPlugin {

    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    HashMap<String, String> chattoggle = new HashMap<String, String>();
    HashMap<String, String> partyhash = new HashMap<String, String>();
    HashMap<String, String> invite = new HashMap<String, String>();
    HashMap<String, String> partyspy = new HashMap<String, String>();
    HashMap<String,PermissionAttachment> perms = new HashMap<String,PermissionAttachment>();
    String aquaprefix = (char)27+"[1;36m";
    String aquasufix = (char)27+"[22;39m";
    String greenprefix = (char)27+"[1;32m";
    String greensufix = (char)27+"[22;39m";
    ChatColor pcprefix; //PartyChat Color prefix
    ChatColor pncprefix; //PlayerName Color prefix
    ChatColor sncprefix; //StaffNameChat Color prefix
    ChatColor acprefix; //AdminChat Color prefix

    PartyChat party = new PartyChat(this);

    @Override
    public void onEnable() {
	this.saveDefaultConfig();
	Bukkit.getPluginManager().registerEvents(new OasisChatListener(this), this);

	getCommand("a").setExecutor(new OasisChatCommand(this));
	getCommand("staff").setExecutor(new OasisChatCommand(this));
	getCommand("oasischat").setExecutor(new OasisChatCommand(this));
	getCommand("p").setExecutor(new OasisChatCommand(this));
	getCommand("party").setExecutor(new OasisChatCommand(this));
	getCommand("psay").setExecutor(new OasisChatCommand(this));
	getCommand("pspyon").setExecutor(new OasisChatCommand(this));
	getCommand("pspyoff").setExecutor(new OasisChatCommand(this));
	getCommand("listparties").setExecutor(new OasisChatCommand(this));
	getCommand("partyinfo").setExecutor(new OasisChatCommand(this));

	setup();
	setupconfig();

	getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
    }

    @Override
    public void onDisable(){
	getLogger().info(aquaprefix+"OasisChat has been disabled!"+aquasufix);
    }

    public void loadconfig(){

    }

    public String effects(){
	StringBuilder sb = new StringBuilder();
	for (PotionEffectType pe : PotionEffectType.values()) {
	    if (sb.length() > 0) {
		sb.append(", "); //separate values with a comma and space
	    }
	    if (pe != null){
		sb.append(pe.getName());
	    }
	}
	return sb.toString();
    }

    public void save(){

	for(Entry<String, String> entry : partyhash.entrySet())
	{
	    this.getConfig().set("partychats." + entry.getKey(), entry.getValue());
	}

	this.saveConfig();
    }

    public void load(){

	for(String key : this.getConfig().getConfigurationSection("partychats").getKeys(false))
	{
	    ConfigurationSection value = this.getConfig().getConfigurationSection(key);
	    partyhash.put(key, this.getConfig().getString("partychats." + key));
	}
    }

    public void setupconfig(){
	if (!(getConfig().contains("partychats"))){
	    getConfig().createSection("partychats");
	    saveConfig();
	}
    }

    public void setup(){
	acprefix = getchatcolor(this.getConfig().getConfigurationSection("ingameconfigurable").getInt("adminchatcolor"));
	pcprefix = getchatcolor(this.getConfig().getConfigurationSection("ingameconfigurable").getInt("partychatcolor"));
	sncprefix = getchatcolor(this.getConfig().getConfigurationSection("ingameconfigurable").getInt("staffnamechatcolor"));
	pncprefix = getchatcolor(this.getConfig().getConfigurationSection("ingameconfigurable").getInt("playernamechatcolor"));
    }

    public ChatColor getchatcolor(int color){
	switch(color){

	case 0:  return ChatColor.BLACK;
	case 1:  return ChatColor.DARK_BLUE;
	case 2:  return ChatColor.DARK_GREEN;
	case 3:  return ChatColor.DARK_AQUA;
	case 4:  return ChatColor.DARK_RED;
	case 5:  return ChatColor.DARK_PURPLE;
	case 6:  return ChatColor.GOLD;
	case 7:  return ChatColor.GRAY;
	case 8:  return ChatColor.DARK_GRAY;
	case 9:  return ChatColor.BLUE;
	case 10: return ChatColor.GREEN;
	case 11: return ChatColor.AQUA;
	case 12: return ChatColor.RED;
	case 13: return ChatColor.LIGHT_PURPLE;
	case 14: return ChatColor.YELLOW;
	case 15: return ChatColor.WHITE;
	default: return ChatColor.AQUA;    
	}
    }

    void printStackTrace(Throwable t){
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	t.printStackTrace(pw);
	for(String l: sw.toString().replace("\r", "").split("\n"))
	    getServer().broadcast(l, "oasis.staff.dev");
	pw.close();
	try {
	    sw.close();
	} catch(IOException e) {

	}
    }

}
