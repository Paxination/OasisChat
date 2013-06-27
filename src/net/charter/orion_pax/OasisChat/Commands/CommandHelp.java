package net.charter.orion_pax.OasisChat.Commands;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.ChatColor;

public class CommandHelp {

	private OasisChat plugin;
	
	public CommandHelp(OasisChat plugin){
		this.plugin = plugin;
	}
	String[] oasischatsub = {
			ChatColor.GOLD + "Usage: /oasischat subcommand [args]"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "SAVE - Saves config"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "LIST - List settings that can be changed in game"
			,ChatColor.GOLD + "SET - Sets in game settings for oasischat"
			,ChatColor.GOLD + "DEBUG - turns on debug."
	}; 

	String[] partychatsub = {
			ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "Usage: /party subcommand [args]")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "SubCommands:")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "CREATE partyname password (password optional)")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "JOIN partyname password (password option)")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "INVITE playername - invites player to partychat")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "ACCEPT - accepts invite, 5 min time limit")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "KICK playername")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "List - list members of your party")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "PASSWORD password")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "GIVE playername")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "QUIT - quits current party chat")
	}; 
}
