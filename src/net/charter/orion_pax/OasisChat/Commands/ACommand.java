package net.charter.orion_pax.OasisChat.Commands;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ACommand implements CommandExecutor {

			private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

			public ACommand(OasisChat plugin){
				this.plugin = plugin;
			}

			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				String name = sender.getName();
				String acprefix = plugin.acprefix;
				String sncprefix = plugin.sncprefix;
				
				if (args.length == 0) {
					if (sender instanceof Player) {
						if (plugin == null){
							plugin.getLogger().info("PLUGIN");
						}
						if (plugin.partyPlayer==null){
							plugin.getLogger().info("PARTYPLAYER");
						}
						if (name==null){
							plugin.getLogger().info("name");
						}
						if (plugin.partyPlayer.get(name)==null){
							plugin.getLogger().info("partyplayer.get(name)");
						}
						if (plugin.partyPlayer.get(name).getAToggle()) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', acprefix)+"Adminchat " + ChatColor.RED + "DISABLED");
							plugin.partyPlayer.get(name).setAToggle(false);
						} else {
							plugin.partyPlayer.get(name).setAToggle(true);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',acprefix)+ "Adminchat "+ ChatColor.GREEN+ "ENABLED");
						}
					}
				} else {
					String prefix = acprefix + "{" + sncprefix + sender.getName() + acprefix + "} ";
					StringBuffer buffer = new StringBuffer();
					buffer.append(args[0]);

					for (int i = 1; i < args.length; i++) {
						buffer.append(" ");
						buffer.append(args[i]);
					}

					String message = buffer.toString();
					plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', prefix + message),"oasischat.staff.a");
				}
				return false;
			}
}
