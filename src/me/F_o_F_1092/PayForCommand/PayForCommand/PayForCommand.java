package me.F_o_F_1092.PayForCommand.PayForCommand;

import java.util.ArrayList;

public class PayForCommand {

	ArrayList<String> commands;
	double price;
	String permissions;
	
	public PayForCommand(ArrayList<String> commands, double price) {
		this.commands = commands;
		this.price = price;
	}
	
	public void setPermission(String permission) {
		this.permissions = permission;
	}
	
	public ArrayList<String> getCommands() {
		return this.commands;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public String getPermission() {
		return this.permissions;
	}
	
}
