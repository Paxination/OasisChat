package net.charter.orion_pax.OasisChat.Commands.SubCommands;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.entity.Player;

public class Invite {

	private OasisChat plugin;
	
	public void timer(final Player player){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable()
		{
			public void run()
			{
				plugin.partyPlayer.get(player.getName()).setInvite("");
			}
		}
		, 6000);
	}
}
