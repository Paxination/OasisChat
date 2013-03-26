package net.charter.orion_pax.OasisChat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class OasisChatCommand implements CommandExecutor {

    private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public OasisChatCommand(OasisChat plugin){
	this.plugin = plugin;
    }

    enum Commands {
	OASISCHAT, P, A, PARTY,
	STAFF, LISTPARTIES, PSPYON, PSAY, PSPYOFF, PARTYINFO
    }

    enum SubCommands {
	CREATE, QUIT, KICK, INVITE, JOIN, LIST, SAVE,
	RELOAD, RESET, SET, ACCEPT, GIVE, PASSWORD, DEBUG
    }

    String[] oasischatsub = {
	    ChatColor.GOLD + "Usage: /oasischat subcommand [args]"
	    ,ChatColor.GOLD + "SubCommands:"
	    ,ChatColor.GOLD + "SAVE - Saves config"
	    ,ChatColor.GOLD + "RELOAD - Reloads config"
	    ,ChatColor.GOLD + "RESET - Resets config to defaults and reloads"
	    ,ChatColor.GOLD + "LIST - List settings that can be changed in game"
	    ,ChatColor.GOLD + "SET - Sets in game settings for oasischat"
	    ,ChatColor.GOLD + "DEBUG - turns on debug."
    }; 

    String[] partychatsub = {
	    ChatColor.GREEN + "Usage: /party subcommand [args]"
	    ,ChatColor.GREEN + "SubCommands:"
	    ,ChatColor.GREEN + "CREATE partyname password (password optional)"
	    ,ChatColor.GREEN + "JOIN partyname password (password option)"
	    ,ChatColor.GREEN + "INVITE playername - invites player to partychat"
	    ,ChatColor.GREEN + "ACCEPT - accepts invite, 5 min time limit"
	    ,ChatColor.GREEN + "KICK playername"
	    ,ChatColor.GREEN + "List - list members of your party"
	    ,ChatColor.GREEN + "PASSWORD password"
	    ,ChatColor.GREEN + "GIVE playername"
	    ,ChatColor.GREEN + "QUIT - quits current party chat"
    }; 

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
	// PARTY CHAT!!!!
	try {
	    Commands mycommand = Commands.valueOf(cmd.getName().toUpperCase());
	    
	    Player player = null;
	    String myparty = null;
	    if (sender instanceof Player){
	        player = (Player) sender;
	        myparty = plugin.party.myParty(player);
	    }

	    switch (mycommand) {
	    case PARTYINFO:
	        if (args.length==1){
	    	if (plugin.party.getParties().contains(args[0])) {
	    	    sender.sendMessage(ChatColor.GREEN + "PartyChat: " + args[0]);
	    	    sender.sendMessage(ChatColor.GREEN + "Owner: " + plugin.party.getOwner(args[0]));
	    	    sender.sendMessage(ChatColor.GREEN + "Password: " + plugin.party.getPassword(args[0]));
	    	    sender.sendMessage(ChatColor.GREEN + "Members: " + plugin.party.getMembers(args[0]).toString());
	    	    sender.sendMessage(ChatColor.GREEN + "Online: " + plugin.party.getOnlineMembers(args[0], player).toString());
	    	    return true;
	    	}
	        } else if (plugin.partyspy.containsKey(player.getName())){
	    	sender.sendMessage(ChatColor.GREEN + "PartyChat: " + plugin.partyspy.get(player.getName()));
	    	sender.sendMessage(ChatColor.GREEN + "Password: " + plugin.party.getPassword(plugin.partyspy.get(player.getName())));
	    	sender.sendMessage(ChatColor.GREEN + "Members: " + plugin.party.getMembers(plugin.partyspy.get(player.getName())).toString());
	    	sender.sendMessage(ChatColor.GREEN + "Online: " + plugin.party.getOnlineMembers(plugin.partyspy.get(player.getName()), player).toString());
	    	return true;
	        } else {
	    	sender.sendMessage(ChatColor.GREEN + "Usage: /pinfo <partyname> (partyname not needed if in party spying already!");
	    	return true;
	        }
	        return true;
	        
	    case LISTPARTIES:
	        Set<String> parties = plugin.getConfig().getConfigurationSection("partychats").getKeys(false);
	        sender.sendMessage(ChatColor.GREEN + parties.toString());
	        return true;

	    case PSPYON:
	        if (args.length==1){
	    	if (plugin.getConfig().contains("partychats." + args[0])){
	    	    if (!(plugin.partyspy.containsKey(sender.getName()))) {
	    		plugin.partyspy.put(sender.getName(),args[0]);
	    		plugin.perms.get(sender.getName()).setPermission("oasischat.party." + args[0], true);
	    		sender.sendMessage(ChatColor.GREEN + "You are now watching " + args[0]);
	    		return true;
	    	    } else {
	    		sender.sendMessage(ChatColor.GREEN + "You must quit your current partyspy before spying on another!");
	    		return true;
	    	    }
	    	}
	        } else {
	    	sender.sendMessage(ChatColor.GREEN + "Usage: /pjoin <partyname> Also do /plist to get a list of online party chats!");
	    	return true;
	        }

	    case PSPYOFF:
	        plugin.perms.get(sender.getName()).unsetPermission("oasischat.party." + plugin.partyspy.get(sender.getName()));
	        plugin.partyspy.remove(sender.getName());
	        sender.sendMessage(ChatColor.GREEN + "You have stopped spying!");
	        return true;

	    case PSAY:
	        if (plugin.partyspy.containsKey(sender.getName())){
	    	String prefix = plugin.pcprefix + "{" + plugin.pncprefix + sender.getName() + plugin.pcprefix + "} ";
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(args[0]);

	    	for (int i = 1; i < args.length; i++) {
	    	    buffer.append(" ");
	    	    buffer.append(args[i]);
	    	}

	    	String message = buffer.toString();
	    	plugin.getLogger().info(plugin.greenprefix+"<" + plugin.partyspy.get(sender.getName()) + "> " + sender.getName() + " - " + message+ plugin.greensufix);
	    	plugin.getServer().broadcast(prefix + message, "oasischat.party." + plugin.partyspy.get(sender.getName()));
	        }
	        return true;

	    case P:
	        if (args.length == 0){
	    	if (sender instanceof Player){
	    	    if (myparty==null){
	    		sender.sendMessage(ChatColor.GREEN + "Your not party of a party chat!");
	    		return true;
	    	    }
	    	    if(sender.hasPermission("oasischat.players.p")){
	    		if (plugin.chattoggle.containsKey(sender.getName())){
	    		    if (plugin.chattoggle.containsValue("oasischat.staff.a")) {
	    			plugin.chattoggle.put(sender.getName(),"oasischat.players.p");
	    			sender.sendMessage(plugin.pcprefix + "Playerchat " + ChatColor.GREEN + "ENABLED");
	    		    } else {
	    			plugin.chattoggle.remove(sender.getName());
	    			sender.sendMessage(plugin.pcprefix + "Playerchat " + ChatColor.RED + "DISABLED");
	    		    }
	    		} else {
	    		    plugin.chattoggle.put(sender.getName(),"oasischat.players.p");
	    		    sender.sendMessage(plugin.pcprefix + "Playerchat " + ChatColor.GREEN + "ENABLED");
	    		}
	    	    }
	    	}
	        } else {
	    	if (plugin.partyhash.containsKey(sender.getName())){
	    	    String prefix = plugin.pcprefix + "{" + plugin.pncprefix + sender.getName() + plugin.pcprefix + "} ";
	    	    StringBuffer buffer = new StringBuffer();
	    	    buffer.append(args[0]);

	    	    for (int i = 1; i < args.length; i++) {
	    		buffer.append(" ");
	    		buffer.append(args[i]);
	    	    }

	    	    String message = buffer.toString();
	    	    plugin.getLogger().info(plugin.greenprefix+"<" + myparty + "> " + sender.getName() + " - " + message+ plugin.greensufix);
	    	    plugin.getServer().broadcast(prefix + message, plugin.partyhash.get(sender.getName()));
	    	}
	        }
	        return false;

	    case PARTY:
	        if (args.length==0){
	    	sender.sendMessage(partychatsub);
	    	return true;
	        }
	        SubCommands subcommand = SubCommands.valueOf(args[0].toUpperCase());
	        String password = "";

	        switch (subcommand) {

	        case CREATE:
	    	if (args.length > 1) {
	    	    if (args.length == 3){password = args[2];}
	    	    if (plugin.party.isOwner(player)) {
	    		plugin.getServer().broadcast(ChatColor.GREEN + myparty + " disbanded!", plugin.partyhash.get(player.getName()));
	    		plugin.party.delParty(myparty);
	    	    } else if (plugin.party.isMember(player)) {
	    		plugin.getServer().broadcast(ChatColor.GREEN + player.getName() + " has left chat!", plugin.partyhash.get(player.getName()));
	    		plugin.party.delMember(player,myparty);
	    	    }
	    	    plugin.party.createParty(player, args[1], password);
	    	    sender.sendMessage(ChatColor.GREEN + args[1] + " has been created!");
	    	    return true;
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party create <partyname> <password> (password is optional)");
	    	    return true;
	    	}

	        case QUIT:
	    	if (plugin.party.isOwner(player)) {
	    	    plugin.getServer().broadcast(ChatColor.GREEN + myparty + " disbanded!", plugin.partyhash.get(player.getName()));
	    	    plugin.party.partyspydel(myparty);
	    	    plugin.party.delParty(myparty);
	    	} else if (plugin.party.isMember(player)) {
	    	    plugin.getServer().broadcast(ChatColor.GREEN + player.getName() + " has left chat!", plugin.partyhash.get(player.getName()));
	    	    plugin.party.delMember(player,myparty);
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "You are not in a party!");
	    	    return true;
	    	}
	    	return true;

	        case JOIN:
	    	if (args.length > 1){
	    	    if (args.length==3){password=args[2];}
	    	    if (plugin.party.getParties().toString().contains(args[1])){
	    		if (plugin.party.getPassword(args[1]).equals(password)){
	    		    plugin.party.addMember(player, args[1]);
	    		    plugin.partyhash.put(player.getName(), "oasischat.party." + args[1]);
	    		    player.addAttachment(plugin, "oasischat.party." + args[1], true);
	    		    plugin.getServer().broadcast(ChatColor.GREEN + player.getName() + " has joined " + args[1] + "!", "oasischat.party." + args[1]);
	    		} else {
	    		    sender.sendMessage(ChatColor.GREEN + "Incorrect password!");
	    		}
	    	    } else {
	    		sender.sendMessage(ChatColor.GREEN + "The party " + args[1] + " does not exist!");
	    		return true;
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party join <partyname> <password> (password optional)");
	    	    return true;
	    	}
	    	return false;

	        case KICK:
	    	if (args.length > 1){
	    	    Player target = plugin.getServer().getPlayer(args[1]);
	    	    if (target == null){
	    		if (plugin.getServer().getPlayer(args[1]).hasPlayedBefore()){
	    		    OfflinePlayer otarget = plugin.getServer().getOfflinePlayer(args[1]);
	    		    if (plugin.party.isMember((Player) otarget)) {
	    			plugin.getServer().broadcast(ChatColor.GREEN + otarget.getName() + " has been kicked from " + myparty + "!", plugin.partyhash.get(player.getName()));
	    			plugin.party.delMember((Player) otarget, myparty);
	    		    } else {
	    			sender.sendMessage(ChatColor.GREEN + target.getName() + " is not a member of your party!");
	    			return true;
	    		    }
	    		}
	    	    }
	    	    if (target != null) {
	    		if (plugin.party.isOwner(player)) {
	    		    if (plugin.party.isMember(target)) {
	    			plugin.getServer().broadcast(ChatColor.GREEN + target.getName() + " has been kicked from " + myparty + "!", plugin.partyhash.get(player.getName()));
	    			plugin.party.delMember(target, myparty);
	    		    } else {
	    			sender.sendMessage(ChatColor.GREEN + target.getName() + " is not a member of your party!");
	    			return true;
	    		    }
	    		} else {
	    		    sender.sendMessage(ChatColor.GREEN
	    			    + "Your not the party owner!");
	    		}
	    	    } else {
	    		sender.sendMessage(ChatColor.GREEN + args[1] + " is not online!");
	    		return true;
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party kick <playername>");
	    	    return true;
	    	}
	    	return false;

	        case INVITE:
	    	if (args.length > 1){
	    	    Player target = plugin.getServer().getPlayer(args[1]);
	    	    if (target==null){
	    		sender.sendMessage(ChatColor.GREEN + args[1] + " is not online!");
	    		return true;
	    	    }
	    	    if (plugin.party.isMember(target)){
	    		sender.sendMessage(ChatColor.GREEN + target.getName() + " is allready a member of a party!");
	    		return true;
	    	    }
	    	    if (target.isOnline()) {
	    		plugin.invite.put(target.getName(), myparty);
	    		timer(target);
	    		sender.sendMessage(ChatColor.GREEN + target.getName() + " has been invited to " + myparty + "!");
	    		target.sendMessage(ChatColor.GREEN + sender.getName() + " has invited you to " + myparty + "!");
	    		return true;
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party invite <playername>");
	    	    return true;
	    	}

	        case ACCEPT:
	    	if (plugin.party.isMember(player)){
	    	    sender.sendMessage(ChatColor.GREEN + "You must leave your current party first!");
	    	    return true;
	    	} else if (plugin.party.isOwner(player)){
	    	    sender.sendMessage(ChatColor.GREEN + "You must leave your current party first!");
	    	    return true;
	    	} else if (!(plugin.invite.containsKey(player.getName()))){
	    	    sender.sendMessage(ChatColor.GREEN + "You have no invitations pending!");
	    	    return true;
	    	} else {
	    	    plugin.party.addMember(player, plugin.invite.get(player.getName()));
	    	    plugin.getServer().broadcast(ChatColor.GREEN + player.getName() + " has joined " + plugin.invite.get(player.getName()) + "!" , plugin.partyhash.get(player.getName()));
	    	    plugin.invite.remove(player.getName());
	    	    return true;
	    	}

	        case GIVE:
	    	if (args.length==2){
	    	    Player target = plugin.getServer().getPlayer(args[1]);
	    	    if (target == null){
	    		sender.sendMessage(ChatColor.GREEN + args[1] + " is not online!");
	    	    } else if ((!(plugin.party.isOwner(target))) || (!(plugin.party.isMember(target)))){
	    		plugin.party.setOwner(target, player, myparty);
	    		plugin.getServer().broadcast(ChatColor.GREEN + "Ownership has been given to " + target.getName() + "!", plugin.partyhash.get(target.getName()));
	    		sender.sendMessage(ChatColor.GREEN + "Ownership was been transfered!");
	    		return true;
	    	    } else {
	    		sender.sendMessage(ChatColor.GREEN + "They are already part of a party!");
	    		return true;
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party give <playername>");
	    	    return true;
	    	}

	        case PASSWORD:
	    	if (args.length==2){
	    	    if (plugin.party.isOwner(player)){
	    		plugin.party.setPassword(player, args[1], myparty);
	    		return true;
	    	    } else {
	    		sender.sendMessage(ChatColor.GREEN + "You are not an owner of a party!");
	    		return true;
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GREEN + "Usage: /party password <newpassword>");
	    	}

	        case LIST:
	    	if (plugin.party.isMember(player)){
	    	    sender.sendMessage(ChatColor.GREEN + "Members: " + plugin.party.getMembers(myparty));
	    	    sender.sendMessage(ChatColor.GREEN + "Online: " + plugin.party.getOnlineMembers(myparty, player).toString());
	    	    return true;
	    	} else if (plugin.party.isOwner(player)){
	    	    sender.sendMessage(ChatColor.GREEN + "PartyChat: " + myparty);
	    	    sender.sendMessage(ChatColor.GREEN + "Password: " + plugin.party.getPassword(myparty));
	    	    sender.sendMessage(ChatColor.GREEN + "Members: " + plugin.party.getMembers(myparty).toString());
	    	    sender.sendMessage(ChatColor.GREEN + "Online: " + plugin.party.getOnlineMembers(myparty, player).toString());
	    	    return true;
	    	}
	    	return true;
	    	
	        default:
	    	sender.sendMessage(partychatsub);
	    	return true;
	        }
	    case OASISCHAT:
	        if (args.length == 0){
	    	sender.sendMessage(oasischatsub);
	    	return true;
	        }
	        SubCommands subcommand2 = SubCommands.valueOf(args[0].toUpperCase());
	        switch (subcommand2){
	        
	        case DEBUG:
		    if (args.length==1) {
			if (plugin.perms.get(player.getName()).getPermissions().toString().contains("oasis.staff.dev")) {
			    plugin.perms.get(player.getName()).unsetPermission("oasis.staff.dev");
			    sender.sendMessage(ChatColor.RED + "Debug off!");
			    return true;
			} else {
			    plugin.perms.get(player.getName()).setPermission(
				    "oasis.staff.dev", true);
			    sender.sendMessage(ChatColor.RED + "Debug on!");
			    return true;
			}
		    } else if (args.length==2){
			if (args[1].equalsIgnoreCase("test")){
			    Player test = plugin.getServer().getPlayer("Myfatass");
			    test.sendMessage("biteme");
			}
		    }
		case SAVE:
	    	plugin.saveConfig();
	    	sender.sendMessage(ChatColor.GOLD + "Config Saved!");
	    	return true;

	        case RELOAD:
	    	plugin.reloadConfig();
	    	plugin.setup();
	    	sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
	    	return true;

	        case LIST:
	    	sender.sendMessage(ChatColor.GOLD + "These are the available options you can change in game!");
	    	sender.sendMessage(plugin.getConfig().getConfigurationSection("ingameconfigurable").getKeys(false).toString());
	    	return true;

	        case SET:
	    	if (args.length < 3){
	    	    sender.sendMessage(ChatColor.GOLD + "Usage: /oasischat set key integer");
	    	    return true;
	    	}
	    	try { 
	    	    Integer.parseInt(args[2]); 
	    	} catch(NumberFormatException e) { 
	    	    sender.sendMessage(ChatColor.GOLD + args[2] + " is not an integer!");
	    	    return true;
	    	}
	    	if (keyexist(args[1])) {
	    	    if (args[1].contains("chatcolor")) {
	    		plugin.getConfig().getConfigurationSection("ingameconfigurable").set(args[1],Integer.parseInt(args[2]));
	    	    }
	    	    plugin.setup();
	    	    sender.sendMessage(ChatColor.GOLD + "Config successfully changed!  Dont forget to /oasischat save!");
	    	    return true;
	    	} else {
	    	    sender.sendMessage(ChatColor.GOLD + args[1] + " is not a defined key in the config. Do /oasischat list for a list of keys!");
	    	    return true;
	    	}

	        case RESET:
	    	File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("OasisChat").getDataFolder(), "config.yml");
	    	if (configFile.exists()) {
	    	    configFile.delete();
	    	    plugin.saveDefaultConfig();
	    	    plugin.reloadConfig();
	    	    plugin.setup();
	    	    sender.sendMessage(ChatColor.GOLD + "Config file restored to default settings!");
	    	    return true;
	    	}
	    	return true;
	    	
	        default:
	    	sender.sendMessage(oasischatsub);
	    	return true;
	        }

	    case STAFF:
	        if (!(sender instanceof Player)){
	    	plugin.getLogger().info(plugin.aquaprefix+"STAFF ONLINE"+plugin.aquasufix);
	    	Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	    	for (Player oplayer : onlinePlayers){
	    	    if ((oplayer != null) && (oplayer.hasPermission("OasisChat.staff.a"))){
	    		plugin.getLogger().info(plugin.aquaprefix+oplayer.getName()+plugin.aquasufix);
	    	    }
	    	}
	        } else {
	    	sender.sendMessage(plugin.acprefix + "STAFF ONLINE");
	    	Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	    	for (Player oplayer : onlinePlayers){
	    	    if ((oplayer != null) && (oplayer.hasPermission("OasisChat.staff.a"))){
	    		sender.sendMessage(plugin.acprefix + oplayer.getName());
	    	    }
	    	}
	        }
	        return true;

	    case A:
	        if (args.length == 0) {
	    	if (sender instanceof Player){
	    	    if(sender.hasPermission("OasisChat.staff.a")){
	    		if (plugin.chattoggle.containsKey(sender.getName())){
	    		    if (plugin.chattoggle.containsValue("oasischat.players.p")) {
	    			plugin.chattoggle.put(sender.getName(),"oasischat.staff.a");
	    			sender.sendMessage(plugin.acprefix + "Adminchat " + ChatColor.GREEN + "ENABLED");
	    		    } else {
	    			plugin.chattoggle.remove(sender.getName());
	    			sender.sendMessage(plugin.acprefix + "Adminchat " + ChatColor.RED + "DISABLED");
	    		    }
	    		} else {
	    		    plugin.chattoggle.put(sender.getName(),"oasischat.staff.a");
	    		    sender.sendMessage(plugin.acprefix + "Adminchat " + ChatColor.GREEN + "ENABLED");
	    		}
	    	    }
	    	}
	        } else {
	    	String prefix = plugin.acprefix + "{" + plugin.sncprefix + sender.getName() + plugin.acprefix + "} ";
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(args[0]);

	    	for (int i = 1; i < args.length; i++) {
	    	    buffer.append(" ");
	    	    buffer.append(args[i]);
	    	}

	    	String message = buffer.toString();
	    	plugin.getServer().broadcast(prefix + message, "oasischat.staff.a");
	        }
	        return true;

	    default:
	        return false;

	    }
	} catch (Throwable e) {
	    e.printStackTrace();
	    plugin.printStackTrace(e);
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
