package me.F_o_F_1092.PayForCommand.Command;

public class Command {

	String command;
	double price;
	
	public Command(String command, double price) {
		this.command = command;
		this.price = price;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public double getPrice() {
		return this.price;
	}
	
}
