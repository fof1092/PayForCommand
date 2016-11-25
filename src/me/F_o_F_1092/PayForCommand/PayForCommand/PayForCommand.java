package me.F_o_F_1092.PayForCommand.PayForCommand;

public class PayForCommand {

	String command;
	double price;
	
	public PayForCommand(String command, double price) {
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
