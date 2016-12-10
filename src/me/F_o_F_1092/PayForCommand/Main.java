package me.F_o_F_1092.PayForCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommand;
import me.F_o_F_1092.PayForCommand.PayForCommand.PayForCommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.Command;
import me.F_o_F_1092.PayForCommand.PluginManager.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;
import me.F_o_F_1092.PayForCommand.PluginManager.UpdateListener;

public class Main extends JavaPlugin {

	public HashMap<String, String> msg = new HashMap<String, String>();
	public HashMap<UUID, String> playerCommand = new HashMap<UUID, String>();
	boolean vault = false;

	public void onEnable() {
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");

		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			vault = true;
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(this), this);

		this.getCommand("PayForCommand").setExecutor(new CommnandPayForCommand(this));
		this.getCommand("PayForCommand").setTabCompleter(new CommnandPayForCommandTabCompleter());
		
		File fileCommand = new File("plugins/PayForCommand/Commands.yml");
		FileConfiguration ymlFileCommand = YamlConfiguration.loadConfiguration(fileCommand);

		if(!fileCommand.exists()) {
			try {
				ymlFileCommand.save(fileCommand);
				ymlFileCommand.set("Version", UpdateListener.getUpdateDoubleVersion());
				ymlFileCommand.set("TestCommand.Name", "/TestCommand give");
				ArrayList<String> commands = new ArrayList<String>();
				commands.add("/TC give");
				commands.add("/TC i");
				ymlFileCommand.set("TestCommand.Aliases", commands);
				ymlFileCommand.set("TestCommand.Price", 49.50);
				ymlFileCommand.set("TestCommand.Permission", "The.Default.Command.Permission.*");
				ymlFileCommand.save(fileCommand);
			} catch (IOException e1) {
				System.out.println("\u001B[31m[PayForCommand] ERROR: 001 | Can't create the Config.yml. [" + e1.getMessage() +"]\u001B[0m");
			}
		} else {
			double version = ymlFileCommand.getDouble("Version");
			if (version < UpdateListener.getUpdateDoubleVersion()) {
				try {
					ymlFileCommand.set("Version", UpdateListener.getUpdateDoubleVersion());
					ymlFileCommand.save(fileCommand);
				} catch (IOException e) {		
					System.out.println("\u001B[31m[PayForCommand] ERROR: 001 | Can't create the Config.yml. [" + e.getMessage() +"]\u001B[0m");
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
					
					PayForCommand payForCommand = new PayForCommand(commands, ymlFileCommand.getDouble(strg + ".Price"));
					
					if (ymlFileCommand.contains(strg + ".Permission")) {
						payForCommand.setPermission(ymlFileCommand.getString(strg + ".Permission"));
					}
					
					PayForCommandListener.addCommand(payForCommand);
				} catch (Exception e) {
					System.out.println("\u001B[31m[PayForCommand] ERROR: 003 | Faild to load the Configuration fpr the Command \"" + strg + "\". [" + e.getMessage() +"]\u001B[0m");
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
		} else {
			double version = ymlFileMessage.getDouble("Version");
			if (version < UpdateListener.getUpdateDoubleVersion()) {
				try {
					ymlFileMessage.set("Version", UpdateListener.getUpdateDoubleVersion());
					ymlFileMessage.save(fileMessages);
				} catch (IOException e) {		
					System.out.println("\u001B[31m[PayForCommand] ERROR: 002 | Can't create the Messages.yml. [" + e.getMessage() +"]\u001B[0m");
				}
			}
		}

		msg.put("[PayForCommand]", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("[PayForCommand]")));
		msg.put("color.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.1")));
		msg.put("color.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("Color.2")));
		msg.put("msg.1", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.1")));
		msg.put("msg.2", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.2")));
		msg.put("msg.3", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.3")));
		msg.put("msg.4-5", ChatColor.translateAlternateColorCodes('&', "[\"\",{\"text\":\"           \"},{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand yes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.4") + "\"}]}}},{\"text\":\"                      \"},{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/PayForCommand no\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + ymlFileMessage.getString("Message.5") + "\"}]}}}]))"));
		msg.put("msg.6", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.6")));
		msg.put("msg.7", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.7")));
		msg.put("msg.8", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.8")));
		msg.put("msg.9", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.9")));
		msg.put("msg.10", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.10")));
		msg.put("msg.11", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.11")));
		msg.put("msg.12", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.12")));
		msg.put("msg.13", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.13")));
		msg.put("msg.14", ChatColor.translateAlternateColorCodes('&', msg.get("color.1") + ymlFileMessage.getString("Message.14")));
		msg.put("helpTextGui.1", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.1")));
		msg.put("helpTextGui.2", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.2")));
		msg.put("helpTextGui.3", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.3")));
		msg.put("helpTextGui.4", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpTextGui.4")));

		
		HelpPageListener.setPluginNametag(msg.get("[PayForCommand]"));
		
		CommandListener.addCommand(new Command("/pfc help (Page)", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.1"))));
		CommandListener.addCommand(new Command("/pfc info", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.2"))));
		CommandListener.addCommand(new Command("/pfc yes", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.3"))));
		CommandListener.addCommand(new Command("/pfc no", null, ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.4"))));
		CommandListener.addCommand(new Command("/pfc reload", "PayForCommand.Reload", ChatColor.translateAlternateColorCodes('&', ymlFileMessage.getString("HelpText.5"))));
		
		UpdateListener.checkForUpdate(this);
	}

	public void onDisable() {
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");
	}

}
