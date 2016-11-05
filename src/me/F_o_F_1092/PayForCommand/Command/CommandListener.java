package me.F_o_F_1092.PayForCommand.Command;

import java.util.ArrayList;

public class CommandListener {

	static ArrayList<Command> commands = new ArrayList<Command>();
	
	public static void addCommand(Command command) {
		commands.add(command);
	}
	
	public static void clearCommands() {
		commands.clear();
	}
	
	public static boolean isCommand(String commandString) {
		if (!commands.isEmpty()) {
			for (Command command : commands) {
				if (commandString.toLowerCase().startsWith(command.getCommand().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Command getCommand(String commandString) {
		for (Command command : commands) {
			if (commandString.toLowerCase().startsWith(command.getCommand().toLowerCase())) {
				return command;
			}
		}
		return null;
	}
}
