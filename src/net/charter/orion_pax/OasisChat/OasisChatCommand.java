package net.charter.orion_pax.OasisChat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OasisChatCommand implements CommandExecutor {

	private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

	public OasisChatCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	enum Commands {
		OASISCHAT, P, A, PARTY, CREDITS,
		STAFF, LISTPARTIES, PSPYON, PSAY, PSPYOFF, PARTYINFO
	}

	enum SubCommands {
		CREATE, QUIT, KICK, INVITE, JOIN, LIST, SAVE, LOG, CLEAR,
		RELOAD, RESET, SET, ACCEPT, GIVE, PASSWORD, DEBUG, FIX
	}

	boolean disable = false;

	String[] oasischatsub = {
			ChatColor.GOLD + "Usage: /oasischat subcommand [args]"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "SAVE - Saves config"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "LIST - List settings that can be changed in game"
			,ChatColor.GOLD + "SET - Sets in game settings for oasischat"
			,ChatColor.GOLD + "DEBUG - turns on debug."
	}; 

	String[] partychatsub = {
			ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "Usage: /party subcommand [args]")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "SubCommands:")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "CREATE partyname password (password optional)")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "JOIN partyname password (password option)")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "INVITE playername - invites player to partychat")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "ACCEPT - accepts invite, 5 min time limit")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "KICK playername")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "List - list members of your party")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "PASSWORD password")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "GIVE playername")
			,ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix + "QUIT - quits current party chat")
	}; 

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if (!disable) {
			// PARTY CHAT!!!!
			try {
				Commands mycommand = Commands.valueOf(cmd.getName().toUpperCase());

				Player player = null;
				String myparty = null;
				if (sender instanceof Player) {
					player = (Player) sender;
					myparty = plugin.party.myParty(player);
				}

				switch (mycommand) {
					case CREDITS:
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Beta tests n thanks in no particular order....");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "The Staff of Oasis-SMP");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Paxination, thats right I did it, I'm in here!");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Big_Piglet");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "AtractiveBanana");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Olive474");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "OSGATharp");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "realgriffin");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Jawell");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Keshaluver21");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Zwall99");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Tanaka91");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Grimtongue");
						sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "and...");
						sender.sendMessage(ChatColor.ITALIC.toString() + ChatColor.BLUE + "The FRIENDLY folk at bukkit dev!");
						return true;

					case PARTYINFO:
						if (args.length == 1) {
							if (plugin.party.getParties().contains(args[0])) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "PartyChat: " + args[0]);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Owner: " + plugin.party.getOwner(args[0]));
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "Password: " + plugin.party.getPassword(args[0]));
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix) + "Members: " + plugin.party.getMembers(args[0]).toString());
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Online: "+ plugin.party.getOnlineMembers(args[0],player).toString());
								return true;
							}
						} else if (plugin.partyspy.containsKey(player.getName())) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "PartyChat: "+ plugin.partyspy.get(player.getName()));
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Password: "+ plugin.party.getPassword(plugin.partyspy.get(player.getName())));
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Members: "+ plugin.party.getMembers(plugin.partyspy.get(player.getName())).toString());
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Online: "+ plugin.party.getOnlineMembers(plugin.partyspy.get(player.getName()),player).toString());
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Usage: /pinfo <partyname> (partyname not needed if in party spying already!");
							return true;
						}
						return true;

					case LISTPARTIES:
						Set<String> parties = plugin.getConfig().getConfigurationSection("partychats").getKeys(false);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + parties.toString());
						return true;

					case PSPYON:
						if (args.length == 1) {
							if (plugin.getConfig().contains("partychats." + args[0])) {
								if (!(plugin.partyspy.containsKey(sender.getName()))) {
									plugin.partyspy.put(sender.getName(), args[0]);
									plugin.perms.get(sender.getName()).setPermission("oasischat.party." + args[0],true);
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You are now watching " + args[0]);
									return true;
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You must quit your current partyspy before spying on another!");
									return true;
								}
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Usage: /pjoin <partyname> Also do /plist to get a list of online party chats!");
							return true;
						}

					case PSPYOFF:
						plugin.perms.get(sender.getName()).unsetPermission("oasischat.party."+ plugin.partyspy.get(sender.getName()));
						plugin.partyspy.remove(sender.getName());
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "You have stopped spying!");
						return true;

					case PSAY:
						if (plugin.partyspy.containsKey(sender.getName())) {
							String prefix = ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "<"+ ChatColor.translateAlternateColorCodes('&',OasisChat.pncprefix)+ plugin.partyspy.get(sender.getName())+ ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "> - "+ ChatColor.translateAlternateColorCodes('&',OasisChat.pncprefix)+ sender.getName()+ ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix) + ": ";
							StringBuffer buffer = new StringBuffer();
							buffer.append(args[0]);

							for (int i = 1; i < args.length; i++) {
								buffer.append(" ");
								buffer.append(args[i]);
							}

							String message = buffer.toString();
							plugin.console.sendMessage(prefix + message);
							plugin.getServer().broadcast(prefix + message,"oasischat.party."+ plugin.partyspy.get(sender.getName()));
						}
						return true;

					case P:
						if (args.length == 0) {
							if (sender instanceof Player) {
								if (myparty == null) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "You're not part of a party chat!");
									return true;
								}
								if (plugin.partychattoggle.get(sender.getName()) != "") {

									plugin.partychattoggle.put(sender.getName(),"");
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix)+ "Playerchat "+ ChatColor.RED+ "DISABLED");
								} else {
									plugin.partychattoggle.put(sender.getName(),myparty);
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.pcprefix)+ "Playerchat "+ ChatColor.GREEN+ "ENABLED");
								}
							}
						} else {
							if (plugin.partyhash.containsKey(sender.getName())) {
								String prefix = ChatColor
										.translateAlternateColorCodes('&',
												OasisChat.pcprefix)
												+ "<"
												+ ChatColor.translateAlternateColorCodes(
														'&', OasisChat.pncprefix)
														+ myparty
														+ ChatColor.translateAlternateColorCodes(
																'&', OasisChat.pcprefix)
																+ "> - "
																+ ChatColor.translateAlternateColorCodes(
																		'&', OasisChat.pncprefix)
																		+ sender.getName()
																		+ ChatColor.translateAlternateColorCodes(
																				'&', OasisChat.pcprefix) + ": ";
								StringBuffer buffer = new StringBuffer();
								buffer.append(args[0]);

								for (int i = 1; i < args.length; i++) {
									buffer.append(" ");
									buffer.append(args[i]);
								}

								String message = buffer.toString();
								plugin.console.sendMessage(prefix + message);
								plugin.getServer().broadcast(prefix + ChatColor.translateAlternateColorCodes('&', message),
										plugin.partyhash.get(sender.getName()));
							}
						}
						return false;

					case PARTY:
						if (args.length == 0) {
							sender.sendMessage(partychatsub);
							return true;
						}
						SubCommands subcommand = SubCommands.valueOf(args[0]
								.toUpperCase());
						String password = "";

						try {
							switch (subcommand) {

								case CREATE:
									if (args.length > 1) {
										if (args.length == 3) {
											password = args[2];
										}
										if (plugin.party.isOwner(player)) {
											plugin.getServer()
											.broadcast(
													ChatColor.translateAlternateColorCodes(
															'&',
															OasisChat.pcprefix)
															+ myparty
															+ " disbanded!",
															plugin.partyhash.get(player
																	.getName()));
											plugin.party.delParty(myparty);
										} else if (plugin.party.isMember(player)) {
											plugin.getServer()
											.broadcast(
													ChatColor
													.translateAlternateColorCodes(
															'&',
															OasisChat.pcprefix)
															+ player.getName()
															+ " has left chat!",
															plugin.partyhash.get(player
																	.getName()));
											plugin.party.delMember(player.getName(),
													myparty);
										}
										plugin.party.createParty(player, args[1],
												password);
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ args[1] + " has been created!");
										return true;
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party create <partyname> <password> (password is optional)");
										return true;
									}

								case QUIT:
									if (plugin.party.isOwner(player)) {
										plugin.getServer().broadcast(
												ChatColor.translateAlternateColorCodes(
														'&', OasisChat.pcprefix)
														+ myparty + " disbanded!",
														plugin.partyhash.get(player.getName()));
										plugin.party.partyspydel(myparty);
										plugin.party.delParty(myparty);
									} else if (plugin.party.isMember(player)) {
										plugin.getServer().broadcast(
												ChatColor.translateAlternateColorCodes(
														'&', OasisChat.pcprefix)
														+ player.getName()
														+ " has left chat!",
														plugin.partyhash.get(player.getName()));
										plugin.party.delMember(player.getName(),
												myparty);
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "You are not in a party!");
										return true;
									}
									return true;

								case JOIN:
									if (args.length > 1) {
										if (args.length == 3) {
											password = args[2];
										}
										if (plugin.party.getParties().toString()
												.contains(args[1])) {
											if (plugin.party.getPassword(args[1])
													.equals(password)) {
												plugin.party.addMember(player, args[1]);
												plugin.partyhash.put(player.getName(),
														"oasischat.party." + args[1]);
												player.addAttachment(plugin,
														"oasischat.party." + args[1],
														true);
												plugin.getServer()
												.broadcast(
														ChatColor
														.translateAlternateColorCodes(
																'&',
																OasisChat.pcprefix)
																+ player.getName()
																+ " has joined "
																+ args[1] + "!",
																"oasischat.party."
																		+ args[1]);
											} else {
												sender.sendMessage(ChatColor
														.translateAlternateColorCodes(
																'&', OasisChat.pcprefix)
																+ "Incorrect password!");
											}
										} else {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ "The party "
															+ args[1]
																	+ " does not exist!");
											return true;
										}
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party join <partyname> <password> (password optional)");
										return true;
									}
									return false;

								case KICK:
									if (args.length > 1) {
										Player target = plugin.getServer().getPlayer(
												args[1]);
										if (target == null) {
											if (plugin.getServer().getPlayer(args[1])
													.hasPlayedBefore()) {
												OfflinePlayer otarget = plugin
														.getServer().getOfflinePlayer(
																args[1]);
												if (plugin.party
														.isMember((Player) otarget)) {
													plugin.getServer()
													.broadcast(
															ChatColor
															.translateAlternateColorCodes(
																	'&',
																	OasisChat.pcprefix)
																	+ otarget.getName()
																	+ " has been kicked from "
																	+ myparty
																	+ "!",
																	plugin.partyhash.get(player
																			.getName()));
													plugin.party.delMember(
															otarget.getName(), myparty);
												} else {
													sender.sendMessage(ChatColor
															.translateAlternateColorCodes(
																	'&',
																	OasisChat.pcprefix)
																	+ plugin.getServer()
																	.getOfflinePlayer(
																			args[1])
																			.getName()
																			+ " is not a member of your party!");
													return true;
												}
											}
										}
										if (target != null) {
											if (plugin.party.isOwner(player)) {
												if (plugin.party.isMember(target)) {
													plugin.getServer()
													.broadcast(
															ChatColor
															.translateAlternateColorCodes(
																	'&',
																	OasisChat.pcprefix)
																	+ target.getName()
																	+ " has been kicked from "
																	+ myparty
																	+ "!",
																	plugin.partyhash.get(player
																			.getName()));
													plugin.party.delMember(
															target.getName(), myparty);
												} else {
													sender.sendMessage(ChatColor
															.translateAlternateColorCodes(
																	'&',
																	OasisChat.pcprefix)
																	+ target.getName()
																	+ " is not a member of your party!");
													return true;
												}
											} else {
												sender.sendMessage(ChatColor
														.translateAlternateColorCodes(
																'&', OasisChat.pcprefix)
																+ "You're not the party owner!");
											}
										} else {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ args[1] + " is not online!");
											return true;
										}
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party kick <playername>");
										return true;
									}
									return false;

								case INVITE:
									if (args.length > 1) {
										Player target = plugin.getServer().getPlayer(
												args[1]);
										if (target == null) {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ args[1] + " is not online!");
											return true;
										}
										if (plugin.party.isMember(target)) {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ target.getName()
															+ " is allready a member of a party!");
											return true;
										}
										if (target.isOnline() && (plugin.party.isOwner(player))) {
											plugin.invite
											.put(target.getName(), myparty);
											timer(target);
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ target.getName()
															+ " has been invited to "
															+ myparty
															+ "!");
											target.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ sender.getName()
															+ " has invited you to "
															+ myparty
															+ "!");
											return true;
										}
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party invite <playername>");
										return true;
									}

								case ACCEPT:
									if (plugin.party.isMember(player)) {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "You must leave your current party first!");
										return true;
									} else if (plugin.party.isOwner(player)) {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "You must leave your current party first!");
										return true;
									} else if (!(plugin.invite.containsKey(player
											.getName()))) {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "You have no invitations pending!");
										return true;
									} else {
										plugin.party.addMember(player,
												plugin.invite.get(player.getName()));
										plugin.getServer().broadcast(
												ChatColor.translateAlternateColorCodes(
														'&', OasisChat.pcprefix)
														+ player.getName()
														+ " has joined "
														+ plugin.invite.get(player
																.getName()) + "!",
																plugin.partyhash.get(player.getName()));
										plugin.invite.remove(player.getName());
										return true;
									}

								case GIVE:
									if (args.length == 2) {
										Player target = plugin.getServer().getPlayer(
												args[1]);
										if (target == null) {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ args[1] + " is not online!");
										} else if ((!(plugin.party.isOwner(target)))
												|| (!(plugin.party.isMember(target)))) {
											plugin.party.setOwner(target, player,
													myparty);
											plugin.getServer()
											.broadcast(
													ChatColor.translateAlternateColorCodes(
															'&',
															OasisChat.pcprefix)
															+ "Ownership has been given to "
															+ target.getName()
															+ "!",
															plugin.partyhash.get(target
																	.getName()));
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ "Ownership was been transfered!");
											return true;
										} else {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ "They are already part of a party!");
											return true;
										}
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party give <playername>");
										return true;
									}

								case PASSWORD:
									if (args.length == 2) {
										if (plugin.party.isOwner(player)) {
											plugin.party.setPassword(player, args[1],
													myparty);
											return true;
										} else {
											sender.sendMessage(ChatColor
													.translateAlternateColorCodes('&',
															OasisChat.pcprefix)
															+ "You are not an owner of a party!");
											return true;
										}
									} else {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Usage: /party password <newpassword>");
									}

								case LIST:
									if (plugin.party.isMember(player)) {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Members: "
														+ plugin.party.getMembers(myparty));
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Online: "
														+ plugin.party.getOnlineMembers(
																myparty, player).toString());
										return true;
									} else if (plugin.party.isOwner(player)) {
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "PartyChat: " + myparty);
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Password: "
														+ plugin.party.getPassword(myparty));
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Members: "
														+ plugin.party.getMembers(myparty)
														.toString());
										sender.sendMessage(ChatColor
												.translateAlternateColorCodes('&',
														OasisChat.pcprefix)
														+ "Online: "
														+ plugin.party.getOnlineMembers(
																myparty, player).toString());
										return true;
									}
									return true;

								default:
									sender.sendMessage(partychatsub);
									return true;
							}
						} catch (IllegalArgumentException e) {
							sender.sendMessage(partychatsub);
							throw e;
						}
					case OASISCHAT:
						if (args.length == 0) {
							sender.sendMessage(oasischatsub);
							return true;
						}
						SubCommands subcommand2 = SubCommands.valueOf(args[0]
								.toUpperCase());
						try {
							switch (subcommand2) {
								case RESET:
									disable = !disable;
									plugin.getServer().broadcastMessage(ChatColor.RED + "OasisChat is reloading!");
									plugin.partyhash.clear();
									plugin.partyspy.clear();
									plugin.perms.clear();
									plugin.partychattoggle.clear();
									plugin.adminchattoggle.clear();
									plugin.invite.clear();
									Player [] onlineplayers = plugin.getServer().getOnlinePlayers();
									for (Player onlineplayer : onlineplayers){
										plugin.perms.put(player.getName(), player.addAttachment(plugin));
										String myparty1 = plugin.party.myParty(player);
										if (myparty1 !=null){
											plugin.partyhash.put(player.getName(), "oasischat.party." + myparty1);
											plugin.perms.get(player.getName()).setPermission("oasischat.party." + myparty1, true);
										}
									}
									disable = !disable;
									plugin.getServer().broadcastMessage(ChatColor.RED + "OasisChat is done reloading!");
									return true;

								case FIX:
									boolean needreset = false;
									Set<String> parties1 = plugin.party.getParties();
									if (parties1 == null) {
										return true;
									}
									for (String thisparty : parties1) {
										if (!plugin.getConfig().contains(
												"partychats." + thisparty + ".owner")) {
											plugin.getConfig().set(
													"partychats." + thisparty, null);
											needreset = true;
										}
										if (!plugin.getConfig()
												.contains(
														"partychats." + thisparty
														+ ".password")) {
											plugin.getConfig().set(
													"partychats." + thisparty, null);
											needreset = true;
										}
										if (!plugin.getConfig().contains(
												"partychats." + thisparty + ".members")) {
											plugin.getConfig().set(
													"partychats." + thisparty, null);
											needreset = true;
										}
									}
									if (needreset) {
										plugin.party.resetchats();
									}
									plugin.saveConfig();
									return true;

								case CLEAR:
									plugin.file = new File(
											"plugins/OasisChat/paxserrorlog.txt");
									if (plugin.file.exists()) {
										plugin.file.delete();
										sender.sendMessage(ChatColor.RED
												+ "Error log cleared!");
										return true;
									} else {
										sender.sendMessage(ChatColor.RED
												+ "No log file to delete!");
										return true;
									}

								case LOG:
									if (plugin.file.exists()) {
										BufferedReader in = new BufferedReader(
												new FileReader(plugin.file));
										String line;
										while ((line = in.readLine()) != null) {
											sender.sendMessage(line);
										}
										in.close();
										return true;
									} else {
										sender.sendMessage(ChatColor.RED
												+ "No log file today!");
										return true;
									}

								case DEBUG:
									if (args.length == 1) {
										if (plugin.perms.get(player.getName())
												.getPermissions().toString()
												.contains("oasis.debug")) {
											plugin.perms.get(player.getName())
											.unsetPermission("oasis.debug");
											sender.sendMessage(ChatColor.RED
													+ "Debug off!");
											return true;
										} else {
											plugin.perms.get(player.getName())
											.setPermission("oasis.debug", true);
											sender.sendMessage(ChatColor.RED
													+ "Debug on!");
											return true;
										}
									} else if (args.length == 2) {
										if (args[1].equalsIgnoreCase("test")) {
											Player test = plugin.getServer().getPlayer(
													"Myfatass");
											test.sendMessage("biteme");
										}
									}
								case SAVE:
									plugin.partytask.cancel();
									plugin.saveConfig();
									plugin.partytask.runTaskTimer(plugin, 10, 12000);
									sender.sendMessage(ChatColor.GOLD + "Config Saved!");
									return true;

								case RELOAD:
									plugin.partytask.cancel();
									plugin.reloadConfig();
									plugin.partytask.runTaskTimer(plugin, 10, 12000);
									plugin.setup();
									sender.sendMessage(ChatColor.GOLD
											+ "Config reloaded!");
									return true;

								case LIST:
									sender.sendMessage(ChatColor.GOLD+ "These are the available options you can change in game!");
									sender.sendMessage(plugin.getConfig().getConfigurationSection("ingameconfigurable").getKeys(false).toString());
									return true;

								case SET:
									if (args.length < 3) {
										sender.sendMessage(ChatColor.GOLD
												+ "Usage: /oasischat set key value");
										return true;
									}
									if (keyexist(args[1])) {
										if (args[1].contains("chatcolor")) {
											plugin.getConfig().set(
													"ingameconfigurable." + args[1],
													args[2]);
										}
										plugin.setup();
										sender.sendMessage(ChatColor.GOLD
												+ "Config successfully changed!  Dont forget to /oasischat save!");
										return true;
									} else {
										sender.sendMessage(ChatColor.GOLD
												+ args[1]
														+ " is not a defined key in the config. Do /oasischat list for a list of keys!");
										return true;
									}

								default:
									sender.sendMessage(oasischatsub);
									return true;
							}
						} catch (IllegalArgumentException e) {
							sender.sendMessage(oasischatsub);
							throw e;
						}

					case STAFF:
						sender.sendMessage(ChatColor.translateAlternateColorCodes(
								'&', OasisChat.acprefix) + "[STAFF ONLINE]");
						Player[] onlinePlayers = Bukkit.getServer()
								.getOnlinePlayers();
						for (Player oplayer : onlinePlayers) {
							if ((oplayer != null)
									&& (oplayer.hasPermission("OasisChat.staff.a"))) {
								sender.sendMessage(ChatColor
										.translateAlternateColorCodes('&',
												OasisChat.acprefix)
												+ oplayer.getName());
							}
						}
						return true;

					case A:
						if (args.length == 0) {
							if (sender instanceof Player) {
								if (sender.hasPermission("OasisChat.staff.a")) {
									if (plugin.adminchattoggle.get(sender.getName()).equalsIgnoreCase("oasischat.staff.a")) {
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', OasisChat.acprefix)+"Adminchat " + ChatColor.RED + "DISABLED");
										plugin.adminchattoggle.put(sender.getName(), "");
									} else {
										plugin.adminchattoggle.put(sender.getName(),"oasischat.staff.a");
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&',OasisChat.acprefix)+ "Adminchat "+ ChatColor.GREEN+ "ENABLED");
									}
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
						return true;

					default:
						return false;

				}
			} catch (Throwable e) {
				plugin.printStackTrace(e,
						cmd.getName() + " " + Arrays.toString(args));
			}
			return true;
		}
		return true;
	}


	public boolean keyexist(String key){
		if (plugin.getConfig().getConfigurationSection("ingameconfigurable").getKeys(false).toString().contains(key)){
			return true;
		}

		return false;
	}

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
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
