package net.charter.orion_pax.OasisChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Collections;
import java.util.Map;

public class OasisChatListener implements Listener{

	private OasisChat plugin;
	public OasisChatListener(OasisChat plugin){
		this.plugin = plugin;
	}

	String adminchatprefix;
	String partychatprefix;
	Map<String, String> syncadminT;
	Map<String, String> syncpartyT;

	@EventHandler
	public void OnAsyncPlayerChat(AsyncPlayerChatEvent event) {
		syncadminT = Collections.synchronizedMap(plugin.adminchattoggle);
		syncpartyT = Collections.synchronizedMap(plugin.partychattoggle);
		synchronized (syncadminT) {
			if (syncadminT.containsKey(event.getPlayer())) {
				if (syncadminT.get(event.getPlayer().getName()).toString().equalsIgnoreCase("on")) {
					String thismsg = OasisChat.acprefix + "{" + OasisChat.sncprefix + event.getPlayer().getName() + OasisChat.acprefix + "} " + event.getMessage();

					for (Map.Entry<String, String> entry : syncadminT.entrySet()) {
						Bukkit.getServer().getPlayer(entry.getKey()).sendMessage(ChatColor.translateAlternateColorCodes('&', thismsg));
					}
					plugin.console.sendMessage(ChatColor.translateAlternateColorCodes('&', thismsg));
					event.setCancelled(true);
					return;
				}
			}
		}
		synchronized (syncpartyT) {
			if (syncpartyT.get(event.getPlayer().getName()) != "off") {
				String prefix = ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "<" + ChatColor.translateAlternateColorCodes('&', OasisChat.pncprefix) + syncpartyT.get(event.getPlayer().getName()) + ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + "> - " + ChatColor.translateAlternateColorCodes('&', OasisChat.pncprefix) + event.getPlayer().getName() + ChatColor.translateAlternateColorCodes('&', OasisChat.pcprefix) + ": ";
				plugin.console.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', event.getMessage()));
				for (Map.Entry<String, String> entry : syncpartyT.entrySet()) {
					if (syncpartyT.get(event.getPlayer().getName()) == entry.getValue()) {
						Bukkit.getServer().getPlayer(entry.getKey()).sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', event.getMessage()));
					}
				}
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.perms.put(player.getName(), player.addAttachment(plugin));
		String myparty = plugin.party.myParty(player);
		plugin.partychattoggle.put(player.getName(), "off");
		if (myparty==null){

		} else {
			plugin.partyhash.put(player.getName(), "oasischat.party." + myparty);
			plugin.perms.get(player.getName()).setPermission("oasischat.party." + myparty, true);
		}
		if (player.hasPermission("oasischat.staff.a")){
			plugin.adminchattoggle.put(player.getName(),"off");
		}
	}

	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.partyhash.remove(player.getName());
		plugin.partychattoggle.remove(player.getName());
		plugin.adminchattoggle.remove(player.getName());
		plugin.perms.remove(player.getName());
	}

	@EventHandler
	public void OnPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		plugin.partyhash.remove(player.getName());
		plugin.partychattoggle.remove(player.getName());
		plugin.adminchattoggle.remove(player.getName());
		plugin.perms.remove(player.getName());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		Player attacker = null;
		Player defender = null;

		if (event.getDamager() instanceof Player){
			attacker = (Player) event.getDamager();
		}

		if (event.getEntity() instanceof Player){
			defender = (Player) event.getEntity();
		}

		if ((defender != null) && (attacker !=null)) {
			if (plugin.partyhash.containsKey(attacker.getName())) {
				if (plugin.partyhash.containsKey(defender.getName())) {
					if (plugin.partyhash.get(attacker.getName()).equals(
							plugin.partyhash.get(defender.getName()))) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
