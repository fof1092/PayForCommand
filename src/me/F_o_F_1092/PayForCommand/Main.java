package me.F_o_F_1092.PayForCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.F_o_F_1092.PayForCommand.PluginManager.Command;
import me.F_o_F_1092.PayForCommand.PluginManager.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.ServerLog;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Spigot.UpdateListener;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommand;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommandListener;

public class Main extends JavaPlugin {
	
	static Main plugin;
	
	public static Main getPlugin() {
		return plugin;
	}
	
	@Override
	public void onEnable() {
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");

		plugin = this;
		
		ServerLog.setPluginTag("§a[§2PayForCommand§a]§2");
		UpdateListener.initializeUpdateListener(1.18, "1.1.8", 31350);
		UpdateListener.checkForUpdate();
		
		setup();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(), this);

		this.getCommand("PayForCommand").setExecutor(new CommnandPayForCommand());
		this.getCommand("PayForCommand").setTabCompleter(new CommnandPayForCommandTabCompleter());
	}

	@Override
	public void onDisable() {
		disable();
	}
	
	
	@SuppressWarnings("deprecation")
	public static void setup() {
		
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			Options.vault = true;
		}

		File fileCommand = new File("plugins/PayForCommand/Commands.yml");
		YamlConfiguration ymlFileCommand = YamlConfiguration.loadConfiguration(fileCommand);

		
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
				ServerLog.err("Can't create the Config.yml. [" + e1.getMessage() +"]");
			}
		} else {
			double version = ymlFileCommand.getDouble("Version");
			if (version < UpdateListener.getUpdateDoubleVersion()) {
				try {
					ymlFileCommand.set("Version", UpdateListener.getUpdateDoubleVersion());
					
					if (version <= 1.15) {
						for (String strg : ymlFileCommand.getKeys(false)) {
							if (!strg.equals("Version")) {
								double price = ymlFileCommand.getDouble(strg + ".Price");
								
								ymlFileCommand.set(strg + ".Price", null);
								
								ymlFileCommand.set(strg + ".Price.Money", price);
							}
						}
					}
					
					ymlFileCommand.save(fileCommand);
				} catch (IOException e) {		
					ServerLog.err("Can't update the Config.yml. [" + e.getMessage() +"]");
				}
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
					ServerLog.err("Faild to load the Configuration for the Command \"" + strg + "\". [" + e.getMessage() +"]");
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
				ServerLog.err("[PayForCommand] Can't create the Messages.yml. [" + e1.getMessage() +"]");
			}
		} else {
			double version = ymlFileMessage.getDouble("Version");
			if (version < UpdateListener.getUpdateDoubleVersion()) {
				try {
					ymlFileMessage.set("Version", UpdateListener.getUpdateDoubleVersion());
					
					if (version <= 1.14) {
						ymlFileMessage.set("Message.16", "Do you want to pay &a&l[NUMBER]&2 &a&l[ITEM](s)&2 to use the \ncommand: &a&l[COMMAND]&2?");
						ymlFileMessage.set("Message.17", "You need &a&l[NUMBER]&2 more &a&l[ITEM](s)&2 to use this command.");
						ymlFileMessage.set("Message.18", "You payed &a&l[NUMBER]&2 &a&l[ITEM](s)&2 to use this command.");
						ymlFileMessage.set("Message.19", "Unable to find a command price.");
						ymlFileMessage.set("Message.20", "You have to hold the item in your hand.");
					}
					
					ymlFileMessage.save(fileMessages);
				} catch (IOException e) {		
					ServerLog.err("[PayForCommand] Can't update the Messages.yml. [" + e.getMessage() +"]");
				}
			}
		}

		Options.msg.put("[PayForCommand]", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("[PayForCommand]")));
		Options.msg.put("color.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.1")));
		Options.msg.put("color.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.2")));
		Options.msg.put("msg.1", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.1")));
		Options.msg.put("msg.2", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.2")));
		Options.msg.put("msg.3", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.3")));
		Options.msg.put("msg.4", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.4")));
		Options.msg.put("msg.5", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.5")));
		Options.msg.put("msg.6", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.6")));
		Options.msg.put("msg.7", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.7")));
		Options.msg.put("msg.8", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.8")));
		Options.msg.put("msg.9", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.9")));
		Options.msg.put("msg.10", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.10")));
		Options.msg.put("msg.11", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.11")));
		Options.msg.put("msg.12", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.12")));
		Options.msg.put("msg.13", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.13")));
		Options.msg.put("msg.14", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.14")));
		Options.msg.put("msg.15", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.15")));
		Options.msg.put("msg.16", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.16")));
		Options.msg.put("msg.17", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.17")));
		Options.msg.put("msg.18", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.18")));
		Options.msg.put("msg.19", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.19")));
		Options.msg.put("msg.20", ChatColor.translateAlternateColorCodes('&', Options.msg.get("color.1") + ymlFileMessage.getString("Message.20")));
		Options.msg.put("helpTextGui.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.1")));
		Options.msg.put("helpTextGui.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.2")));
		Options.msg.put("helpTextGui.3", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.3")));
		Options.msg.put("helpTextGui.4", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.4")));

		
		HelpPageListener.initializeHelpPageListener("/PayForCommand help", Options.msg.get("[PayForCommand]"));
		
		CommandListener.addCommand(new Command("/pfc help (Page)", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.1"))));
		CommandListener.addCommand(new Command("/pfc info", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.2"))));
		CommandListener.addCommand(new Command("/pfc yes", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.3"))));
		CommandListener.addCommand(new Command("/pfc no", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.4"))));
		CommandListener.addCommand(new Command("/pfc reload", "PayForCommand.Reload", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.5"))));
	}
	
	public static void disable() {
		Options.msg.clear();
		Options.playerCommand.clear();
		Options.vault = false;
		
		PayForCommandListener.clearCommands();
		
		CommandListener.clearCommands();
		
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");
	}

}
