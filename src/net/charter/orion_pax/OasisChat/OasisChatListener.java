package net.charter.orion_pax.OasisChat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class OasisChatListener implements Listener{
	
	private OasisChat plugin;
	
	public OasisChatListener(OasisChat plugin){
		this.plugin = plugin;
	}
	
	public static Economy economy = null;
	
	@EventHandler(priority=EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        String username = vote.getUsername();
		Player player = Bukkit.getPlayer(username);

		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

		if (player != null){
			economy.depositPlayer(player.getName(), plugin.amount);
			player.sendMessage("Thanks for voting on " + vote.getServiceName() + "!");
			player.sendMessage("100" + " has been added to your iConomy balance.");
		}
    }
	
	@EventHandler
	public void OnAsyncPlayerChat(AsyncPlayerChatEvent event) {
		synchronized (plugin.staff)
		{
			if (plugin.staff.contains(event.getPlayer().getName())){
				String prefix = ChatColor.AQUA + "{" + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.AQUA + "} ";
				plugin.getLogger().info(plugin.aquaprefix+"<A> " + event.getPlayer().getName() + " - " + event.getMessage()+plugin.aquasufix);
				Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
				for (Player player : onlinePlayers){
					if ((player != null) && (player.hasPermission("OasisChat.staff"))){
						player.sendMessage(prefix + event.getMessage());
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	 public void OnPlayerJoin(PlayerJoinEvent event) {
	       
	}
	
	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(player.hasPermission("OasisChat.staff")){
			if (plugin.staff.contains(player.getName())){
				plugin.staff.remove(player.getName());
			}
		
		}
	}
	
	@EventHandler
	public void OnPlayerKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		
		if(player.hasPermission("OasisChat.staff")){
			if (plugin.staff.contains(player.getName())){
				plugin.staff.remove(player.getName());
			}
		
		}
	}
	
}
