package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PsayCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PsayCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (plugin.partyspy.containsKey(sender.getName())) {
			String prefix = ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "<"+ ChatColor.translateAlternateColorCodes('&',OasisChat.pncprefix)+ plugin.partyspy.get(sender.getName())+ ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "> - "+ ChatColor.translateAlternateColorCodes('&',OasisChat.pncprefix)+ sender.getName()+ ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix) + ": ";
			StringBuffer buffer = new StringBuffer();
			buffer.append(args[0]);

			for (int i = 1; i < args.length; i++) {
				buffer.append(" ");
				buffer.append(args[i]);
			}

			String message = buffer.toString();
			plugin.console.sendMessage(prefix + message);
			plugin.getServer().broadcast(prefix + message,"oasischat.party."+ plugin.partyspy.get(sender.getName()));
		}
		return false;
	}
}
