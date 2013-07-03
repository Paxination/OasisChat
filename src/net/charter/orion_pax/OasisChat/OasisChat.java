package net.charter.orion_pax.OasisChat;


import java.util.*;

import net.charter.orion_pax.OasisChat.Commands.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;



public class OasisChat extends JavaPlugin {

	public ConsoleCommandSender console;
	public final HashMap<String, Parties> MyParties = new HashMap<String, Parties>();
	public final HashMap<String, PartyPlayer> partyPlayer = new HashMap<String, PartyPlayer>();
	String aquaprefix = (char)27+"[1;36m";
	String aquasufix = (char)27+"[22;39m";
	String greenprefix = (char)27+"[1;32m";
	String greensufix = (char)27+"[22;39m";
	public String pcprefix; //PartyChat Color prefix
	public String pncprefix; //PlayerName Color prefix
	public String sncprefix; //StaffNameChat Color prefix
	public String acprefix; //AdminChat Color prefix
	public MyConfigFile partyconfig;
	
	public String[] oasischatsub = {
			ChatColor.GOLD + "Usage: /oasischat subcommand [args]"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "SAVE - Saves config"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "LIST - List settings that can be changed in game"
			,ChatColor.GOLD + "SET - Sets in game settings for oasischat"
			,ChatColor.GOLD + "DEBUG - turns on debug."
	};
	
	public String[] partychatsub = {
			ChatColor.translateAlternateColorCodes('&', pcprefix + "Usage: /party subcommand [args]")
			,ChatColor.translateAlternateColorCodes('&', pcprefix + "SubCommands:")
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
		getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
	}

	@Override
	public void onDisable(){
		this.saveConfig();
		getLogger().info(aquaprefix+"OasisChat has been disabled!"+aquasufix);
	}

	public void setup(){
		acprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("adminchatcolor");
		pcprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("partychatcolor");
		sncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("staffnamechatcolor");
		pncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("playernamechatcolor");
	}
	
//	BukkitRunnable partychat = new BukkitRunnable(){
//		@Override
//		public void run(){
//			
//		}
//	};
	
	public void loadParties(){
		Set<String> parties = this.partyconfig.getConfig().getConfigurationSection("partychats").getKeys(false);
		if (parties!=null){
			for (String party : parties){
				String owner = this.partyconfig.getConfig().getString("partychats." + party + ".owner");
				String password = this.partyconfig.getConfig().getString("partychats." + party + ".password");
				List<String> members = this.partyconfig.getConfig().getStringList("partychats." + party + ".members");
				this.MyParties.put(party, new Parties(this, owner,party,password,members));
			}
		}
	}
	
	public void saveParties(){
		Iterator it = this.MyParties.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();
			Parties party = (Parties) entry.getValue();
			this.partyconfig.getConfig().set("partychats."+entry.getKey()+".owner", party.getOwner());
			this.partyconfig.getConfig().set("partychats."+entry.getKey()+".password", party.getPassword());
			this.partyconfig.getConfig().set("partychats."+entry.getKey()+".members", party.getMembers());
			it.remove();
		}
		this.partyconfig.saveConfig();
	}
}
