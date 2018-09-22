package me.F_o_F_1092.PayForCommand.PayForCommand;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PayForCommand {

	ArrayList<String> commands;
	Double price;
	Material itemMaterial;
	Integer itemNumber;
	Short itemSubID;
	String permissions;
	
	public PayForCommand(ArrayList<String> commands) {
		this.commands = commands;
	}
	
	public void setPermission(String permission) {
		this.permissions = permission;
	}
	
	public ArrayList<String> getCommands() {
		return this.commands;
	}
	
	public void setItemPrice(Material itemMaterial, Integer itemNumber, Short itemSubID) {
		this.itemMaterial = itemMaterial;
		this.itemNumber = itemNumber;
		this.itemSubID = itemSubID;
	}
	
	public boolean hasItemPrice() {
		return this.itemMaterial != null;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getItemPrice() {
		return new ItemStack(this.itemMaterial, this.itemNumber, this.itemSubID);
	}
	
	public void setMoneyPrice(double price) {
		this.price = price;
	}
	
	public boolean hasMoneyPrice() {
		return this.price != null;
	}
	
	public double getMoneyPrice() {
		return this.price;
	}
	
	public String getPermission() {
		return this.permissions;
	}
	
}
