package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PartyinfoCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PartyinfoCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length == 1) {
			if (plugin.party.getParties().contains(args[0])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "PartyChat: " + args[0]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Owner: " + plugin.party.getOwner(args[0]));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Password: " + plugin.party.getPassword(args[0]));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix) + "Members: " + plugin.party.getMembers(args[0]).toString());
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Online: "+ plugin.party.getOnlineMembers(args[0],player).toString());
				return true;
			}
		} else if (plugin.partyspy.containsKey(player.getName())) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "PartyChat: "+ plugin.partyspy.get(player.getName()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Password: "+ plugin.party.getPassword(plugin.partyspy.get(player.getName())));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Members: "+ plugin.party.getMembers(plugin.partyspy.get(player.getName())).toString());
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Online: "+ plugin.party.getOnlineMembers(plugin.partyspy.get(player.getName()),player).toString());
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Usage: /pinfo <partyname> (partyname not needed if in party spying already!");
			return true;
		}
		return true;
	}
}
