package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.charter.orion_pax.OasisChat.OasisChat;
import net.charter.orion_pax.OasisChat.PartyPlayer;

public class PCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		String name = player.getName();
		Team myparty = null;
		PartyPlayer mypartyplayer = plugin.partyPlayer.get(name);
		if (mypartyplayer.getMyParty()!=null){
			myparty = plugin.partyPlayer.get(name).getMyParty();
		}
		String pcprefix = plugin.pcprefix;
		String pncprefix = plugin.pncprefix;
		
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (myparty == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.pcprefix)+ "You're not part of a party chat!");
					return true;
				}
				if (plugin.partyPlayer.get(name).getPToggle()) {

					plugin.partyPlayer.get(name).setPToggle(false);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix+ "Playerchat "+ ChatColor.RED+ "DISABLED"));
				} else {
					plugin.partyPlayer.get(name).setPToggle(true);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.pcprefix+ "Playerchat "+ ChatColor.GREEN+ "ENABLED"));
				}
			}
		} else {
			if (myparty!=null) {
				String prefix = pcprefix + "<" + pncprefix + myparty.getName() + pcprefix + "> - " + pncprefix + sender.getName() + pcprefix + ": ";
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[0]);

				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}

				String message = buffer.toString();
				mypartyplayer.sendTeamChat(prefix + message);
			}
		}
		return false;
	}
}
