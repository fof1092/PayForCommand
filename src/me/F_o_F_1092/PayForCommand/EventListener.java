package me.F_o_F_1092.PayForCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;


import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommand;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommandListener;
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
			
			if (PayForCommandListener.isCommand(e.getMessage())) {
				PayForCommand payForCommand = PayForCommandListener.getCommand(e.getMessage());
				
				if (!payForCommand.hasItemPrice() && !payForCommand.hasMoneyPrice()) {
					p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.19"));
				} else {
					if (payForCommand.hasItemPrice() && !plugin.vault) {
						p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.14"));
					} else {
						if (payForCommand.getPermission() != null && !p.hasPermission(payForCommand.getPermission())) {
							p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.1")); 
						} else {
							if (!plugin.playerCommand.containsKey(p.getUniqueId()) || plugin.playerCommand.containsKey(p.getUniqueId()) && !plugin.playerCommand.get(p.getUniqueId()).equals(e.getMessage())) {
								p.sendMessage(plugin.msg.get("msg.6") + plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.6"));
								p.sendMessage("");
								
								String replaceString = null;
								
								if (payForCommand.hasMoneyPrice()) {
									replaceString = plugin.msg.get("msg.3");
									replaceString = replaceString.replace("[MONEY]", payForCommand.getMoneyPrice() + "");
									replaceString = replaceString.replace("[COMMAND]", e.getMessage());
								} else if (payForCommand.hasItemPrice()) {
									replaceString = plugin.msg.get("msg.16");
									replaceString = replaceString.replace("[NUMBER]", payForCommand.getItemPrice().getAmount() + "");
									replaceString = replaceString.replace("[ITEM]", payForCommand.getItemPrice().getType().toString() + "");
									replaceString = replaceString.replace("[COMMAND]", e.getMessage());
								}
								
								p.sendMessage(replaceString); 
								
								p.sendMessage("");
								p.sendMessage("");
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + plugin.msg.get("msg.4-5"));
								
								p.sendMessage("");
								p.sendMessage(plugin.msg.get("msg.6") + plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.6"));
								
								plugin.playerCommand.put(p.getUniqueId(), e.getMessage());
								
								e.setCancelled(true);
							} else {
								if (payForCommand.hasMoneyPrice()) {
									if (getVault().getBalance(p) < payForCommand.getMoneyPrice()) {
										p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.8"));
										e.setCancelled(true);
									} else {
										getVault().withdrawPlayer(p, payForCommand.getMoneyPrice());
										
										String replaceString = plugin.msg.get("msg.9");
										replaceString = replaceString.replace("[MONEY]", payForCommand.getMoneyPrice() + "");
										p.sendMessage(plugin.msg.get("[PayForCommand]") + replaceString); 
									}
								} else if (payForCommand.hasItemPrice()) {
									
									if (p.getInventory().getItemInMainHand().getType() != payForCommand.getItemPrice().getType()) {
										p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.20"));
										
										e.setCancelled(true);
									} else {
										if (p.getInventory().getItemInMainHand().getAmount() < payForCommand.getItemPrice().getAmount()) {
											String replaceString = plugin.msg.get("msg.17");
											replaceString = replaceString.replace("[NUMBER]", payForCommand.getItemPrice().getAmount() + "");
											replaceString = replaceString.replace("[ITEM]", payForCommand.getItemPrice().getType().toString() + "");
											replaceString = replaceString.replace("[COMMAND]", e.getMessage());
											
											p.sendMessage(replaceString); 
											
											e.setCancelled(true);
										} else {
											
											if (p.getInventory().getItemInMainHand().getAmount() == payForCommand.getItemPrice().getAmount()) {
												p.getInventory().setItemInMainHand(null);
											} else {
												p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - payForCommand.getItemPrice().getAmount());
											}
											
											String replaceString = plugin.msg.get("msg.18");
											replaceString = replaceString.replace("[NUMBER]", payForCommand.getItemPrice().getAmount() + "");
											replaceString = replaceString.replace("[ITEM]", payForCommand.getItemPrice().getType().toString() + "");
											replaceString = replaceString.replace("[COMMAND]", e.getMessage());
											
											p.sendMessage(replaceString); 
										}
									}
								}
							}
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
