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
		plugin.partyPlayer.get(sender.getName()).setPartySpyChat("");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "You have stopped spying!"));
		return true;
	}
}
