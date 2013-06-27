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
				if (args.length == 0) {
					if (sender instanceof Player) {
						if (plugin.adminchattoggle.get(sender.getName()).equalsIgnoreCase("on")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.acprefix)+"Adminchat " + ChatColor.RED + "DISABLED");
							plugin.adminchattoggle.put(sender.getName(), "off");
						} else {
							plugin.adminchattoggle.put(sender.getName(),"on");
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.acprefix)+ "Adminchat "+ ChatColor.GREEN+ "ENABLED");
						}
					}
				} else {
					String prefix = ChatColor.translateAlternateColorCodes(
							'&', OasisChat.acprefix)
							+ "{"
							+ ChatColor.translateAlternateColorCodes('&',
									OasisChat.sncprefix)
									+ sender.getName()
									+ ChatColor.translateAlternateColorCodes('&',
											OasisChat.acprefix) + "} ";
					StringBuffer buffer = new StringBuffer();
					buffer.append(args[0]);

					for (int i = 1; i < args.length; i++) {
						buffer.append(" ");
						buffer.append(args[i]);
					}

					String message = buffer.toString();
					plugin.getServer().broadcast(prefix + ChatColor.translateAlternateColorCodes('&', message),
							"oasischat.staff.a");
				}
				return false;
			}
}
