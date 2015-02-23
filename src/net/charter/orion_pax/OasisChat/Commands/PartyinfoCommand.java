package net.charter.orion_pax.OasisChat.Commands;

import java.awt.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

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
			if (plugin.SMPboard.getTeam(args[0])!=null) {
				Team thisteam = plugin.SMPboard.getTeam(args[0]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "PartyChat: " + args[0]));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Owner: " + plugin.MyParties2.get(thisteam)));
				//sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Password: " + plugin.MyParties.get(args[0]).getPassword()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + thisteam.getPlayers().toString()));
				List mlist = null;
				for (OfflinePlayer teamplayer: thisteam.getPlayers()){
					if (teamplayer.isOnline()){
						mlist.add(teamplayer.getName());
					}
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Online: " + mlist));
				return true;
			}
		} else if (plugin.partyPlayer.get(name).getPartySpyChat()!=null) {
			Team thisteam=plugin.partyPlayer.get(name).getPartySpyChat();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "PartyChat: " + args[0]));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Owner: " + plugin.MyParties2.get(thisteam)));
			//sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Password: " + plugin.MyParties.get(args[0]).getPassword()));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + thisteam.getPlayers().toString()));
			List mlist = null;
			for (OfflinePlayer teamplayer: thisteam.getPlayers()){
				if (teamplayer.isOnline()){
					mlist.add(teamplayer.getName());
				}
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
