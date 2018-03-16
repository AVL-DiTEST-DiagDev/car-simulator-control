package com.avl.ditest.carsimulator;

import java.io.IOException;
import java.util.List;

import com.avl.ditest.carsimulator.discovery.CarSimDiscovery;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CarSimDiscovery carsimDiscovery = new CarSimDiscovery();
		System.out.println("Discovering CarSimulators ...");
		List<String> discoveredCarSims = carsimDiscovery.discoverSync();
		System.out.println("--- Discovered CarSimulators ---");
		for (String carSim : discoveredCarSims) {
			System.out.println(carSim);
		}
	}

}
