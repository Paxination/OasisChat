package net.charter.orion_pax.OasisChat;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

import org.apache.commons.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;



public class OasisChat extends JavaPlugin {

	ConsoleCommandSender console;
	ConcurrentHashMap<String, String> adminchattoggle = new ConcurrentHashMap<String, String>();
	ConcurrentHashMap<String, String> partychattoggle = new ConcurrentHashMap<String, String>();
	HashMap<String, String> partyhash = new HashMap<String, String>();
	HashMap<String, String> invite = new HashMap<String, String>();
	HashMap<String, String> partyspy = new HashMap<String, String>();
	HashMap<String,PermissionAttachment> perms = new HashMap<String,PermissionAttachment>();
	String aquaprefix = (char)27+"[1;36m";
	String aquasufix = (char)27+"[22;39m";
	String greenprefix = (char)27+"[1;32m";
	String greensufix = (char)27+"[22;39m";
	static String pcprefix; //PartyChat Color prefix
	static String pncprefix; //PlayerName Color prefix
	static String sncprefix; //StaffNameChat Color prefix
	static String acprefix; //AdminChat Color prefix
	File file = new File("plugins/OasisChat/paxserrorlog.txt");

	PartyChat party = new PartyChat(this);
	OasisChatListener ChatListener = new OasisChatListener(this);

	@Override
	public void onEnable() {
		try {
			this.saveDefaultConfig();
			setupconfig();
			setup();
			Bukkit.getPluginManager().registerEvents(new OasisChatListener(this), this);
			getCommand("a").setExecutor(new OasisChatCommand(this));
			getCommand("staff").setExecutor(new OasisChatCommand(this));
			getCommand("oasischat").setExecutor(new OasisChatCommand(this));
			getCommand("p").setExecutor(new OasisChatCommand(this));
			getCommand("party").setExecutor(new OasisChatCommand(this));
			getCommand("psay").setExecutor(new OasisChatCommand(this));
			getCommand("pspyon").setExecutor(new OasisChatCommand(this));
			getCommand("pspyoff").setExecutor(new OasisChatCommand(this));
			getCommand("listparties").setExecutor(new OasisChatCommand(this));
			getCommand("partyinfo").setExecutor(new OasisChatCommand(this));
			getCommand("credits").setExecutor(new OasisChatCommand(this));
			console = Bukkit.getServer().getConsoleSender();
			partytask.runTaskTimer(this, 10, 12000);
			getLogger().info(aquaprefix+"OasisChat has been enabled!"+aquasufix);
		} catch (Throwable e) {

			printStackTrace(e, "OnEnable");
		}
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

	void printStackTrace(Throwable t, String cmd){
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss,a");
			Date date = new Date();
			if (!file.exists()){file.createNewFile();}
			StringWriter sw = new StringWriter();
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
			PrintWriter pw = new PrintWriter(sw);
			out.println("**BEGIN**");
			out.println("OasisChat");
			out.println(dateFormat.format(date));
			out.println(cmd);
			t.printStackTrace(pw);
			for(String l: sw.toString().replace("\r", "").split("\n")){
				out.println(l);
				getServer().broadcast(l, "oasis.debug");
			}
			pw.close();
			out.println("**END**");
			out.close();
			sw.close();
		} catch (IOException e) {

		}
	}
	
	BukkitRunnable partychat = new BukkitRunnable(){
		@Override
		public void run(){
			
		}
	};

	BukkitRunnable partytask = new BukkitRunnable(){
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
