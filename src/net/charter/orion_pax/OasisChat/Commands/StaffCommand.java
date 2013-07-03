package net.charter.orion_pax.OasisChat.Commands;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {

	private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

	public StaffCommand(OasisChat plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes(
				'&', plugin.acprefix) + "[STAFF ONLINE]");
		Player[] onlinePlayers = Bukkit.getServer()
				.getOnlinePlayers();
		for (Player oplayer : onlinePlayers) {
			if ((oplayer != null)
					&& (oplayer.hasPermission("OasisChat.staff.a"))) {
				sender.sendMessage(ChatColor
						.translateAlternateColorCodes('&',
								plugin.acprefix)
								+ oplayer.getName());
			}
		}
		return true;
	}

}
