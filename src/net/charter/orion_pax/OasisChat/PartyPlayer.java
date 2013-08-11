package net.charter.orion_pax.OasisChat;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class PartyPlayer {

	private OasisChat plugin;
	private String name;
	private String myparty;
	private boolean partytoggle;
	private boolean admintoggle;
	private boolean staff;
	private String partyspychat;
	private String invite;
	
	public PartyPlayer(OasisChat plugin, String name){
		this.plugin = plugin;
		this.name = name;
		this.myparty = getMyParty();
		this.partytoggle = false;
		this.admintoggle = false;
		this.partyspychat = "";
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
	
	public String getMyParty(){
		Iterator it = plugin.MyParties.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			Parties party = (Parties) entry.getValue();
			if (party.getMembers().contains(name)){
				return (String) entry.getKey();
			}
			if (party.getOwner().equals(name)){
				return (String) entry.getKey();
			}
		}
		return null;
	}
	
	public String myParty(){
		return myparty;
	}
	
	public void removeParty(){
		myparty=null;
	}
	
	public void changeParty(String myparty){
		myparty=myparty;
	}

	public void setPToggle(boolean b) {
		partytoggle = b;
	}

	public void setAToggle(boolean b) {
		admintoggle = b;
	}
	
	public void setPartySpyChat(String chat){
		partyspychat = chat;
	}
	
	public String getPartySpyChat(){
		return partyspychat;
	}

	public void setInvite(String myparty) {
		invite = myparty;
	}
	
	public String getInvite(){
		return invite;
	}
}
