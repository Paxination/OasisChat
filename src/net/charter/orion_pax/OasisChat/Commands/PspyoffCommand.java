package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PspyoffCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PspyoffCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		plugin.perms.get(sender.getName()).unsetPermission("oasischat.party."+ plugin.partyspy.get(sender.getName()));
		plugin.partyspy.remove(sender.getName());
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "You have stopped spying!");
		return true;
	}
}
