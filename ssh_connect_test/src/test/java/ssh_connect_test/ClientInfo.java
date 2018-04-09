package ssh_connect_test;

public interface ClientInfo {
	static final String PASSWORD = "raspberry";
	static final String HOSTNAME = "169.254.236.214";
	static final String USERNAME = "pi";
	static final String FILEPATH = "~/Desktop/";
}

class ConnectionInfo implements ClientInfo {
	private String hostname;
	private String username;

	public String getHostname() {
		hostname = HOSTNAME;
		return hostname;
	}

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