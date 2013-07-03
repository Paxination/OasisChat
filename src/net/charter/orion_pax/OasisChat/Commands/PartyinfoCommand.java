package net.charter.orion_pax.OasisChat.Commands;

import java.awt.List;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisChat.OasisChat;
import net.charter.orion_pax.OasisChat.PartyPlayer;

public class PartyinfoCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PartyinfoCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		String name = player.getName();
		String pcprefix = plugin.pcprefix;
		
		if (args.length == 1) {
			if (plugin.MyParties.containsKey(args[0])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "PartyChat: " + args[0]));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Owner: " + plugin.MyParties.get(args[0]).getOwner()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Password: " + plugin.MyParties.get(args[0]).getPassword()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + plugin.MyParties.get(args[0]).getMembers()));
				Iterator it = plugin.partyPlayer.entrySet().iterator();
				List mlist = null;
				while (it.hasNext()){
					Map.Entry entry = (Map.Entry)it.next();
					PartyPlayer partyplayer = (PartyPlayer) entry.getValue();
					if (partyplayer.getMyParty().equals(args[0])){
						mlist.add(partyplayer.getName());
					}
					it.remove();
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Online: " + mlist));
				return true;
			}
		} else if (plugin.partyPlayer.get(name).getPartySpyChat()!="") {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "PartyChat: " + plugin.partyPlayer.get(name).getPartySpyChat()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Owner: " + plugin.MyParties.get(plugin.partyPlayer.get(name).getPartySpyChat()).getOwner()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Password: " + plugin.MyParties.get(plugin.partyPlayer.get(name).getPartySpyChat()).getPassword()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + plugin.MyParties.get(plugin.partyPlayer.get(name).getPartySpyChat()).getMembers()));
			Iterator it = plugin.partyPlayer.entrySet().iterator();
			List mlist = null;
			while (it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				PartyPlayer partyplayer = (PartyPlayer) entry.getValue();
				if (partyplayer.getMyParty().equals(plugin.partyPlayer.get(name).getPartySpyChat())){
					mlist.add(partyplayer.getName());
				}
				it.remove();
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Online: " + mlist));
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',pcprefix)+ "Usage: /pinfo <partyname> (partyname not needed if in party spying already!");
			return true;
		}
		return true;
	}
}
