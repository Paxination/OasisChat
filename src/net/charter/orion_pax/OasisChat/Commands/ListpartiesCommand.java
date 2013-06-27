package net.charter.orion_pax.OasisChat.Commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisChat.OasisChat;

public class ListpartiesCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public ListpartiesCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Set<String> parties = plugin.getConfig().getConfigurationSection("partychats").getKeys(false);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + parties.toString());
		return true;
	}
}
