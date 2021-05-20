package com.avl.ditest.carsimulator.control.cli;

public class CommandGenerator {
	public String generateCommand(String command, String argument) {
		ConnectionInfo info = new ConnectionInfo();
		String generatedCommand = null;
		String filepath = info.getFilePath();
		if (command.equals("kill")) {
			generatedCommand = "sudo systemctl stop carsimulator@can0; sudo systemctl stop carsimulator@can1";
		}else if(command.equals("start")) {
			generatedCommand = "sudo systemctl start carsimulator@can0; sudo systemctl start carsimulator@can1";
		}else if(command.equals("restart")) {
			generatedCommand = "sudo systemctl restart carsimulator@can0; sudo systemctl restart carsimulator@can1";
		}else if (command.equals("delete")){
			if(argument != null) {
				if(argument.equals("all")) {
					generatedCommand = "cd " + filepath +  "&& rm -R ./*";
				}else {
					generatedCommand = "cd " + filepath +  "&& rm " + argument;
				}
			}
			else {
				System.out.println("API error: please use an argument. filename or all");
			}
		}else if (command.equals("list")) {
			generatedCommand = "cd " + filepath + "&& ls >&2";
		}else {
			System.out.println("API error: please use one of the commands: kill, start, restart, list or delete ... Given: " + command);
		}
		return generatedCommand;
	}
}
