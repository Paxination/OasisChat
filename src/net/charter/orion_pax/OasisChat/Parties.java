package net.charter.orion_pax.OasisChat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Parties {

	private String owner;
	private String myparty;
	private String password;
	private List<String> members;
	private OasisChat plugin;
	
	public Parties(OasisChat plugin, String owner, String myparty, String password, List<String>members){
		this.owner = owner;
		this.myparty = myparty;
		this.password = password;
		this.members = members;
	}
	
	public void addMember(String name){
		if (!this.members.contains(name)){
			this.members.add(name);
			plugin.partyPlayer.get(name).changeParty(myparty);
		}
	}
	
	public void removeMember(String name){
		this.members.remove(name);
		plugin.partyPlayer.get(name).removeParty();
	}
	
	public void changePassword(String password){
		this.password = password;
	}
	
	public void changeOwner(String owner){
		this.owner = owner;
	}
	
	public List<String> getMembers(){
		return this.members;
	}
	
	public boolean isOwner(String name){
		if (name.equals(this.owner)){
			return true;
		}
		
		return false;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void sendMessage(String msg){
		for (String member:members){
			Player player = plugin.getServer().getPlayer(member);
			if (player!=null){
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
	}
	
	public void removeMembers(){
		for (String member:members){
			Player player = plugin.getServer().getPlayer(member);
			if (player!=null){
				plugin.partyPlayer.get(player.getName()).removeParty();
			}
		}
		plugin.saveParties();
	}

	public String getOwner() {
		return this.owner;
	}
}
