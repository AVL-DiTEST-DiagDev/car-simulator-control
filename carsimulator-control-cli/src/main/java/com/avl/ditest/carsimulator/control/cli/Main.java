package com.avl.ditest.carsimulator.control.cli;

import java.io.IOException;
import java.util.List;

import com.avl.ditest.carsimulator.control.cli.ICarSimulatorControl.SimulatorInfo;


/**
 * @author Julian Anders
 * valid arguments are:
 * discover - which starts a discovery and returns all the car simulators in your network.
 * execute - also needs two more arguments
 * 	1) hostname - IP address of the client you want to connect to.
 * 	2) command - "kill" and "restart" can be given here. "kill" stops the simulation server on the client. "restart" start the server.
 * transfer - also need two more arguments
 * 	1) hostname - IP address of the client you want to connect to.
 * 	2) path - local path to the file you'd wish to transfer.
 */
public class Main {
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		CarSimulatorControlImpl inter = new CarSimulatorControlImpl();
		if(args.length != 0) {
			if(args[0].equals("discover")) {
				try {
					List<SimulatorInfo> discoveredCarSims = inter.discoverSimulators();
					System.out.println("--- Discovered CarSimulators ---");
					for (SimulatorInfo carSim : discoveredCarSims) {
						String address = carSim.getAddress().toString();
						String version = carSim.getMajorVersion()+"."+carSim.getMinorVersion();
						System.out.println("Address: "+address+ " Version: " + version);
					}	
				}
				catch(Exception e) {
					System.out.print("An error occured while discovering...  error: "+ e);
				}
				
				
			}else if(args[0].equals("execute")) {
				if(args.length > 2) {
					//hostname
					String hostname = args[1];
					//command
					String command = args[2];
					//argument
					String argument = "";
					if (args.length > 3) {
						argument = args[3];
					}
						
					try {
						inter.execCommand(hostname, command, argument);
						System.out.println("Command successfully executed.");
					}
					catch(Exception e) {
						System.out.println("An error occured while executing your command.. error: " + e);
					}
					
				}else {
					System.out.println("Wrong argument format! Please use: execute hostname command");
					System.out.println("For getting the hostname use argument: discover");
				}
				
			}else if(args[0].equals("transfer")) {
				if(args.length == 3) {
					String commandInfo[] = new String[2];
					//hostname
					String hostname = args[1];
					//path
					String path = args[2];
					try {
						inter.upload(hostname, path);
						System.out.println("File successfully transfered!");
					}
					catch(Exception e) {
						System.out.println("An error occured while transfering your file.. error: " + e);
					}
					
					String command = "restart";
					try {
						inter.execCommand(hostname, command, null); 
						System.out.println("Server successfully restarted!");
					}
					catch(Exception e) {
						System.out.println("An error occured while restarting the server.. error: " + e);
					}
				}else {
					System.out.println("Wrong argument format! Please use: transfer hostname path. ");
					System.out.println("For getting the hostname use argument: discover.");
				}
			}else {
				System.out.println("Wrong argument format! Please use one of the arguments: discover || execute || transfer");
			}
		}
		else {
			System.out.println("Wrong argument format! Please use one of the arguments: discover || execute || transfer");
		}
		
	}
}
