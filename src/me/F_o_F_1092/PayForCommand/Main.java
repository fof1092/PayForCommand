package me.F_o_F_1092.PayForCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.F_o_F_1092.PayForCommand.Command.Command;
import me.F_o_F_1092.PayForCommand.Command.CommandListener;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpMessage;
import me.F_o_F_1092.PayForCommand.PluginManager.HelpPageListener;

public class Main extends JavaPlugin {

	public HashMap<String, String> msg = new HashMap<String, String>();
	public HashMap<UUID, String> playerCommand = new HashMap<UUID, String>();
	boolean vault = false;
	boolean updateAvailable = false;

	public void onEnable() {
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");

		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			vault = true;
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(this), this);

		this.getCommand("PayForCommand").setExecutor(new CommnandPayForCommand(this));

		File fileCommand = new File("plugins/PayForCommand/Commands.yml");
		FileConfiguration ymlFileCommand = YamlConfiguration.loadConfiguration(fileCommand);

		if(!fileCommand.exists()) {
			try {
				ymlFileCommand.save(fileCommand);
				ymlFileCommand.set("Version", 1.0);
				ymlFileCommand.set("TestCommand.Name", "/TestCommand give");
				ymlFileCommand.set("TestCommand.Price", 49.50);
				ymlFileCommand.save(fileCommand);
			} catch (IOException e1) {
				System.out.println("\u001B[31m[PayForCommand] ERROR: 009 | Can't create the Config.yml. [" + e1.getMessage() +"]\u001B[0m");
			}
		}
		
		for (String strg : ymlFileCommand.getKeys(false)) {
			if (!strg.equals("Version")) {
				CommandListener.addCommand(new Command(ymlFileCommand.getString(strg + ".Name"), ymlFileCommand.getDouble(strg + ".Price")));
			}
		}
		
		
		File fileMessages = new File("plugins/PayForCommand/Messages.yml");
		FileConfiguration ymlFileMessage = YamlConfiguration.loadConfiguration(fileMessages);

		if(!fileMessages.exists()) {
			try {
				ymlFileMessage.save(fileMessages);
				ymlFileMessage.set("Version", 1.0);
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
				System.out.println("\u001B[31m[PayForCommand] ERROR: 012 | Can't create the Messages.yml. [" + e1.getMessage() +"]\u001B[0m");
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
		msg.put("helpText.1", ChatColor.translateAlternateColorCodes('&', msg.get("color.2") + ymlFileMessage.getString("HelpText.1")));
		msg.put("helpText.2", ChatColor.translateAlternateColorCodes('&', msg.get("color.2") + ymlFileMessage.getString("HelpText.2")));
		msg.put("helpText.3", ChatColor.translateAlternateColorCodes('&', msg.get("color.2") + ymlFileMessage.getString("HelpText.3")));
		msg.put("helpText.4", ChatColor.translateAlternateColorCodes('&', msg.get("color.2") + ymlFileMessage.getString("HelpText.4")));
		msg.put("helpText.5", ChatColor.translateAlternateColorCodes('&', msg.get("color.2") + ymlFileMessage.getString("HelpText.5")));

		HelpPageListener.setPluginNametag(msg.get("[PayForCommand]"));
		
		HelpPageListener.addHelpMessage(new HelpMessage(null, msg.get("helpText.1"), "/pfc help (Page)"));
		HelpPageListener.addHelpMessage(new HelpMessage(null, msg.get("helpText.2"), "/pfc info"));
		HelpPageListener.addHelpMessage(new HelpMessage(null, msg.get("helpText.3"), "/pfc yes"));
		HelpPageListener.addHelpMessage(new HelpMessage(null, msg.get("helpText.4"), "/pfc no"));
		HelpPageListener.addHelpMessage(new HelpMessage("PayForCommand.Reload", msg.get("helpText.5"), "/pfc reload"));

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				try {
					TrustManager[] trustAllCerts = new TrustManager[] {
							new X509TrustManager() {
								public java.security.cert.X509Certificate[] getAcceptedIssuers() {
									return null;
								}
								
								public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

								public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
							}
					};
					
					SSLContext sslC = SSLContext.getInstance("SSL");
					sslC.init(null, trustAllCerts, new java.security.SecureRandom());
					
					HttpsURLConnection.setDefaultSSLSocketFactory(sslC.getSocketFactory());

					HostnameVerifier allHostsValid = new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					};
						    
					HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
						 
					URL url = new URL("https://fof1092.de/Plugins/PFC/version.txt");
					URLConnection connection = url.openConnection();
					final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
					
					if (!reader.readLine().equals("Version: 1.0")) {
						System.out.println("[PayForCommand] A new update is available.");
						updateAvailable = true;
					}
					
				} catch ( IOException e) {
					System.out.println("\u001B[31m[PayForCommand] Couldn't check for updates. [" + e.getMessage() +"]\u001B[0m");
				} catch (NoSuchAlgorithmException e) {
					System.out.println("\u001B[31m[PayForCommand] Couldn't check for updates. [" + e.getMessage() +"]\u001B[0m");
				} catch (KeyManagementException e) {
					System.out.println("\u001B[31m[PayForCommand] Couldn't check for updates. [" + e.getMessage() +"]\u001B[0m");
				}
			}
		}, 0L);
	}

	public void onDisable() {
		System.out.println("[PayForCommand] a Plugin by F_o_F_1092");
	}

}
