package com.avl.ditest.carsimulator.control.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.avl.ditest.carsimulator.control.cli.CarSimDiscovery;
import com.avl.ditest.carsimulator.control.cli.ExecuteCommand;
import com.avl.ditest.carsimulator.control.cli.ScpTo;
import com.avl.ditest.carsimulator.control.cli.ICarSimulatorControl.SimulatorInfo;

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
			ScpTo transferer = new ScpTo();
			transferer.transfer(hostname, path);
		} else {
			System.out.println("API error: please use set the filepath as argument ... ");
		}

	}

	public void execCommand(String hostname, String command) {
		// arg[0] = IP of the raspberry
		// arg[1] = kill || restart
		if (command == "kill" || command == "restart") {
			ExecuteCommand executer = new ExecuteCommand();
			executer.exec(hostname, command);
		} else {
			System.out.println("API error: please use one of the commands: kill or restart ... ");
		}

	}
}
