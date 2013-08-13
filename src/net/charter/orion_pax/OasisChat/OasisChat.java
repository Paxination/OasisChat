package net.charter.orion_pax.OasisChat;


import java.util.*;

import net.charter.orion_pax.OasisChat.Commands.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;



public class OasisChat extends JavaPlugin {

	public ConsoleCommandSender console;
	//public HashMap<String, Parties> MyParties = new HashMap<String, Parties>();
	public HashMap<String, PartyPlayer> partyPlayer = new HashMap<String, PartyPlayer>();
	public HashMap<Team, String> MyParties2 = new HashMap<Team, String>();
	public HashMap<Team, String> SpyChat = new HashMap<Team, String>();
	String aquaprefix = (char)27+"[1;36m";
	String aquasufix = (char)27+"[22;39m";
	String greenprefix = (char)27+"[1;32m";
	String greensufix = (char)27+"[22;39m";
	public String pcprefix; //PartyChat Color prefix
	public String pncprefix; //PlayerName Color prefix
	public String sncprefix; //StaffNameChat Color prefix
	public String acprefix; //AdminChat Color prefix
	public MyConfigFile partyconfig;
	
	public ScoreboardManager PartyChat = Bukkit.getScoreboardManager();
	public Scoreboard SMPboard = PartyChat.getNewScoreboard();
	
	public String[] oasischatsub = {
			ChatColor.GOLD + "Usage: /oasischat subcommand [args]"
			,ChatColor.GOLD + "net.charter.orion_pax.OasisChat.SubCommands:"
			,ChatColor.GOLD + "SAVE - Saves config"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "LIST - List settings that can be changed in game"
			,ChatColor.GOLD + "SET - Sets in game settings for oasischat"
			,ChatColor.GOLD + "DEBUG - turns on debug."
	};
	
	public String[] partychatsub = {
			ChatColor.translateAlternateColorCodes('&', pcprefix + "Usage: /party subcommand [args]")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "net.charter.orion_pax.OasisChat.SubCommands:")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "CREATE partyname password (password optional)")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "JOIN partyname password (password option)")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "INVITE playername - invites player to partychat")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "ACCEPT - accepts invite, 5 min time limit")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "KICK playername")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "List - list members of your party")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "PASSWORD password")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "GIVE playername")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "QUIT - quits current party chat")
	};

	//public PartyChat party = new PartyChat(this);
	OasisChatListener ChatListener = new OasisChatListener(this);

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		setup();
		Bukkit.getPluginManager().registerEvents(new OasisChatListener(this), this);
		getCommand("a").setExecutor(new ACommand(this));
		getCommand("staff").setExecutor(new StaffCommand(this));
		getCommand("oasischat").setExecutor(new OasisChatCommand(this));
		getCommand("p").setExecutor(new PCommand(this));
		getCommand("party").setExecutor(new PartyCommand(this));
		getCommand("psay").setExecutor(new PsayCommand(this));
		getCommand("pspyon").setExecutor(new PspyonCommand(this));
		getCommand("pspyoff").setExecutor(new PspyoffCommand(this));
		getCommand("listparties").setExecutor(new ListpartiesCommand(this));
		getCommand("partyinfo").setExecutor(new PartyinfoCommand(this));
		getCommand("credits").setExecutor(new CreditsCommand(this));
		console = Bukkit.getServer().getConsoleSender();
		partyconfig = new MyConfigFile(this, "partychat.yml");
		this.partyconfig.saveDefaultConfig();
		this.partyconfig.reloadConfig();
		loadParties();
		getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
	}

	@Override
	public void onDisable(){
		this.saveConfig();
		this.saveParties();
		getLogger().info(aquaprefix+"OasisChat has been disabled!"+aquasufix);
	}

	public void setup(){
		acprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("adminchatcolor");
		pcprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("partychatcolor");
		sncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("staffnamechatcolor");
		pncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("playernamechatcolor");
	}
	
	public void loadParties(){
		need to redo this method!
	}
	
	public void saveParties(){
		need to redo this method!
	}
}
