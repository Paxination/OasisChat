package net.charter.orion_pax.OasisChat.Commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Set;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OasisChatCommand implements CommandExecutor {
	
	private OasisChat plugin;
	
	public OasisChatCommand(OasisChat plugin){
		this.plugin = plugin;
	}
	
	CommandHelp helper = new CommandHelp(plugin);
	
	boolean disable = false;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//Player player = (Player) sender;
		
		if (args.length == 0) {
			sender.sendMessage(helper.oasischatsub);
			return true;
		}
		if (args[0].equalsIgnoreCase("save")) {
			plugin.partytask.cancel();
			plugin.saveConfig();
			plugin.partytask.runTaskTimer(plugin, 10, 12000);
			sender.sendMessage(ChatColor.GOLD + "Config Saved!");
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			plugin.partytask.cancel();
			plugin.reloadConfig();
			plugin.partytask.runTaskTimer(plugin, 10, 12000);
			plugin.setup();
			sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
			return true;
		}
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(ChatColor.GOLD
					+ "These are the available options you can change in game!");
			sender.sendMessage(plugin.getConfig()
					.getConfigurationSection("ingameconfigurable")
					.getKeys(false).toString());
			return true;
		}
		if (args[0].equalsIgnoreCase("set")) {
			if (args.length < 3) {
				sender.sendMessage(ChatColor.GOLD
						+ "Usage: /oasischat set key value");
				return true;
			}
			if (keyexist(args[1])) {
				if (args[1].contains("chatcolor")) {
					plugin.getConfig().set(
							"ingameconfigurable." + args[1],
							args[2]);
				}
				plugin.setup();
				sender.sendMessage(ChatColor.GOLD
						+ "Config successfully changed!  Dont forget to /oasischat save!");
				return true;
			} else {
				sender.sendMessage(ChatColor.GOLD
						+ args[1]
								+ " is not a defined key in the config. Do /oasischat list for a list of keys!");
				return true;
			}
		}
		sender.sendMessage(helper.oasischatsub);
		return false;
	}

	public boolean keyexist(String key){
		if (plugin.getConfig().getConfigurationSection("ingameconfigurable").getKeys(false).toString().contains(key)){
			return true;
		}

		return false;
	}

}
