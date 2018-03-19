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

	public void upload(String hostname,String path) {
		// arg[0] = IP of the raspberry
		// arg[1] = filePath (C:\Users\)
		if (hostname != null && path != null) {
			ScpTo.transfer(hostname, path);
		} else {
			System.out.println("API error: please use set the filepath as argument ... ");
		}

	}

	public void execCommand(String hostname, String command) {
		// arg[0] = IP of the raspberry
		// arg[1] = kill || restart
		if (command == "kill" || command == "restart") {
			ExecuteCommand.exec(hostname, command);
		} else {
			System.out.println("API error: please use one of the commands: kill or restart ... ");
		}

	}
}
