package ssh_connect_test;

public interface MainInterface {
	public void upload(String arg);

	public void execCommand(String arg);

	public void openUI();

}

class Interface implements MainInterface {
	public void openUI() {
		MainGui.main(null);
	}

	public void upload(String arg) {
		// arg = filePath (C:\Users\)
		if (arg != null) {
			ScpTo.main(arg);
		} else {
			System.out.println("API error: please use set the filepath as argument ... ");
		}

	}

	public void execCommand(String arg) {
		// arg = kill || restart
		if (arg == "kill" || arg == "restart") {
			ExecuteCommand.main(arg);
		} else {
			System.out.println("API error: please use one of the commands: kill or restart ... ");
		}

	}
}