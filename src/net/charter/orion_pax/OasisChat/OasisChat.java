package net.charter.orion_pax.OasisChat;


import java.io.File;
import java.util.*;
import net.charter.orion_pax.OasisChat.Commands.*;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;



public class OasisChat extends JavaPlugin {

	public ConsoleCommandSender console;
	public HashMap<String, String> adminchattoggle = new HashMap<String, String>();
	public HashMap<String, String> partychattoggle = new HashMap<String, String>();
	public HashMap<String, String> partyhash = new HashMap<String, String>();
	public HashMap<String, String> invite = new HashMap<String, String>();
	public HashMap<String, String> partyspy = new HashMap<String, String>();
	public HashMap<String,PermissionAttachment> perms = new HashMap<String,PermissionAttachment>();
	String aquaprefix = (char)27+"[1;36m";
	String aquasufix = (char)27+"[22;39m";
	String greenprefix = (char)27+"[1;32m";
	String greensufix = (char)27+"[22;39m";
	public static String pcprefix; //PartyChat Color prefix
	public static String pncprefix; //PlayerName Color prefix
	public static String sncprefix; //StaffNameChat Color prefix
	public static String acprefix; //AdminChat Color prefix
	public boolean disable = false;
	File file = new File("plugins/OasisChat/paxserrorlog.txt");

	public PartyChat party = new PartyChat(this);
	OasisChatListener ChatListener = new OasisChatListener(this);

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		setupconfig();
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
		partytask.runTaskTimer(this, 10, 12000);
		getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
	}

	@Override
	public void onDisable(){
		this.saveConfig();
		getLogger().info(aquaprefix+"OasisChat has been disabled!"+aquasufix);
	}

	public void setupconfig(){
		if (!(getConfig().contains("partychats"))){
			getConfig().createSection("partychats");
			saveConfig();
		}
	}

	public void setup(){
		acprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("adminchatcolor");
		pcprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("partychatcolor");
		sncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("staffnamechatcolor");
		pncprefix = this.getConfig().getConfigurationSection("ingameconfigurable").getString("playernamechatcolor");
	}
	
	BukkitRunnable partychat = new BukkitRunnable(){
		@Override
		public void run(){
			
		}
	};

	public BukkitRunnable partytask = new BukkitRunnable(){
		@Override
		public void run(){
			boolean needreset = false;
			Set<String> parties = party.getParties();
			if (parties==null){return;}
			for (String thisparty : parties){
				if (!getConfig().contains("partychats." + thisparty + ".owner")){
					getConfig().set("partychats." + thisparty, null);
					needreset = true;
				}
				if (!getConfig().contains("partychats." + thisparty + ".password")){
					getConfig().set("partychats." + thisparty, null);
					needreset = true;
				}
				if (!getConfig().contains("partychats." + thisparty + ".members")){
					getConfig().set("partychats." + thisparty, null);
					needreset = true;
				}
			}
			if (needreset){
				party.resetchats();
			}
			saveConfig();
		}
	};

}
