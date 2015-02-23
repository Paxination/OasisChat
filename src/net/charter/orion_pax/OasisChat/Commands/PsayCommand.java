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
		String pcprefix = plugin.pcprefix;
		String pncprefix = plugin.pncprefix;
		String acprefix = plugin.acprefix;
		if (plugin.partyPlayer.get(sender.getName()).getPartySpyChat()!="") {
			String prefix = pcprefix + "<" + pncprefix + plugin.partyPlayer.get(sender.getName()).getPartySpyChat() + pcprefix + "> - " + acprefix + sender.getName() + pcprefix + ": ";
			StringBuffer buffer = new StringBuffer();
			buffer.append(args[0]);

			for (int i = 1; i < args.length; i++) {
				buffer.append(" ");
				buffer.append(args[i]);
			}

			String message = buffer.toString();
			plugin.console.sendMessage(prefix + message);
			plugin.MyParties.get(plugin.partyPlayer.get(sender.getName()).getPartySpyChat()).sendMessage(prefix + message);
		}
		return false;
	}
}
