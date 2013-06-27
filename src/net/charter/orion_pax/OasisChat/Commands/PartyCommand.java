package net.charter.orion_pax.OasisChat.Commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisChat.OasisChat;

public class PartyCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PartyCommand(OasisChat plugin){
		this.plugin = plugin;
	}
	
	CommandHelp helper = new CommandHelp(plugin);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(helper.partychatsub);
			return true;
		}
		String password = "";
		if (args[0].equalsIgnoreCase("create")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);
			if (args.length > 1) {
				if (args.length == 3) {
					password = args[2];
				}
				if (plugin.party.isOwner(player)) {
					plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + myparty + " disbanded!",plugin.partyhash.get(player.getName()));
					plugin.party.delParty(myparty);
				} else if (plugin.party.isMember(player)) {
					plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ player.getName()+ " has left chat!",plugin.partyhash.get(player.getName()));
					plugin.party.delMember(player.getName(), myparty);
				}
				plugin.party.createParty(player,args[1], password);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ args[1]+ " has been created!");
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "Usage: /party create <partyname> <password> (password is optional)");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("quit")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);
			if (plugin.party.isOwner(player)) {
				plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + myparty + " disbanded!", plugin.partyhash.get(player.getName()));
				plugin.party.partyspydel(myparty);
				plugin.party.delParty(myparty);
			} else if (plugin.party.isMember(player)) {
				plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + player.getName() + " has left chat!", plugin.partyhash.get(player.getName()));
				plugin.party.delMember(player.getName(), myparty);
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You are not in a party!");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("join")) {
			Player player = (Player) sender;
			
			if (args.length > 1) {
				if (args.length == 3) {
					password = args[2];
				}
				if (plugin.party.getParties().toString().contains(args[1])) {
					if (plugin.party.getPassword(args[1]).equals(password)) {
						plugin.party.addMember(player, args[1]);
						plugin.partyhash.put(player.getName(), "oasischat.party." + args[1]);
						player.addAttachment(plugin, "oasischat.party." + args[1], true);
						plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + player.getName() + " has joined " + args[1] + "!", "oasischat.party." + args[1]);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Incorrect password!");
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "The party " + args[1] + " does not exist!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Usage: /party join <partyname> <password> (password optional)");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("kick")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);

			if (args.length > 1) {
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					if (plugin.getServer().getPlayer(args[1]).hasPlayedBefore()) {
						OfflinePlayer otarget = plugin.getServer().getOfflinePlayer(args[1]);
						if (plugin.party.isMember((Player) otarget)) {
							plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + otarget.getName() + " has been kicked from " + myparty + "!", plugin.partyhash.get(player.getName()));
							plugin.party.delMember(otarget.getName(), myparty);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + plugin.getServer().getOfflinePlayer(args[1]).getName() + " is not a member of your party!");
							return true;
						}
					}
				}
				if (target != null) {
					if (plugin.party.isOwner(player)) {
						if (plugin.party.isMember(target)) {
							plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + target.getName() + " has been kicked from " + myparty + "!", plugin.partyhash.get(player.getName()));
							plugin.party.delMember(target.getName(), myparty);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + target.getName() + " is not a member of your party!");
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You're not the party owner!");
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + args[1] + " is not online!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Usage: /party kick <playername>");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("invite")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);

			if (args.length > 1) {
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + args[1] + " is not online!");
					return true;
				}
				if (plugin.party.isMember(target)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + target.getName() + " is allready a member of a party!");
					return true;
				}
				if (target.isOnline() && (plugin.party.isOwner(player))) {
					plugin.invite.put(target.getName(), myparty);
					timer(target);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + target.getName() + " has been invited to " + myparty + "!");
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + sender.getName() + " has invited you to " + myparty + "!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Usage: /party invite <playername>");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("accept")) {
			Player player = (Player) sender;

			if (plugin.party.isMember(player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You must leave your current party first!");
				return true;
			} else if (plugin.party.isOwner(player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You must leave your current party first!");
				return true;
			} else if (!(plugin.invite.containsKey(player.getName()))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You have no invitations pending!");
				return true;
			} else {
				plugin.party.addMember(player, plugin.invite.get(player.getName()));
				plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + player.getName() + " has joined " + plugin.invite.get(player.getName()) + "!", plugin.partyhash.get(player.getName()));
				plugin.invite.remove(player.getName());
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("give")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);

			if (args.length == 2) {
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + args[1] + " is not online!");
				} else if ((!(plugin.party.isOwner(target))) || (!(plugin.party.isMember(target)))) {
					plugin.party.setOwner(target, player, myparty);
					plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Ownership has been given to " + target.getName() + "!", plugin.partyhash.get(target.getName()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Ownership was been transfered!");
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "They are already part of a party!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Usage: /party give <playername>");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("password")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);
			
			if (args.length == 2) {
				if (plugin.party.isOwner(player)) {
					plugin.party.setPassword(player, args[1], myparty);
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "You are not an owner of a party!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Usage: /party password <newpassword>");
			}
		}
		if (args[0].equalsIgnoreCase("list")) {
			Player player = (Player) sender;
			String myparty = plugin.party.myParty(player);

			if (plugin.party.isMember(player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Members: " + plugin.party.getMembers(myparty));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Online: " + plugin.party.getOnlineMembers(myparty, player).toString());
				return true;
			} else if (plugin.party.isOwner(player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "PartyChat: " + myparty);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Password: " + plugin.party.getPassword(myparty));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Members: " + plugin.party.getMembers(myparty).toString());
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Online: " + plugin.party.getOnlineMembers(myparty, player).toString());
				return true;
			}
		}
		sender.sendMessage(helper.partychatsub);
		return false;
	}
	
	public void timer(final Player player){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable()
		{
			public void run()
			{
				plugin.invite.remove(player.getName());
			}
		}
		, 6000);
	}
	
}
