package net.charter.orion_pax.OasisChat.Commands;

import net.charter.orion_pax.OasisChat.OasisChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreditsCommand implements CommandExecutor {

	private OasisChat plugin; // pointer to your main class, unrequired if you don't need methods from the main class

	public CreditsCommand(OasisChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Beta tests n thanks in no particular order....");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "The Staff of Oasis-SMP");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Paxination, thats right I did it, I'm in here!");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Big_Piglet");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "AtractiveBanana");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Olive474");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "OSGATharp");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "realgriffin");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Jawell");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Keshaluver21");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Zwall99");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Tanaka91");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "Grimtongue");
		sender.sendMessage(ChatColor.ITALIC.toString() + "" + ChatColor.BLUE + "and...");
		sender.sendMessage(ChatColor.ITALIC.toString() + ChatColor.BLUE + "The FRIENDLY folk at bukkit dev!");
		return true;
	}
}