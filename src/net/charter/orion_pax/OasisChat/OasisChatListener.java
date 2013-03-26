package net.charter.orion_pax.OasisChat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.permissions.PermissionAttachment;

public class OasisChatListener implements Listener{

    private OasisChat plugin;
    public OasisChatListener(OasisChat plugin){
	this.plugin = plugin;
    }


    @EventHandler
    public void OnAsyncPlayerChat(AsyncPlayerChatEvent event) {
	synchronized (plugin.chattoggle)
	{
	    String myparty = plugin.party.myParty(event.getPlayer());
	    if (plugin.chattoggle.containsKey(event.getPlayer().getName())){
		if (plugin.chattoggle.containsValue("oasischat.staff.a")) {
		    String prefix = plugin.acprefix + "{" + plugin.sncprefix
			    + event.getPlayer().getName() + plugin.acprefix
			    + "} ";
		    plugin.getLogger().info(
			    plugin.aquaprefix + "<A> "
				    + event.getPlayer().getName() + " - "
				    + event.getMessage() + plugin.aquasufix);
		    plugin.getServer().broadcast(prefix + event.getMessage(),
			    "oasischat.staff.a");
		    event.setCancelled(true);
		} else if (plugin.chattoggle.containsValue("oasischat.players.p")){
		    String prefix = plugin.pcprefix + "{" + plugin.pncprefix
			    + event.getPlayer().getName() + plugin.pcprefix
			    + "} ";
		    plugin.getLogger().info(
			    plugin.greenprefix + "<" + myparty + "> "
				    + event.getPlayer().getName() + " - "
				    + event.getMessage() + plugin.greensufix);
		    plugin.getServer().broadcast(prefix + event.getMessage(),
			    plugin.partyhash.get(event.getPlayer().getName()));
		    event.setCancelled(true);
		}
	    }
	}
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
	Player player = event.getPlayer();
	plugin.perms.put(player.getName(), player.addAttachment(plugin));
	String myparty = plugin.party.myParty(player);
	if (myparty==null){
	    return;
	} else {
	    plugin.partyhash.put(player.getName(), "oasischat.party." + myparty);
	    plugin.perms.get(player.getName()).setPermission("oasischat.party." + myparty, true);
	}
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
	Player player = event.getPlayer();
	plugin.partyhash.remove(player.getName());
	plugin.chattoggle.remove(player.getName());
	plugin.perms.remove(player.getName());
    }

    @EventHandler
    public void OnPlayerKick(PlayerKickEvent event){
	Player player = event.getPlayer();
	plugin.partyhash.remove(player.getName());
	plugin.chattoggle.remove(player.getName());
	plugin.perms.remove(player.getName());
    }

    @EventHandler
    public void onEntityDamage2(EntityDamageByEntityEvent event){
	Player attacker = null;
	Player defender = null;

	if (event.getDamager() instanceof Player){
	    attacker = (Player) event.getDamager();
	}

	if (event.getEntity() instanceof Player){
	    defender = (Player) event.getEntity();
	}

	if ((defender != null) && (attacker !=null)) {
	    if (plugin.partyhash.get(attacker.getName()).equals(plugin.partyhash.get(defender.getName()))) {
		event.setCancelled(true);
	    }
	}
    }

}
