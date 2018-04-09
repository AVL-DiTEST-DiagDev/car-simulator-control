package com.avl.ditest.carsimulator.control.cli;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Julian Anders
 * Implementation of the ICarSimulatorControl.java 
 */
public class CarSimulatorControlImpl implements ICarSimulatorControl {
	
	public ArrayList<SimulatorInfo> discoverSimulators() throws IOException {
		//CarSimDiscovery.discoverSync();
		CarSimDiscovery carsimDiscovery = new CarSimDiscovery();
		ArrayList<SimulatorInfo> discoveredCarSims = carsimDiscovery.discoverSync();
		//list of all Car Sims in the format: Host: /127.0.0.1 Version:0.1
		return discoveredCarSims;
	}

	public void upload(String hostname, String path) {
		// arg[0] = IP of the raspberry
		// arg[1] = filePath (C:\Users\)
		if (hostname != null && path != null) {
			ScpTo transferer = new ScpTo();
			transferer.transfer(hostname, path);
		} else {
			System.out.println("API error: please set the hostname and filepath as argument ... ");
		}

	}

	public void execCommand(String hostname, String command, String argument) {
		// hostname = IP of the raspberry
		// command = kill || restart || restart || delete
		// argument = null || all || filename
		
		CommandGenerator comgen = new CommandGenerator();
		ExecuteCommand executer = new ExecuteCommand();
		
		String generatedCommand = comgen.generateCommand(command, argument);
		executer.exec(hostname, generatedCommand);

	}
}
