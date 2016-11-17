package me.F_o_F_1092.PayForCommand;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.F_o_F_1092.PayForCommand.Command.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpMessage;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.UpdateListener;

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
				cs.sendMessage("§2Version: §a" + UpdateListener.getUpdateStringVersion());
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
					
					if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
						plugin.vault = true;
					} else {
						plugin.vault = false;
					}
					
					File fileCommand = new File("plugins/PayForCommand/Commands.yml");
					FileConfiguration ymlFileCommand = YamlConfiguration.loadConfiguration(fileCommand);

					if(!fileCommand.exists()) {
						try {
							ymlFileCommand.save(fileCommand);
							ymlFileCommand.set("Version", UpdateListener.getUpdateDoubleVersion());
							ymlFileCommand.set("TestCommand.Name", "/TestCommand give");
							ymlFileCommand.set("TestCommand.Price", 49.50);
							ymlFileCommand.save(fileCommand);
						} catch (IOException e1) {
							System.out.println("\u001B[31m[PayForCommand] ERROR: 001 | Can't create the Config.yml. [" + e1.getMessage() +"]\u001B[0m");
						}
					}
					
					for (String strg : ymlFileCommand.getKeys(false)) {
						if (!strg.equals("Version")) {
							CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.Command.Command(ymlFileCommand.getString(strg + ".Name"), ymlFileCommand.getDouble(strg + ".Price")));
						}
					}
					
					
					File fileMessages = new File("plugins/PayForCommand/Messages.yml");
					FileConfiguration ymlFileMessage = YamlConfiguration.loadConfiguration(fileMessages);

					if(!fileMessages.exists()) {
						try {
							ymlFileMessage.save(fileMessages);
							ymlFileMessage.set("Version", UpdateListener.getUpdateDoubleVersion());
							ymlFileMessage.set("[PayForCommand]", "&a[&2PayForCommand&a] ");
							ymlFileMessage.set("Color.1", "&2");
							ymlFileMessage.set("Color.2", "&a");
							ymlFileMessage.set("Message.1", "You have to be a player, to use this command.");
							ymlFileMessage.set("Message.2", "You do not have the permission for this command.");
							ymlFileMessage.set("Message.3", "Do you whant to pay &a&l[MONEY]$&2 to use the \ncommand: &a&l[COMMAND]&2?");
							ymlFileMessage.set("Message.4", "&a&lYES");
							ymlFileMessage.set("Message.5", "&c&lNO");
							ymlFileMessage.set("Message.6", "&a&m&l---------&r ");
							ymlFileMessage.set("Message.7", "There is no command you can accept or deny.");
							ymlFileMessage.set("Message.8", "You do not have enought money to use this command.");
							ymlFileMessage.set("Message.9", "You payed &a&l[MONEY]$&2 to use this command.");
							ymlFileMessage.set("Message.10", "You canceled the purchase of the command &a&l[COMMAND]&2.");
						    ymlFileMessage.set("Message.11", "The plugin is reloading...");
						    ymlFileMessage.set("Message.12", "Reloading completed.");
						    ymlFileMessage.set("Message.13", "Try [COMMAND]");
						    ymlFileMessage.set("Message.14", "The Plugin vault has not been found on the server!");
						    ymlFileMessage.set("Message.15", "There is a new update available for this plugin. &a( https://fof1092.de/Plugins/PFC )&2");
							ymlFileMessage.set("HelpTextGui.1", "&a[&2Click to use this command&a]");
							ymlFileMessage.set("HelpTextGui.2", "&a[&2Next page&a]");
							ymlFileMessage.set("HelpTextGui.3", "&a[&2Last page&a]");
							ymlFileMessage.set("HelpTextGui.4", "&7&oPage [PAGE]. &7Click on the arrows for the next page.");
							ymlFileMessage.set("HelpText.1", "This command shows you the help page.");
							ymlFileMessage.set("HelpText.2", "This command shows you the info page.");
							ymlFileMessage.set("HelpText.3", "This command accepts the buying of the command.");
							ymlFileMessage.set("HelpText.4", "This command denys the buying of the command.");
							ymlFileMessage.set("HelpText.5", "This command is reloading the Config.yml and Commands.yml file.");
							ymlFileMessage.save(fileMessages);
						} catch (IOException e1) {
							System.out.println("\u001B[31m[PayForCommand] ERROR: 002 | Can't create the Messages.yml. [" + e1.getMessage() +"]\u001B[0m");
						}
					}

					plugin.msg.put("[PayForCommand]", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("[PayForCommand]")));
					plugin.msg.put("color.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.1")));
					plugin.msg.put("color.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.2")));
					plugin.msg.put("plugin.msg.1", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.1")));
					plugin.msg.put("plugin.msg.2", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.2")));
					plugin.msg.put("plugin.msg.3", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.3")));
					plugin.msg.put("plugin.msg.4-5", ChatColor.translateAlternateColorCodes('&', "[\"\",{\"text\":\"           \"},{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand yes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\"}]}}},{\"text\":\"                      \"},{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand no\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\"}]}}}]))"));
					plugin.msg.put("plugin.msg.6", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.6")));
					plugin.msg.put("plugin.msg.7", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.7")));
					plugin.msg.put("plugin.msg.8", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.8")));
					plugin.msg.put("plugin.msg.9", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.9")));
					plugin.msg.put("plugin.msg.10", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.10")));
					plugin.msg.put("plugin.msg.11", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.11")));
					plugin.msg.put("plugin.msg.12", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.12")));
					plugin.msg.put("plugin.msg.13", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.13")));
					plugin.msg.put("plugin.msg.14", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.14")));
					plugin.msg.put("helpTextGui.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.1")));
					plugin.msg.put("helpTextGui.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.2")));
					plugin.msg.put("helpTextGui.3", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.3")));
					plugin.msg.put("helpTextGui.4", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.4")));
					plugin.msg.put("helpText.1", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.2") + ymlFileMessage.getString("HelpText.1")));
					plugin.msg.put("helpText.2", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.2") + ymlFileMessage.getString("HelpText.2")));
					plugin.msg.put("helpText.3", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.2") + ymlFileMessage.getString("HelpText.3")));
					plugin.msg.put("helpText.4", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.2") + ymlFileMessage.getString("HelpText.4")));
					plugin.msg.put("helpText.5", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.2") + ymlFileMessage.getString("HelpText.5")));

					HelpPageListener.setPluginNametag(plugin.msg.get("[PayForCommand]"));
					
					HelpPageListener.addHelpMessage(new HelpMessage(null, plugin.msg.get("helpText.1"), "/pfc help (Page)"));
					HelpPageListener.addHelpMessage(new HelpMessage(null, plugin.msg.get("helpText.2"), "/pfc info"));
					HelpPageListener.addHelpMessage(new HelpMessage(null, plugin.msg.get("helpText.3"), "/pfc yes"));
					HelpPageListener.addHelpMessage(new HelpMessage(null, plugin.msg.get("helpText.4"), "/pfc no"));
					HelpPageListener.addHelpMessage(new HelpMessage("PayForCommand.Reload", plugin.msg.get("helpText.5"), "/pfc reload"));
					
					
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
