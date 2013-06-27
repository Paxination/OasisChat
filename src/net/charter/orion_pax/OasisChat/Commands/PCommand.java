package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		String myparty = plugin.party.myParty(player);
		
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (myparty == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You're not part of a party chat!");
					return true;
				}
				if (plugin.partychattoggle.get(sender.getName()) != "off") {

					plugin.partychattoggle.put(sender.getName(),"off");
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "Playerchat "+ ChatColor.RED+ "DISABLED");
				} else {
					plugin.partychattoggle.put(sender.getName(),myparty);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Playerchat "+ ChatColor.GREEN+ "ENABLED");
				}
			}
		} else {
			if (plugin.partyhash.containsKey(sender.getName())) {
				String prefix = ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "<"+ ChatColor.translateAlternateColorCodes('&', OasisChat.pncprefix)+ myparty+ ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "> - "+ ChatColor.translateAlternateColorCodes('&', OasisChat.pncprefix)+ sender.getName()+ ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + ": ";
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[0]);

				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}

				String message = buffer.toString();
				plugin.console.sendMessage(prefix + message);
				plugin.getServer().broadcast(prefix + ChatColor.translateAlternateColorCodes('&', message),plugin.partyhash.get(sender.getName()));
			}
		}
		return false;
	}
}
