package net.charter.orion_pax.OasisChat;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public final class PartyPlayer {

	private OasisChat plugin;
	private String name;
	private Team myparty;
	private boolean partytoggle;
	private boolean admintoggle;
	private boolean staff;
	private Team partyspychat;
	private String invite;
	
	public PartyPlayer(OasisChat plugin, String name){
		this.plugin = plugin;
		this.name = name;
		this.myparty = getMyParty();
		this.partytoggle = false;
		this.admintoggle = false;
		this.partyspychat = null;
		this.invite = "";
		if (plugin.getServer().getPlayer(name).getPlayer().hasPermission("oasischat.staff.a")){
			this.staff = true;
		} else {
			this.staff = false;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getPToggle(){
		return partytoggle;
	}
	
	public boolean isStaff(){
		return staff;
	}
	
	public boolean getAToggle(){
		return admintoggle;
	}
	
	public void sendMessage(String msg){
		plugin.getServer().getPlayer(name).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public void sendTeamChat(String msg){
		for (OfflinePlayer Oplayer : myparty.getPlayers()){
			if (Oplayer.isOnline()) {
				Player player = (Player) Oplayer;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
	}
	
	public Team getMyParty(){
		Set<Team> teams = plugin.SMPboard.getTeams();
		for(Team myteam : teams){
			if (myteam.hasPlayer(plugin.getServer().getOfflinePlayer(name))){
				return myteam;
			}
		}
		return null;
	}
	
	public void removeParty(){
		if (myparty.hasPlayer(plugin.getServer().getOfflinePlayer(name))) {
			myparty.removePlayer(plugin.getServer().getOfflinePlayer(name));
			myparty = null;
		}
	}
	
	public void changeParty(Team myparty){
		if (this.myparty!=null) {
			this.myparty.removePlayer(plugin.getServer().getOfflinePlayer(name));
		}
		this.myparty = myparty;
		myparty.addPlayer(plugin.getServer().getOfflinePlayer(name));
	}

	public void setPToggle(boolean b) {
		partytoggle = b;
	}

	public void setAToggle(boolean b) {
		admintoggle = b;
	}
	
	public void setPartySpyChat(Team spyteam){
		partyspychat = spyteam;
	}
	
	public Team getPartySpyChat(){
		return partyspychat;
	}

	public void setInvite(String myparty) {
		invite = myparty;
	}
	
	public String getInvite(){
		return invite;
	}
}
