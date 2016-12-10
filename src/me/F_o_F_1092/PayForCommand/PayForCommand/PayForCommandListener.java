package me.F_o_F_1092.PayForCommand.PayForCommand;

import java.util.ArrayList;

public class PayForCommandListener {

	static ArrayList<PayForCommand> payForCommands = new ArrayList<PayForCommand>();
	
	public static void addCommand(PayForCommand payForCommand) {
		payForCommands.add(payForCommand);
	}
	
	public static void clearCommands() {
		payForCommands.clear();
	}
	
	public static boolean isCommand(String commandString) {
		if (!payForCommands.isEmpty()) {
			for (PayForCommand payForCommands : payForCommands) {
				for (String command : payForCommands.getCommands()) {
					if (commandString.toLowerCase().startsWith(command.toLowerCase())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static PayForCommand getCommand(String commandString) {
		for (PayForCommand payForCommands : payForCommands) {
			for (String command : payForCommands.getCommands()) {
				if (commandString.toLowerCase().startsWith(command.toLowerCase())) {
					return payForCommands;
				}
			}
		}
		return null;
	}
}
