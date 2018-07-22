package me.F_o_F_1092.PayForCommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;


import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommand;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.JSONMessage;
import me.F_o_F_1092.PayForCommand.PluginManager.UpdateListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Spigot.JSONMessageListener;
import net.milkbowl.vault.economy.Economy;

public class EventListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();

		if (UpdateListener.isAnewUpdateAvailable()) {
			if (p.hasPermission("PayForCommand.UpdateMessage")) {
				p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.15"));
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
					p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.19"));
				} else {
					if (payForCommand.hasItemPrice() && !Options.vault) {
						p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.14"));
					} else {
						if (payForCommand.getPermission() != null && !p.hasPermission(payForCommand.getPermission())) {
							p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.1")); 
						} else {
							if (!Options.playerCommand.containsKey(p.getUniqueId()) || Options.playerCommand.containsKey(p.getUniqueId()) && !Options.playerCommand.get(p.getUniqueId()).equals(e.getMessage())) {
								p.sendMessage(Options.msg.get("msg.6") + Options.msg.get("[PayForCommand]") + Options.msg.get("msg.6"));
								p.sendMessage("");
								
								String replaceString = null;
								
								if (payForCommand.hasMoneyPrice()) {
									replaceString = Options.msg.get("msg.3");
									replaceString = replaceString.replace("[MONEY]", payForCommand.getMoneyPrice() + "");
									replaceString = replaceString.replace("[COMMAND]", e.getMessage());
								} else if (payForCommand.hasItemPrice()) {
									replaceString = Options.msg.get("msg.16");
									replaceString = replaceString.replace("[NUMBER]", payForCommand.getItemPrice().getAmount() + "");
									replaceString = replaceString.replace("[ITEM]", payForCommand.getItemPrice().getType().toString() + "");
									replaceString = replaceString.replace("[COMMAND]", e.getMessage());
								}
								
								p.sendMessage(replaceString); 
								
								p.sendMessage("");
								p.sendMessage("");
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + Options.msg.get("msg.4-5"));
								
								List<JSONMessage> jsonMessages = new ArrayList<JSONMessage>();
								
								JSONMessage jsonMsgSpace1 = new JSONMessage("           ");
								
								JSONMessage jsonMsgYes = new JSONMessage(Options.msg.get("msg.4"));
								jsonMsgYes.setRunCommand("/PayForCommand yes");
								jsonMsgYes.setHoverText(Options.msg.get("msg.4"));
								
								JSONMessage jsonMsgSpace2 = new JSONMessage("                      ");
								
								JSONMessage jsonMsgNo = new JSONMessage(Options.msg.get("msg.5"));
								jsonMsgNo.setRunCommand("/PayForCommand no");
								jsonMsgNo.setHoverText(Options.msg.get("msg.5"));
								
								jsonMessages.add(jsonMsgSpace1);
								jsonMessages.add(jsonMsgYes);
								jsonMessages.add(jsonMsgSpace2);
								jsonMessages.add(jsonMsgNo);
								
								JSONMessageListener.send(p, JSONMessageListener.putJSONMessagesTogether(jsonMessages));
								
								p.sendMessage("");
								p.sendMessage(Options.msg.get("msg.6") + Options.msg.get("[PayForCommand]") + Options.msg.get("msg.6"));
								
								Options.playerCommand.put(p.getUniqueId(), e.getMessage());
								
								e.setCancelled(true);
							} else {
								if (payForCommand.hasMoneyPrice()) {
									if (getVault().getBalance(p) < payForCommand.getMoneyPrice()) {
										p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.8"));
										e.setCancelled(true);
									} else {
										getVault().withdrawPlayer(p, payForCommand.getMoneyPrice());
										
										String replaceString = Options.msg.get("msg.9");
										replaceString = replaceString.replace("[MONEY]", payForCommand.getMoneyPrice() + "");
										p.sendMessage(Options.msg.get("[PayForCommand]") + replaceString); 
									}
								} else if (payForCommand.hasItemPrice()) {
									
									if (p.getInventory().getItemInMainHand().getType() != payForCommand.getItemPrice().getType()) {
										p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.20"));
										
										e.setCancelled(true);
									} else {
										if (p.getInventory().getItemInMainHand().getAmount() < payForCommand.getItemPrice().getAmount()) {
											String replaceString = Options.msg.get("msg.17");
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
											
											String replaceString = Options.msg.get("msg.18");
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
