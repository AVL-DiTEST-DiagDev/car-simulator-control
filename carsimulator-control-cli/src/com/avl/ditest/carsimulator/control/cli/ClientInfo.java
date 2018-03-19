package com.avl.ditest.carsimulator.control.cli;

public interface ClientInfo {
	String PASSWORD = "raspberry";
	String USERNAME = "pi";
	String FILEPATH = "~/Desktop/";
}

class ConnectionInfo implements ClientInfo {
	
	private String username;

	public String getUsername() {
		username = USERNAME;
		return username;
	}
}

class UploadInfo implements ClientInfo {
	private String filepath;

	public String getFilePath() {
		filepath = FILEPATH;
		return filepath;
	}
}