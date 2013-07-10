package net.charter.orion_pax.OasisChat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class OasisChatListener implements Listener{

	private OasisChat plugin;
	public OasisChatListener(OasisChat plugin){
		this.plugin = plugin;
	}

	private Map<String, PartyPlayer> syncPartyPlayer;


	@EventHandler
	public void OnAsyncPlayerChat(AsyncPlayerChatEvent event) {
		syncPartyPlayer = Collections.synchronizedMap(plugin.partyPlayer);
		String name = event.getPlayer().getName();
		String acprefix = plugin.acprefix;
		String pcprefix = plugin.pcprefix;
		String sncprefix = plugin.sncprefix;
		String pncprefix = plugin.pncprefix;

		synchronized (syncPartyPlayer) {
			if (syncPartyPlayer.containsKey(name)) {
				if (syncPartyPlayer.get(name).isStaff()) {
					if (syncPartyPlayer.get(name).getAToggle()) {
						String thismsg = acprefix + "{" + sncprefix + event.getPlayer().getName() + acprefix + "} " + event.getMessage();
						Iterator it = syncPartyPlayer.entrySet().iterator();
						while (it.hasNext()){
							Map.Entry entry = (Map.Entry) it.next();
							if (syncPartyPlayer.get(entry.getKey()).isStaff()){
								syncPartyPlayer.get(entry.getKey()).sendMessage(thismsg);
							}
						}
						plugin.console.sendMessage(ChatColor.translateAlternateColorCodes('&', thismsg));
						event.setCancelled(true);
						return;
					}
				} else {
					if (syncPartyPlayer.get(name).getPToggle()){
						String prefix = pcprefix + "<" + pncprefix + name + pcprefix + "> - " + pncprefix + event.getPlayer().getName() + pcprefix + ": ";
						plugin.console.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + event.getMessage()));
						Iterator it = syncPartyPlayer.entrySet().iterator();
						while (it.hasNext()){
							Map.Entry entry = (Map.Entry)it.next();
							if (syncPartyPlayer.get(entry.getKey()).getMyParty().equals(syncPartyPlayer.get(name).getMyParty())){
								syncPartyPlayer.get(entry.getKey()).sendMessage(prefix + event.getMessage());
							}
						}
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.partyPlayer.put(player.getName(), new PartyPlayer(plugin,player.getName()));
	}

	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.partyPlayer.remove(player.getName());
	}

	@EventHandler
	public void OnPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		plugin.partyPlayer.remove(player.getName());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		Player attacker = null;
		Player defender = null;

		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			attacker = (Player) event.getDamager();
			defender = (Player) event.getEntity();
			if (plugin.partyPlayer.get(attacker.getName()).myParty().equals(plugin.partyPlayer.get(defender.getName()).myParty())){
				event.setCancelled(true);
			}
		}
	}

}
