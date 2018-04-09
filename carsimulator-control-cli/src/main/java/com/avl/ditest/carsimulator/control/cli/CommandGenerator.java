package com.avl.ditest.carsimulator.control.cli;

public class CommandGenerator {
	public String generateCommand(String command, String argument) {
		ConnectionInfo info = new ConnectionInfo();
		String generatedCommand = null;
		String filepath = info.getFilePath();
		if (command.equals("kill")) {
			generatedCommand = "sudo pkill amos-ss17-proj4";
		}else if(command.equals("start")) {
			generatedCommand = "cd /home/pi/Desktop/python/ && python startServer.py";
		}else if(command.equals("restart")) {
			generatedCommand = "sudo pkill amos-ss17-proj4 && cd /home/pi/Desktop/python/ && python startServer.py";
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
		}else {
			System.out.println("API error: please use one of the commands: kill, start, restart or delete ... Given: " + command);
		}
		return generatedCommand;
	}
}
