package net.charter.orion_pax.OasisChat;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PartyPlayer {

	private OasisChat plugin;
	private Player player;
	private String name;
	private String myparty;
	private boolean partytoggle;
	private boolean admintoggle;
	private boolean staff;
	private String partyspychat;
	private String invite;
	
	public PartyPlayer(OasisChat plugin, Player player){
		this.plugin = plugin;
		this.player = player;
		this.name = player.getName();
		this.myparty = getMyParty();
		this.partytoggle = false;
		this.admintoggle = false;
		this.partyspychat = "";
		this.invite = "";
		if (!player.hasPermission("oasischat.staff.a")){
			this.staff = false;
		} else {
			this.staff = true;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getPToggle(){
		return this.partytoggle;
	}
	
	public boolean isStaff(){
		return this.staff;
	}
	
	public boolean getAToggle(){
		return this.admintoggle;
	}
	
	public void sendMessage(String msg){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public String getMyParty(){
		Iterator it = plugin.MyParties.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry entry = (Map.Entry) it.next();
			Parties party = (Parties) entry.getValue();
			if (party.getMembers().contains(this.name)){
				this.myparty=(String) entry.getKey();
				return this.myparty;
			}
			if (party.getOwner().equals(this.name)){
				this.myparty=(String) entry.getKey();
				return this.myparty;
			}
			it.remove();
		}
		return null;
	}
	
	public void removeParty(){
		this.myparty=null;
	}
	
	public void changeParty(String myparty){
		this.myparty=myparty;
	}

	public void setPToggle(boolean b) {
		this.partytoggle = b;
	}

	public void setAToggle(boolean b) {
		this.admintoggle = b;
	}
	
	public void setPartySpyChat(String chat){
		this.partyspychat = chat;
	}
	
	public String getPartySpyChat(){
		return this.partyspychat;
	}

	public void setInvite(String myparty) {
		this.invite = myparty;
	}
	
	public String getInvite(){
		return this.invite;
	}
}
