package net.charter.orion_pax.OasisChat;

import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;


/**This is the PartyChat class that has all the methods that are used for partychat in my OasisChat plugin.
 * @author Orion Pax
 * 
 */
public class PartyChat {

	private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

	public PartyChat(OasisChat plugin){
		this.plugin = plugin;
	}
	
	/**Method used to generate a list of available parties on the server!
	 * @return 
	 */
	public List<String> getParties(){
//		if (plugin.getConfig().getConfigurationSection("partychats").getValues(false)==null){return null;}
		List<String> parties = (List<String>) plugin.getConfig().getList("partychatslist");
		plugin.getLogger().info("parties is returned");
		return parties;
	}


	/**Test to see if a player is an owner of a party
	 * @param player - Player player
	 * @return Returns true if player is owner of a party, false other wise.
	 */
	public boolean isOwner(Player player){
		List<String> parties = getParties();
		for (String party : parties){
			if (plugin.getConfig().getString("partychats." + party + ".owner").contains(player.getName())){
				return true;
			}
		}
		return false;
	}

	/**Gets a list of members in a players party.
	 * @param myparty - The party that the player belongs to.
	 * @return 
	 */
	public List<String> getMembers(String myparty){
		List<String> members;
		return members = (List<String>) plugin.getConfig().getStringList("partychats." + myparty + ".members");
	}

	/**Finds out if a player is a member of a party
	 * @param player - Player player
	 * @return Returns true if they are a member.
	 */
	public boolean isMember(Player player){
		List<String> members = getMembers(myParty(player));
		for (String member : members){
			if (member.contains(player.getName())){
				return true;
			}
		}
		return false;
	}
	/**Gets a list of members that are part of a party that are ONLINE!
	 * @return returns a list of online users.
	 */
	public List<String> getOnlineMembers(Player player){
		List<String> members = getMembers(myParty(player));
		List<String> online =null;
		online.add("ONLINE");
		for (String member : members){
			if (plugin.getServer().getPlayer(member).isOnline()){
				if (!(player == plugin.getServer().getPlayer(member))) {
					online.add(member);
				}
			}
		}
		return online;
	}

	/**Finds the party that a player/owner is part of.
	 * @param player - Player player
	 * @return returns the party they are in, else null.
	 */
	public String myParty(Player player){
		List<String> parties = getParties();
		if (parties==null){return null;}
		for (String party : parties){
			if (plugin.getConfig().getStringList("partychats." + party + ".members").contains(player.getName())){
				return party;
			}
			if (isOwner(player)){
				return party;
			}
		}
		return null;
	}

	/**Gets the password that is set for that party, use to verify upon joining a party with the /party join command.
	 * @param myparty - Party that the player belongs to.
	 * @return returns the password
	 */
	public String getPassword(String myparty){
		return plugin.getConfig().getString("partychats." + myparty + ".password");
	}


	/**Gets the password that is set for that party, used to verify upon joining a party with the /party join command.
	 * @param myparty - PartyChat partyl
	 * @return returns password.
	 */

	/**Changes ownership of a party to a new player.  Does not verify owner.
	 * @param newowner - New owner of party chat
	 * @param oldowner - Old owner of party chat
	 * @param myparty - Players current party
	 */
	public void setOwner(Player newowner, Player oldowner, String myparty){
		oldowner.addAttachment(plugin, plugin.partyhash.get(oldowner.getName()), false);
		plugin.partyhash.remove(oldowner);
		plugin.getConfig().set("partychats." + myparty + ".owner", newowner.getName());
		plugin.saveConfig();
	}

	/**Changes password of a party.  Does not verify owner.
	 * @param player - Player player
	 * @param newpassword - New password
	 * @param myparty - Players current party
	 */
	public void setPassword(Player player, String newpassword, String myparty){
		plugin.getConfig().set("partychats." + myparty + ".password", newpassword);
		plugin.saveConfig();
	}

	/**Adds member to a party.  Does not verify owners or passwords.
	 * @param player - Player player
	 * @param myparty - String, Players current party.
	 */
	public void addMember(Player player, String myparty){
		List<String> members = getMembers(myparty);
		members.add(player.getName());
		plugin.getConfig().set("partychats." + myparty + ".members", members);
		plugin.partyhash.put(player.getName(), "oasischat.party." + myparty);
		player.addAttachment(plugin, plugin.partyhash.get(player.getName()), true);
		plugin.saveConfig();
	}

	/**Deletes member from a party.  Only removes members.  Does not verify owner.
	 * @param player - Player player
	 * @param myparty - Players current party.
	 */
	public void delMember(Player player, String myparty){
		List<String> members = getMembers(myparty);
		members.remove(player.getName());
		plugin.getConfig().set("partychats." + myparty + ".members", members);
		player.addAttachment(plugin, plugin.partyhash.get(player.getName()), false);
		plugin.partyhash.remove(player.getName());
		plugin.saveConfig();
	}

	/**Deletes a party and notifies ALL members/owner of disbanded party!  Does not check for owner.
	 * @param myparty - Players current party.
	 */
	public void delParty(String myparty){
		List<String> members = getMembers(myparty);
		for (String member : members){
			Player player = plugin.getServer().getPlayer(member);
			player.addAttachment(plugin, plugin.partyhash.get(member), false);
			plugin.partyhash.remove(member);
		}
		String ownername = plugin.getConfig().getString("partychats." + myparty + ".owner");
		plugin.getServer().getPlayer(ownername).addAttachment(plugin, plugin.partyhash.remove(ownername), false);
		plugin.getConfig().set("partychats." + myparty, null);
		plugin.saveConfig();
	}

	/**Creates a party.  Does not check if there is a party with that name already. And does not check if player already has a party.
	 * @param player - Player player
	 * @param party - String name of party
	 * @param password - String password
	 */
	public void createParty(Player player, String party, String password){
		plugin.partyhash.put(player.getName(), "oasischat.party." + party);
		player.addAttachment(plugin, plugin.partyhash.get(player.getName()), true);
		plugin.getConfig().createSection("partychats." + party);
		plugin.getConfig().set("partychats." + party + ".owner", player.getName());
		plugin.getConfig().set("partychats." + party + ".password", password);
		plugin.getConfig().set("partychats." + party + ".members", "");
		plugin.saveConfig();
	}
}
