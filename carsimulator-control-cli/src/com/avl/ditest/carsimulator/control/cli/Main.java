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
				List<SimulatorInfo> discoveredCarSims = inter.discoverSimulators();
				System.out.println("--- Discovered CarSimulators ---");
				for (SimulatorInfo carSim : discoveredCarSims) {
					System.out.println(carSim);
				}
				
			}else if(args[0].equals("execute")) {
				if(args.length != 3) {
					//hostname
					String hostname = args[1];
					//command
					String command = args[2];
					inter.execCommand(hostname, command);
				}else {
					System.out.print("Wrong argument format! Please use: execute hostname command");
					System.out.print("For getting the hostname use argument: discover");
				}
				
			}else if(args[0].equals("transfer")) {
				if(args.length == 3) {
					String commandInfo[] = new String[2];
					//hostname
					String hostname = args[1];
					//path
					String path = args[2];
					inter.upload(hostname, path);
					String command = "kill";
					inter.execCommand(hostname, command);
					command = "restart";
					inter.execCommand(hostname, command); 
				}else {
					System.out.println("Wrong argument format! Please use: transfer hostname path");
					System.out.println("For getting the hostname use argument: discover");
				}
			}else {
				System.out.println("Wrong argument format! Please use one of the arguments: discover || execute || transfer");
				System.out.println("Given: " + args[0]);
			}
		}
		else {
			System.out.println("Wrong argument format! Please use one of the arguments: discover || execute || transfer");
			System.out.println("Given: " + args);
		}
		
	}
}
