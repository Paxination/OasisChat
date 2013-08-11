package net.charter.orion_pax.OasisChat;

import java.util.ArrayList;
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
	private List<String> members = new ArrayList<String>();
	private OasisChat plugin;
	
	public Parties(OasisChat plugin, String owner, String myparty, String password, List<String>members){
		this.plugin = plugin;
		this.owner = owner;
		this.myparty = myparty;
		this.password = password;
		this.members = members;
	}
	
	public void addMember(String name){
		if (!members.contains(name)){
			members.add(name);
			plugin.partyPlayer.get(name).changeParty(myparty);
		}
	}
	
	public void removeMember(String name){
		members.remove(name);
		plugin.partyPlayer.get(name).removeParty();
	}
	
	public void changePassword(String newpassword){
		password = newpassword;
	}
	
	public void changeOwner(String newowner){
		owner = newowner;
	}
	
	public List<String> getMembers(){
		return members;
	}
	
	public boolean isOwner(String name){
		if (name.equals(owner)){
			return true;
		}
		
		return false;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void sendMessage(String msg){
		if (members!=null) {
			for (String member : members) {
				Player player = plugin.getServer().getPlayer(member);
				if (player != null) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
				}
			}
		}
		plugin.getLogger().info(myparty);
		plugin.getLogger().info(password);
		plugin.getLogger().info(owner);
		plugin.getServer().getPlayer(owner).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		plugin.console.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
		return owner;
	}
}
