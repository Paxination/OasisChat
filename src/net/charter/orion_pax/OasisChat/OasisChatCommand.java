package net.charter.orion_pax.OasisChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OasisChatCommand implements CommandExecutor {
	
	//new OasisChatCommand(OasisChat);
	
	private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class
	
	public OasisChatCommand(OasisChat plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if (cmd.getName().equalsIgnoreCase("reloadchat")){
			plugin.reloadConfig();
			plugin.amount = plugin.getConfig().getInt("amount");
		}
		if (cmd.getName().equalsIgnoreCase("enableme")){
			plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.GREEN + "ENABLED!");
		}
		if (cmd.getName().equalsIgnoreCase("disableme")){
			plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.RED + "DISABLED!");
		}
		if (cmd.getName().equalsIgnoreCase("staff")) {
			if (!(sender instanceof Player)){
				plugin.getLogger().info(plugin.aquaprefix+"STAFF ONLINE"+plugin.aquasufix);
				Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
			    for (Player oplayer : onlinePlayers){
			    	if ((oplayer != null) && (oplayer.hasPermission("OasisChat.staff"))){
			    		plugin.getLogger().info(plugin.aquaprefix+oplayer.getName()+plugin.aquasufix);
			    	}
			    }
			} else {
				sender.sendMessage(ChatColor.AQUA + "STAFF ONLINE");
				Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
				for (Player oplayer : onlinePlayers){
					if ((oplayer != null) && (oplayer.hasPermission("OasisChat.staff"))){
						sender.sendMessage(ChatColor.AQUA + oplayer.getName());
					}
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("a")) {
			if (args.length == 0) {
				if (sender instanceof Player){
					if(sender.hasPermission("OasisChat.staff")){
						if (plugin.staff.contains(sender.getName())){
							plugin.staff.remove(sender.getName());
							sender.sendMessage(ChatColor.AQUA + "Adminchat " + ChatColor.RED + "DISABLED");
						} else {
							plugin.staff.add(sender.getName());
							sender.sendMessage(ChatColor.AQUA + "Adminchat " + ChatColor.GREEN + "ENABLED");
						}
					}
				}
				
				
			
				
				return false;
			} else {
				String prefix = ChatColor.AQUA + "{" + ChatColor.WHITE + sender.getName() + ChatColor.AQUA + "} ";
				StringBuffer buffer = new StringBuffer();
	            buffer.append(args[0]);

	            for (int i = 1; i < args.length; i++) {
	                buffer.append(" ");
	                buffer.append(args[i]);
	            }

	            String message = buffer.toString();
	            plugin.getLogger().info(plugin.aquaprefix+"<A> " + sender.getName() + " - " + message+ plugin.aquasufix);
				Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
			    for (Player oplayer : onlinePlayers){
			    	if ((oplayer != null) && (oplayer.hasPermission("OasisChat.staff"))){
			    		oplayer.sendMessage(prefix + message);
			    	}
			    }
			    return true;
			}
		}
		return false;
	}
}
