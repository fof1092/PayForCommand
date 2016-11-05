package me.F_o_F_1092.PayForCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.F_o_F_1092.PayForCommand.Command.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;

public class CommnandPayForCommand implements CommandExecutor {

	private Main plugin;

	public CommnandPayForCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			String replaceCommand = plugin.msg.get("msg.13");
			replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
			cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
		} else if (args[0].equalsIgnoreCase("help")) {
			if (!(args.length >= 1 && args.length <= 2)) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					if (args.length != 1) {
						String replaceCommand = plugin.msg.get("msg.13");
						replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
						cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
					} else {
						HelpPageListener.sendNormalMessage(cs);
					}
				} else {
					Player p = (Player)cs;
						if (args.length == 1) {
						HelpPageListener.sendMessage(p, 0);
					} else {
						if (!HelpPageListener.isNumber(args[1])) {
							String replaceCommand = plugin.msg.get("msg.13");
							replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
							cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
						} else {
							if (Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > HelpPageListener.getMaxPlayerPages(p)) {
								String replaceCommand = plugin.msg.get("msg.13");
								replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
								cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
							} else {
								HelpPageListener.sendMessage(p, Integer.parseInt(args[1]) - 1);
							}
						}
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("yes")) {
			if (args.length != 1) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc yes"));
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.1"));
				} else {
					Player p = (Player)cs;
					if (!plugin.playerCommand.containsKey(p.getUniqueId())) {
						p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.7"));
					} else {
						p.chat(plugin.playerCommand.get(p.getUniqueId()));
						
						plugin.playerCommand.remove(p.getUniqueId());
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("no")) {
			if (args.length != 1) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc no"));
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.1"));
				} else {
					Player p = (Player)cs;
					if (!plugin.playerCommand.containsKey(p.getUniqueId())) {
						p.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.7"));
					} else {
						String replaceString = plugin.msg.get("msg.10");
						replaceString = replaceString.replace("[COMMAND]", plugin.playerCommand.get(p.getUniqueId()));
						cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceString); 
						
						plugin.playerCommand.remove(p.getUniqueId());
						
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("info")) {
			if (args.length != 1) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc info"));
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				cs.sendMessage("§2-----§a[§2PayForCommand§a]§2-----");
				cs.sendMessage("§2Version: §a1.0");
				cs.sendMessage("§2By: §aF_o_F_1092");
				cs.sendMessage("§2PayForCommand: §ahttps://fof1092.de/Plugins/PFC");
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (args.length != 1) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc reload"));
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!cs.hasPermission("PayForCommand.Reload")) {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.2"));
				} else {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.11"));
					
					CommandListener.clearCommands();
					plugin.playerCommand.clear();
					
					
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.12"));
				}
			}
		} else {
			String replaceCommand = plugin.msg.get("msg.22");
			replaceCommand = replaceCommand.replace("[COMMAND]", HelpPageListener.getColoredCommand("/pfc help (Page)"));
			cs.sendMessage(plugin.msg.get("[TimeVote]") + replaceCommand); 
		}
		
		return true;
	}

}
