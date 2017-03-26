package me.F_o_F_1092.PayForCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommand;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.JSONMessage;
import me.F_o_F_1092.PayForCommand.PluginManager.JSONMessageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Math;
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
			replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
			cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
		} else if (args[0].equalsIgnoreCase("help")) {
			if (!(args.length >= 1 && args.length <= 2)) {
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!(cs instanceof Player)) {
					if (args.length != 1) {
						String replaceCommand = plugin.msg.get("msg.13");
						replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
						cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
					} else {
						HelpPageListener.sendNormalMessage(cs);
					}
				} else {
					Player p = (Player)cs;
						if (args.length == 1) {
						HelpPageListener.sendMessage(p, 0);
					} else {
						if (!Math.isNumber(args[1])) {
							String replaceCommand = plugin.msg.get("msg.13");
							replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
							cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
						} else {
							if (Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > HelpPageListener.getMaxPlayerPages(p)) {
								String replaceCommand = plugin.msg.get("msg.13");
								replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
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
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc yes").getColoredCommand());
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
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc no").getColoredCommand());
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
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc info").getColoredCommand());
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
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
				String replaceCommand = plugin.msg.get("msg.13");
				replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc reload").getColoredCommand());
				cs.sendMessage(plugin.msg.get("[PayForCommand]") + replaceCommand); 
			} else {
				if (!cs.hasPermission("PayForCommand.Reload")) {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.2"));
				} else {
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.11"));
					
					plugin.playerCommand.clear();
					
					if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
						plugin.vault = true;
					} else {
						plugin.vault = false;
					}
					
					CommandListener.clearCommands();
					
					File fileCommand = new File("plugins/PayForCommand/Commands.yml");
					FileConfiguration ymlFileCommand = YamlConfiguration.loadConfiguration(fileCommand);

					if(!fileCommand.exists()) {
						try {
							ymlFileCommand.save(fileCommand);
							ymlFileCommand.set("Version", UpdateListener.getUpdateDoubleVersion());
							ymlFileCommand.set("TestCommand1.Name", "/TestCommand1 give");
							ArrayList<String> commands1 = new ArrayList<String>();
							commands1.add("/TC give");
							commands1.add("/TC i");
							ymlFileCommand.set("TestCommand1.Aliases", commands1);
							ymlFileCommand.set("TestCommand1.Price.Money", 49.50);
							ymlFileCommand.set("TestCommand1.Permission", "The.Default.Command.Permission.*");
							
							ymlFileCommand.set("TestCommand2.Name", "/TestCommand2 give");
							ArrayList<String> commands2 = new ArrayList<String>();
							commands2.add("/TC give");
							commands2.add("/TC i");
							ymlFileCommand.set("TestCommand2.Aliases", commands2);
							ymlFileCommand.set("TestCommand2.Price.Item.Material", "DIAMOND");
							ymlFileCommand.set("TestCommand2.Price.Item.Amount", 5);
							ymlFileCommand.set("TestCommand2.Price.Item.SubID", 0);
							ymlFileCommand.set("TestCommand2.Permission", "The.Default.Command.Permission.*");
							ymlFileCommand.save(fileCommand);
						} catch (IOException e1) {
							System.out.println("\u001B[31m[PayForCommand] Can't create the Config.yml. [" + e1.getMessage() +"]\u001B[0m");
						}
					}
					
					for (String strg : ymlFileCommand.getKeys(false)) {
						if (!strg.equals("Version")) {
							try {
								ArrayList<String> commands = new ArrayList<String>();
								commands.add(ymlFileCommand.getString(strg + ".Name"));
								
								if (ymlFileCommand.contains(strg + ".Aliases")) {
									commands.addAll(ymlFileCommand.getStringList(strg + ".Aliases"));
								}
								
								PayForCommand payForCommand = new PayForCommand(commands);
								if (ymlFileCommand.contains(strg + ".Price.Money")) {
									payForCommand.setMoneyPrice(ymlFileCommand.getDouble(strg + ".Price.Money"));
								} else if (ymlFileCommand.contains(strg + ".Price.Item")) {
									if (ymlFileCommand.contains(strg + ".Price.Item.Material")) {
										if (Material.getMaterial(ymlFileCommand.getString(strg + ".Price.Item.Material")) != null) {
											ItemStack is = new ItemStack(Material.getMaterial(ymlFileCommand.getString(strg + ".Price.Item.Material")));
											
											if (ymlFileCommand.contains(strg + ".Price.Item.SubID")) {
												is = new ItemStack(Material.getMaterial(ymlFileCommand.getString(strg + ".Price.Item.Material")), 1, Short.parseShort(ymlFileCommand.get(strg + ".Price.Item.SubID") + ""));
											}
											
											if (ymlFileCommand.contains(strg + ".Price.Item.Amount")) {
												is.setAmount(ymlFileCommand.getInt(strg + ".Price.Item.Amount"));
											}
											
											payForCommand.setItemPrice(is.getType(), is.getAmount(), is.getDurability());
										}
									}
								}
								
								if (ymlFileCommand.contains(strg + ".Permission")) {
									payForCommand.setPermission(ymlFileCommand.getString(strg + ".Permission"));
								}
								
								PayForCommandListener.addCommand(payForCommand);
							} catch (Exception e) {
								System.out.println("\u001B[31m[PayForCommand] Faild to load the Configuration for the Command \"" + strg + "\". [" + e.getMessage() +"]\u001B[0m");
							}
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
							ymlFileMessage.set("Message.3", "Do you want to pay &a&l[MONEY]$&2 to use the \ncommand: &a&l[COMMAND]&2?");
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
							ymlFileMessage.set("Message.16", "Do you want to pay &a&l[NUMBER]&2 &a&l[ITEM](s)&2 to use the \ncommand: &a&l[COMMAND]&2?");
							ymlFileMessage.set("Message.17", "You need &a&l[NUMBER]&2 more &a&l[ITEM](s)&2 to use this command.");
							ymlFileMessage.set("Message.18", "You payed &a&l[NUMBER]&2 &a&l[ITEM](s)&2 to use this command.");
							ymlFileMessage.set("Message.19", "Unable to find a command price.");
							ymlFileMessage.set("Message.20", "You have to hold the item in your hand.");
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
							System.out.println("\u001B[31m[PayForCommand] Can't create the Messages.yml. [" + e1.getMessage() +"]\u001B[0m");
						}
					}

					plugin.msg.put("[PayForCommand]", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("[PayForCommand]")));
					plugin.msg.put("color.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.1")));
					plugin.msg.put("color.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.2")));
					plugin.msg.put("msg.1", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.1")));
					plugin.msg.put("msg.2", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.2")));
					plugin.msg.put("msg.3", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.3")));
					plugin.msg.put("msg.4-5", ChatColor.translateAlternateColorCodes('&', "[\"\",{\"text\":\"           \"},{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand yes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\"}]}}},{\"text\":\"                      \"},{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand no\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\"}]}}}]))"));
					plugin.msg.put("msg.6", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.6")));
					plugin.msg.put("msg.7", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.7")));
					plugin.msg.put("msg.8", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.8")));
					plugin.msg.put("msg.9", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.9")));
					plugin.msg.put("msg.10", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.10")));
					plugin.msg.put("msg.11", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.11")));
					plugin.msg.put("msg.12", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.12")));
					plugin.msg.put("msg.13", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.13")));
					plugin.msg.put("msg.14", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.14")));
					plugin.msg.put("msg.15", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.15")));
					plugin.msg.put("msg.16", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.16")));
					plugin.msg.put("msg.17", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.17")));
					plugin.msg.put("msg.18", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.18")));
					plugin.msg.put("msg.19", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.19")));
					plugin.msg.put("msg.20", ChatColor.translateAlternateColorCodes('&', plugin.msg.get("color.1") + ymlFileMessage.getString("Message.20")));
					plugin.msg.put("helpTextGui.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.1")));
					plugin.msg.put("helpTextGui.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.2")));
					plugin.msg.put("helpTextGui.3", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.3")));
					plugin.msg.put("helpTextGui.4", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.4")));


					HelpPageListener.initializeHelpPageListener("/PayForCommand help", plugin.msg.get("[PayForCommand]"));
					
					CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.PluginManager.Command("/pfc help (Page)", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.1"))));
					CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.PluginManager.Command("/pfc info", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.2"))));
					CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.PluginManager.Command("/pfc yes", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.3"))));
					CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.PluginManager.Command("/pfc no", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.4"))));
					CommandListener.addCommand(new me.F_o_F_1092.PayForCommand.PluginManager.Command("/pfc reload", "PayForCommand.Reload", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.5"))));
					
					
					cs.sendMessage(plugin.msg.get("[PayForCommand]") + plugin.msg.get("msg.12"));
				}
			}
		} else {
			String replaceCommand = plugin.msg.get("msg.22");
			replaceCommand = replaceCommand.replace("[COMMAND]", CommandListener.getCommand("/pfc help (Page)").getColoredCommand());
			cs.sendMessage(plugin.msg.get("[TimeVote]") + replaceCommand); 
		}
		
		return true;
	}

}
