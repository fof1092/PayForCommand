package me.F_o_F_1092.PayForCommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.F_o_F_1092.PayForCommand.PluginManager.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.JSONMessage;
import me.F_o_F_1092.PayForCommand.PluginManager.Spigot.HelpPageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Spigot.JSONMessageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Spigot.UpdateListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Math;

public class CommnandPayForCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			String replaceCommand = Options.msg.get("msg.13");
			replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
			cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
		} else if (args[0].equalsIgnoreCase("help")) {
			if (!(args.length >= 1 && args.length <= 2)) {
				String replaceCommand = Options.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
				cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					if (args.length != 1) {
						String replaceCommand = Options.msg.get("msg.13");
						replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
						cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
					} else {
						HelpPageListener.sendNormalMessage(cs);
					}
				} else {
					Player p = (Player)cs;
						if (args.length == 1) {
						HelpPageListener.sendMessage(p, 0);
					} else {
						if (!Math.isInt(args[1])) {
							String replaceCommand = Options.msg.get("msg.13");
							replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
							cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
						} else {
							if (Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > HelpPageListener.getMaxPlayerPages(p)) {
								String replaceCommand = Options.msg.get("msg.13");
								replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
								cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
							} else {
								HelpPageListener.sendMessage(p, Integer.parseInt(args[1]) - 1);
							}
						}
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("yes")) {
			if (args.length != 1) {
				String replaceCommand = Options.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc yes").getColoredCommand());
				cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					cs.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.1"));
				} else {
					Player p = (Player)cs;
					if (!Options.playerCommand.containsKey(p.getUniqueId())) {
						p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.7"));
					} else {
						p.chat(Options.playerCommand.get(p.getUniqueId()));
						
						Options.playerCommand.remove(p.getUniqueId());
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("no")) {
			if (args.length != 1) {
				String replaceCommand = Options.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc no").getColoredCommand());
				cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					cs.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.1"));
				} else {
					Player p = (Player)cs;
					if (!Options.playerCommand.containsKey(p.getUniqueId())) {
						p.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.7"));
					} else {
						String replaceString = Options.msg.get("msg.10");
						replaceString = replaceString.replace("[COMMAND]", Options.playerCommand.get(p.getUniqueId()));
						cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceString); 
						
						Options.playerCommand.remove(p.getUniqueId());
						
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("info")) {
			if (args.length != 1) {
				String replaceCommand = Options.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc info").getColoredCommand());
				cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				cs.sendMessage("§2§m-----------§a [§2PayForCommand§a] §2§m-----------");
				cs.sendMessage("");
				
				if (cs instanceof Player) {
					Player p = (Player) cs;
					
					List<JSONMessage> jsonFoFMessages = new ArrayList<JSONMessage>();
					
					JSONMessage FoFText = new JSONMessage("§2By: ");
					JSONMessage FoFLink = new JSONMessage("§aF_o_F_1092");
					FoFLink.setHoverText("§2[§aOpen my Website§2]");
					FoFLink.setOpenURL("https://fof1092.de");
					
					jsonFoFMessages.add(FoFText);
					jsonFoFMessages.add(FoFLink);
					
					JSONMessageListener.send(p, JSONMessageListener.putJSONMessagesTogether(jsonFoFMessages));
					
					cs.sendMessage("");
					
					List<JSONMessage> jsonTwitterMessages = new ArrayList<JSONMessage>();
					
					JSONMessage twitterText = new JSONMessage("§2Twitter: ");
					JSONMessage twitterLink = new JSONMessage("§a@F_o_F_1092");
					twitterLink.setHoverText("§2[§aOpen Twitter§2]");
					twitterLink.setOpenURL("https://twitter.com/F_o_F_1092");
					
					jsonTwitterMessages.add(twitterText);
					jsonTwitterMessages.add(twitterLink);
					
					JSONMessageListener.send(p, JSONMessageListener.putJSONMessagesTogether(jsonTwitterMessages));
				
					cs.sendMessage("");
					cs.sendMessage("§2Version: §a" + UpdateListener.getUpdateStringVersion());
					
					List<JSONMessage> jsonPluginPageMessages = new ArrayList<JSONMessage>();
					
					JSONMessage pluginWebsiteText = new JSONMessage("§2PayForCommand: ");
					JSONMessage pluginWebsiteLink = new JSONMessage("§ahttps://fof1092.de/Plugins/PFC");
					pluginWebsiteLink.setHoverText("§2[§aOpen the Plugin Page§2]");
					pluginWebsiteLink.setOpenURL("https://fof1092.de/Plugins/PFC");
					
					jsonPluginPageMessages.add(pluginWebsiteText);
					jsonPluginPageMessages.add(pluginWebsiteLink);
					
					JSONMessageListener.send(p, JSONMessageListener.putJSONMessagesTogether(jsonPluginPageMessages));
				
				} else {
					cs.sendMessage("§2By: §aF_o_F_1092");
					cs.sendMessage("");
					cs.sendMessage("§2Twitter: §a@F_o_F_1092");
					cs.sendMessage("");
					cs.sendMessage("§2Version: §a" + UpdateListener.getUpdateStringVersion());
					cs.sendMessage("§2PayForCommand: §ahttps://fof1092.de/Plugins/PFC");
				}
				
				cs.sendMessage("");
				cs.sendMessage("§2§m-----------§a [§2PayForCommand§a] §2§m-----------");
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (args.length != 1) {
				String replaceCommand = Options.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc reload").getColoredCommand());
				cs.sendMessage(Options.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!cs.hasPermission("PayForCommand.Reload")) {
					cs.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.2"));
				} else {
					cs.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.11"));
					
					Main.disable();
					Main.setup();
					
					cs.sendMessage(Options.msg.get("[PayForCommand]") + Options.msg.get("msg.12"));
				}
			}
		} else {
			String replaceCommand = Options.msg.get("msg.22");
			replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
			cs.sendMessage(Options.msg.get("[TimeVote]") + replaceCommand); 
		}
		
		return true;
	}

}
