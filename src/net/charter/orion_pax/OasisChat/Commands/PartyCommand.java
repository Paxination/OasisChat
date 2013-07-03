package net.charter.orion_pax.OasisChat.Commands;

import java.awt.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisChat.OasisChat;
import net.charter.orion_pax.OasisChat.Parties;
import net.charter.orion_pax.OasisChat.PartyPlayer;

public class PartyCommand implements CommandExecutor {

	private OasisChat plugin;
	
	public PartyCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(plugin.partychatsub);
			return true;
		}
		
		Player player = (Player) sender;
		String myparty = plugin.partyPlayer.get(player.getName()).getMyParty();
		String name = player.getName();
		String password = "";
		
		if (args[0].equalsIgnoreCase("create")) {
			if (args.length > 1) {
				if (args.length == 3) {
					password = args[2];
				}
				if (myparty!=null) {
					if (plugin.MyParties.get(myparty).isOwner(name)) {
						plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + myparty + " is disbanded!");
						plugin.MyParties.get(myparty).removeMembers();
						plugin.partyPlayer.get(name).removeParty();

					} else if (plugin.partyPlayer.get(name).getMyParty() != null) {
						plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + name + " has left chat!");
						plugin.MyParties.get(myparty).removeMember(name);
					}
				}
				plugin.MyParties.put(args[1], new Parties(plugin, name, args[1], password, null));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix)+ args[1]+ " has been created!");
				plugin.partyconfig.saveConfig();
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix)+ "Usage: /party create <partyname> <password> (password is optional)");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("quit")) {
			if (plugin.MyParties.get(myparty).isOwner(name)) {
				plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + myparty + " is disbanded!");
				plugin.MyParties.get(myparty).removeMembers();
				plugin.partyPlayer.get(name).removeParty();
				plugin.partyconfig.saveConfig();
				
			} else if (plugin.partyPlayer.get(name).getMyParty()!=null) {
				plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + name+ " has left chat!");
				plugin.MyParties.get(myparty).removeMember(name);
				plugin.partyconfig.saveConfig();
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "You are not in a party!");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("join")) {
			if (args.length > 1) {
				if (args.length == 3) {
					password = args[2];
				}
				Set<String> parties = plugin.partyconfig.getConfig().getConfigurationSection("partychats").getKeys(false);
				if (parties.contains(args[1])) {
					if (plugin.MyParties.get(args[1]).getPassword().equals(password)) {
						plugin.MyParties.get(args[1]).addMember(name);
						plugin.MyParties.get(args[1]).sendMessage(plugin.pcprefix + name + " has joined " + args[1] + "!");
						plugin.partyconfig.saveConfig();
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "Incorrect password!");
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.pcprefix) + "The party " + args[1] + " does not exist!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "Usage: /party join <partyname> <password> (password optional)");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("kick")) {
			if (args.length > 1) {
				if (plugin.MyParties.get(myparty).isOwner(name)) {
					if (plugin.getServer().getPlayer(args[1]).hasPlayedBefore()) {
						Player target = plugin.getServer().getPlayer(args[1]);
						if (target == null) {
							OfflinePlayer otarget = plugin.getServer().getOfflinePlayer(args[1]);
							if (plugin.MyParties.get(myparty).getMembers().contains(otarget.getName())) {
								plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + otarget.getName() + " has been kicked from " + myparty + "!");
								plugin.MyParties.get(myparty).removeMember(otarget.getName());
								plugin.partyconfig.saveConfig();
								return true;
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + plugin.getServer().getOfflinePlayer(args[1]).getName() + " is not a member of your party!"));
								return true;
							}
						}
						if (target != null) {
							if (plugin.MyParties.get(myparty).getMembers().contains(target.getName())) {
								plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + target.getName() + " has been kicked from " + myparty + "!");
								plugin.MyParties.get(myparty).removeMember(target.getName());
								plugin.partyconfig.saveConfig();
								return true;
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + target.getName() + " is not a member of your party!");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + args[1] + " is not online!");
							return true;
						}
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "You are not the owner of this party!"));
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "Usage: /party kick <playername>");
				return true;
			}
			return false;
		}
		if (args[0].equalsIgnoreCase("invite")) {
			if (args.length > 1) {
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + args[1] + " is not online!");
					return true;
				}
				if (plugin.MyParties.get(myparty).getMembers().contains(target.getName())) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + target.getName() + " is allready a member of a party!");
					return true;
				}
				if (target.isOnline() && (plugin.MyParties.get(myparty).isOwner(name))) {
					plugin.partyPlayer.get(target.getName()).setInvite(myparty);
					timer(target);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + target.getName() + " has been invited to " + myparty + "!"));
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + sender.getName() + " has invited you to " + myparty + "!"));
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "Usage: /party invite <playername>");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("accept")) {
			if (plugin.MyParties.get(myparty).getMembers().contains(name)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "You must leave your current party first!"));
				return true;
			} else if (plugin.MyParties.get(myparty).isOwner(name)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "You must leave your current party first!"));
				return true;
			} else if (!(plugin.partyPlayer.get(name).getInvite().equals(""))) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "You have no invitations pending!"));
				return true;
			} else {
				plugin.MyParties.get(plugin.partyPlayer.get(name).getInvite()).addMember(name);
				plugin.MyParties.get(plugin.partyPlayer.get(name).getInvite()).sendMessage(plugin.pcprefix + name + " has joined " + plugin.partyPlayer.get(name).getInvite() + "!");
				plugin.partyPlayer.get(name).setInvite("");
				plugin.partyconfig.saveConfig();
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("give")) {
			if (args.length == 2) {
				Player target = plugin.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + args[1] + " is not online!");
				} else if ((!(plugin.MyParties.get(target.getName()).isOwner(target.getName()))) || (!(plugin.MyParties.get(target.getName()).getMembers().contains(target.getName())))) {
					plugin.MyParties.get(myparty).changeOwner(target.getName());
					plugin.MyParties.get(myparty).sendMessage(plugin.pcprefix + "Ownership has been given to " + target.getName() + "!");
					plugin.partyconfig.saveConfig();
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "They are already part of a party!"));
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Usage: /party give <playername>"));
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("password")) {
			if (args.length == 2) {
				if (plugin.MyParties.get(myparty).isOwner(name)) {
					plugin.MyParties.get(myparty).changePassword(args[1]);
					plugin.partyconfig.saveConfig();
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "You are not an owner of a party!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix) + "Usage: /party password <newpassword>");
			}
		}
		if (args[0].equalsIgnoreCase("list")) {
			if (plugin.MyParties.get(myparty).getMembers().contains(name)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Owner: " + plugin.MyParties.get(myparty).getOwner()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + plugin.MyParties.get(myparty).getMembers()));
				Iterator it = plugin.partyPlayer.entrySet().iterator();
				List mlist = null;
				while (it.hasNext()){
					Map.Entry entry = (Map.Entry)it.next();
					PartyPlayer partyplayer = (PartyPlayer) entry.getValue();
					if (partyplayer.getMyParty().equals(myparty)){
						mlist.add(partyplayer.getName());
					}
					it.remove();
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Online: " + mlist));
				return true;
			} else if (plugin.MyParties.get(myparty).isOwner(name)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "PartyChat: " + myparty));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Password: " + plugin.MyParties.get(myparty).getPassword()));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Members: " + plugin.MyParties.get(myparty).getMembers()));
				Iterator it = plugin.partyPlayer.entrySet().iterator();
				List mlist = null;
				while (it.hasNext()){
					Map.Entry entry = (Map.Entry)it.next();
					PartyPlayer partyplayer = (PartyPlayer) entry.getValue();
					if (partyplayer.getMyParty().equals(myparty)){
						mlist.add(partyplayer.getName());
					}
					it.remove();
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.pcprefix + "Online: " + mlist));
				return true;
			}
		}
		sender.sendMessage(plugin.partychatsub);
		return false;
	}
	
	public void timer(final Player player){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable()
		{
			public void run()
			{
				plugin.partyPlayer.get(player.getName()).setInvite("");
			}
		}
		, 6000);
	}
}
