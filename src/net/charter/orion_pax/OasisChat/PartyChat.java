package net.charter.orion_pax.OasisChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
    public Set<String> getParties(){
	if (plugin.getConfig().getConfigurationSection("partychats").getKeys(false)==null){return null;}
	Set<String> parties = plugin.getConfig().getConfigurationSection("partychats").getKeys(false);
	plugin.debug("getParties()", "parties",parties.toString());
	return parties;
    }


    /**Test to see if a player is an owner of a party
     * @param player - Player player
     * @return Returns true if player is owner of a party, false other wise.
     */
    public boolean isOwner(Player player){
	Set<String> parties = getParties();
	if (parties==null){return false;}
	for (String party : parties){
	    plugin.debug("isOwner(Player player)", "party",party);
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
	members = (List<String>) plugin.getConfig().getStringList("partychats." + myparty + ".members");
	plugin.debug("getMembers(String myparty)", "members",members.toString());
	return members;
    }

    /**Finds out if a player is a member of a party
     * @param player - Player player
     * @return Returns true if they are a member.
     */
    public boolean isMember(Player player){
	List<String> members = getMembers(myParty(player));
	for (String member : members){
	    plugin.debug("isMember()", "member",member);
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
	plugin.debug("getOnlineMembers(Player player)", "members",members.toString());
	List<String> online = new ArrayList<String>();
	online.add("ONLINE");
	for (String member : members){
	    plugin.debug("getOnlineMembers(Player player)", "member",member);
	    if (plugin.getServer().getPlayer(member).isOnline()){
		if (!(player == plugin.getServer().getPlayer(member))) {
		    online.add(member);
		}
	    }
	}
	plugin.debug("getOnlineMembers(Player player)", "online",online.toString());
	return online;
    }

    /**Finds the party that a player/owner is part of.
     * @param player - Player player
     * @return returns the party they are in, else null.
     */
    public String myParty(Player player){
	Set<String> parties = getParties();
	if (parties==null){return null;}
	for (String party : parties){
	    plugin.debug("myParty(Player player)", "party",party);
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
	plugin.debug("getPassword(String myparty)", "myparty",myparty);
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
	plugin.perms.get(oldowner.getName()).unsetPermission(plugin.partyhash.get(oldowner.getName()));
	plugin.perms.get(newowner.getName()).setPermission(plugin.partyhash.get(oldowner.getName()), true);
	plugin.getConfig().set("partychats." + myparty + ".owner", newowner.getName());
	plugin.partyhash.remove(oldowner);
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
	plugin.perms.get(player.getName()).setPermission(plugin.partyhash.get(player.getName()), true);
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
	plugin.perms.get(player.getName()).unsetPermission(plugin.partyhash.get(player.getName()));
	plugin.partyhash.remove(player.getName());
	plugin.saveConfig();
    }

    /**Deletes a party and notifies ALL members/owner of disbanded party!  Does not check for owner.
     * @param myparty - Players current party.
     */
    public void delParty(String myparty){
	List<String> members = getMembers(myparty);
	for (String member : members){
	    plugin.debug("delParty(String myparty)", "member",member);
	    Player player = plugin.getServer().getPlayer(member);
	    plugin.perms.get(player.getName()).unsetPermission(plugin.partyhash.get(player.getName()));
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
	plugin.perms.get(player.getName()).setPermission("oasischat.party." + party, true);
	plugin.getConfig().createSection("partychats." + party);
	plugin.getConfig().set("partychats." + party + ".owner", player.getName());
	plugin.getConfig().set("partychats." + party + ".password", password);
	plugin.getConfig().set("partychats." + party + ".members", "");
	plugin.saveConfig();
    }
}
