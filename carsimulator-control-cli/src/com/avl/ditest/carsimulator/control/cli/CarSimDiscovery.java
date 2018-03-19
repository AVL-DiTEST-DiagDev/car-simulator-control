package com.avl.ditest.carsimulator.control.cli;

import java.util.*;

import com.avl.ditest.carsimulator.control.cli.ICarSimulatorControl.SimulatorInfo;

import java.net.*;
import java.io.*;
import java.nio.*;

public class CarSimDiscovery {
	public static final int DISCOVERY_PORT = 15123;
	public static final int DISCOVERY_TIME_MS = 3000;
	public static final byte[] DISCOVERY_ID = new byte[] {0x41,0x56,0x4c,0x44,0x69,0x54,0x45,0x53,0x54,0x2d,0x43,0x61,0x72,0x53,0x69,0x6d,0x2d,0x44,0x69,0x73,0x63,0x6f,0x76,0x65,0x72,0x79};
	public static final byte[] DISCOVERY_VERSION = new byte[] {0x00,0x00};
	public static final byte[] SUPPORTED_VERSION = new byte[] {0x00,0x01};
	
	public ArrayList<SimulatorInfo> discoverSync() throws IOException {
		ArrayList<SimulatorInfo> discoveredCarSimulators = new ArrayList<SimulatorInfo>();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( DISCOVERY_ID );
		outputStream.write( DISCOVERY_VERSION );
		outputStream.write( ~DISCOVERY_VERSION[0] & 0xFF );
		outputStream.write( ~DISCOVERY_VERSION[1] & 0xFF );
 
		//InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
		//InetAddress broadcastAddress = InetAddress.getByName("10.2.10.255");
		DatagramSocket clientSocket = new DatagramSocket();
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
        	for( InterfaceAddress ifaddr : netint.getInterfaceAddresses() ) {
        		if(ifaddr != null && ifaddr.getBroadcast() != null) {
        			DatagramPacket sendPacket = new DatagramPacket(outputStream.toByteArray(), outputStream.size(), ifaddr.getBroadcast(), DISCOVERY_PORT);
        			clientSocket.send(sendPacket);
        		}
        	}
        }

		byte[] receiveData = new byte[1500];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		ByteBuffer discoveryID = ByteBuffer.wrap(DISCOVERY_ID);

		clientSocket.setSoTimeout(DISCOVERY_TIME_MS); // by now this means, each new call to receive waits again DISCOVERY_TIME_MS
		try {
			while(true) {
				clientSocket.receive(receivePacket);
				System.out.println("Package received");
				// check if this is a response from the car simulator
				if(receivePacket.getLength() == DISCOVERY_ID.length + DISCOVERY_VERSION.length*2) {
					ByteBuffer packetID = ByteBuffer.wrap(receivePacket.getData(),0,DISCOVERY_ID.length);
					if(discoveryID.equals(packetID)) {
						System.out.println("- Package is from Car Simulator");
						// verify and extract API version
						ByteBuffer apiVersion = ByteBuffer.wrap(receivePacket.getData(), DISCOVERY_ID.length, DISCOVERY_VERSION.length);
						ByteBuffer apiVersionInverse = ByteBuffer.wrap(receivePacket.getData(), DISCOVERY_ID.length + DISCOVERY_VERSION.length, DISCOVERY_VERSION.length);
						// TODO verify API version
						int versionMajor = receivePacket.getData()[DISCOVERY_ID.length];
						int versionMinor = receivePacket.getData()[DISCOVERY_ID.length + 1];
						SimulatorInfo info = new SimulatorInfo();
						info.setAddress(receivePacket.getAddress());
						info.setMajorVersion(versionMajor);
						info.setMinorVersion(versionMinor);
						InetAddress address = receivePacket.getAddress();
						discoveredCarSimulators.add(info);
					}
				}
			}
		} catch(InterruptedIOException e) {
			// this happens if the timeout set with setSoTimeout expired
		}
		
	    clientSocket.close();
	    
		return discoveredCarSimulators;
	}
}
