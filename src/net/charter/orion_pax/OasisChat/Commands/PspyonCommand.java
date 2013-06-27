package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PspyonCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PspyonCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (plugin.getConfig().contains("partychats." + args[0])) {
				if (!(plugin.partyspy.containsKey(sender.getName()))) {
					plugin.partyspy.put(sender.getName(), args[0]);
					plugin.perms.get(sender.getName()).setPermission("oasischat.party." + args[0],true);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You are now watching " + args[0]);
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You must quit your current partyspy before spying on another!");
					return true;
				}
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Usage: /pjoin <partyname> Also do /plist to get a list of online party chats!");
			return true;
		}
		return false;
	}
}
