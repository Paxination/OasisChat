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
	    if (plugin.debug){
		if (plugin.getConfig().getStringList("debugstaff").contains(event.getPlayer().getName())){
		    event.setCancelled(true);
		}
	    }
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerMove(PlayerMoveEvent event) {
	if (plugin.frozen.containsKey(event.getPlayer().getName())){

	    int fromX=(int)event.getFrom().getX();
	    int fromY=(int)event.getFrom().getY();
	    int fromZ=(int)event.getFrom().getZ();
	    int toX=(int)event.getTo().getX();
	    int toY=(int)event.getTo().getY();
	    int toZ=(int)event.getTo().getZ();

	    if(fromX!=toX||fromZ!=toZ||fromY!=toY){
		event.getPlayer().teleport(event.getFrom());
		event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT MOVE, YOU'RE " + ChatColor.AQUA + "FROZEN!");
		event.setCancelled(true);
	    }
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerBreakBlock(BlockBreakEvent event) {
	if (plugin.frozen.containsKey(event.getPlayer().getName())){
	    event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
	    event.setCancelled(true);
	}
	Set set = plugin.frozen.entrySet();
	Iterator i = set.iterator();
	while(i.hasNext()) {
	    Map.Entry me = (Map.Entry)i.next();
	    Location loc = (Location) me.getValue();
	    if (event.getBlock().getLocation().getBlockY() == loc.getY()-1){
		if (!(event.getPlayer().hasPermission("oasischat.staff.a"))){
		    event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS UNDER THE " + ChatColor.AQUA + "FROZEN" + ChatColor.RED + " PLAYER!");
		    event.setCancelled(true);
		}
	    }
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerRespawn(PlayerRespawnEvent event){
	Player player = event.getPlayer();
	if (plugin.frozen.containsKey(player.getName())) {
	    event.setRespawnLocation((Location) plugin.frozen.get(event.getPlayer().getName()));
	    event.getPlayer().sendMessage(ChatColor.RED + "RESPAWNED AT YOUR " + ChatColor.AQUA + "CHILLED " + ChatColor.RED + "LOCATION!");
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
	if (plugin.frozen.containsKey(event.getPlayer().getName())){
	    if (event.getMessage().contains("/")){
		event.setCancelled(true);
		event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
	    }
	}
	if ((event.getPlayer().hasPermission("oasischat.staff.a")) && (event.getMessage().contains("/ban"))) {
	    plugin.partyhash.remove(event.getPlayer().getName());
	    plugin.chattoggle.remove(event.getPlayer().getName());
	    plugin.perms.remove(event.getPlayer().getName());
	    Set set = plugin.frozen.entrySet();
	    Iterator i = set.iterator();
	    while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		if (event.getMessage().contains((CharSequence) me.getKey())){
		    plugin.frozen.remove(me.getKey());
		}
	    }
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerPlaceBlock(BlockPlaceEvent event){
	if (plugin.frozen.containsKey(event.getPlayer().getName())){
	    event.setCancelled(true);
	    event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
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
	
	if (plugin.getConfig().getStringList("debugstaff").contains(event.getPlayer().getName())){
	    PermissionAttachment debugperm = event.getPlayer().addAttachment(plugin);
	    debugperm.setPermission("oasischat.debug", true);
	    plugin.debug = !plugin.debug;
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

	if (plugin.partyhash.get(attacker.getName())==plugin.partyhash.get(defender.getName())){
	    event.setCancelled(true);
	}
    }

}
