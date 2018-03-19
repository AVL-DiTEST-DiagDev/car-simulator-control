package com.avl.ditest.carsimulator.control.cli;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ICarSimulatorControl.java
 * Interface class that has the following methods.
 * 
 * @author Julian Anders
 * @since 03-19-2018
 */
public interface ICarSimulatorControl {
	
	/**
	 * @return ArrayList of all Car-simulators found in the network. datatype: SimulatorInfo 
	 * @throws IOException
	 */
	public ArrayList<SimulatorInfo> discoverSimulators() throws IOException;
	
	/**
	 * @param hostname
	 * @param path
	 * 
	 * hostname shall be the IP address of the car simulator, which can be found with discoverSimulators().
	 * path shall be set to the path, where the file, you want to transfer, is located.
	 */
	public void upload(String hostname, String path);
	
	/**
	 * @param hostname
	 * @param command
	 * 
	 * hostname shall be the IP address of the car simulator, which can be found with discoverSimulators().
	 * valid commands are: "kill" or "restart" - whether you'd like to kill or start the server.
	 */
	public void execCommand(String hostname,String command);
	
	public class SimulatorInfo {
		private InetAddress address;
		private int majorVersion;
		private int minorVersion;
		
		public InetAddress getAddress() {
			return address;
		}
		public void setAddress(InetAddress address) {
			this.address = address;
		}
		public int getMajorVersion() {
			return majorVersion;
		}
		public void setMajorVersion(int majorVersion) {
			this.majorVersion = majorVersion;
		}
		public int getMinorVersion() {
			return minorVersion;
		}
		public void setMinorVersion(int minorVersion) {
			this.minorVersion = minorVersion;
		}
		
	}

}

