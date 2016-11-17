package me.F_o_F_1092.PayForCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.F_o_F_1092.PayForCommand.Command.Command;
import me.F_o_F_1092.PayForCommand.Command.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.UpdateListener;
import net.milkbowl.vault.economy.Economy;

public class EventListener implements Listener {

	private Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();

		if (UpdateListener.isAnewUpdateAvailable()) {
			if (p.hasPermission("PayForCommand.UpdateMessage")) {
				p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.15"));
			}
		}
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if (!e.isCancelled()) {
			Player p = (Player) e.getPlayer();
			
			if (CommandListener.isCommand(e.getMessage())) {
				if (!plugin.vault) {
					p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.14"));
				} else {
					Command command = CommandListener.getCommand(e.getMessage());
					
					if (!plugin.playerCommand.containsKey(p.getUniqueId()) || plugin.playerCommand.containsKey(p.getUniqueId()) && !plugin.playerCommand.get(p.getUniqueId()).equals(e.getMessage())) {
						p.sendMessage(plugin.msg.get("msg.6") + plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.6"));
						p.sendMessage("");
						
						String replaceString = plugin.msg.get("msg.3");
						replaceString = replaceString.replace("[MONEY]", command.getPrice() + "");
						replaceString = replaceString.replace("[COMMAND]", e.getMessage());
						p.sendMessage(replaceString); 
						
						p.sendMessage("");
						p.sendMessage("");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + plugin.msg.get("msg.4-5"));
						
						p.sendMessage("");
						p.sendMessage(plugin.msg.get("msg.6") + plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.6"));
						
						plugin.playerCommand.put(p.getUniqueId(), e.getMessage());
						
						e.setCancelled(true);
					} else {
						if (getVault().getBalance(p) < command.getPrice()) {
							p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.8"));
							e.setCancelled(true);
						} else {
							getVault().withdrawPlayer(p, command.getPrice());
							
							String replaceString = plugin.msg.get("msg.9");
							replaceString = replaceString.replace("[MONEY]", command.getPrice() + "");
							p.sendMessage(plugin.msg.get("[PayForCommand]") + replaceString); 
						}
					}
				}
			}
		}
	}
	
	public static Economy getVault() {
		return Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
	}
}
