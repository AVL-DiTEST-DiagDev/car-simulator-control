/*
 * @author: Julian Anders
 * Company: AVL DiTEST
 * Date: 19.02.2018
 * Version: 0.0.1
 * Based on JSch Example "Exec"*/
/**
 * Set IP address of the client you want to connect to as first argument
 * Put your command as second argument
 * commands can be:
 * kill = kills the amos server
 * restart = restarts the amos server
 * Check ClientInfo for the connection parameters
 */
package com.avl.ditest.carsimulator.control.cli;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import com.avl.ditest.carsimulator.control.cli.*;


public class ExecuteCommand {
	public void exec(String hostname, String command, String argument) {
		try {
			JSch jsch = new JSch();

			String execCommand[] = new String[2];
			if (command != null || hostname != null) {
				execCommand[0] = command;
			} else {
				execCommand[0] = JOptionPane.showInputDialog("Enter hostname", "command");
			}
			if(argument != null) {
				execCommand[1] = argument;
			}

			ConnectionInfo ci = new ConnectionInfo();
			Session session = jsch.getSession(ci.getUsername(), hostname, 22);
			// checks if command is valid and return the correct terminal line
			checkCommand(execCommand);

			// username and password will be given via UserInfo interface.
			UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.connect();

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(execCommand[1]);

			// channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			// InputStream in=channel.getInputStream();

			channel.connect();
			JOptionPane successDialog = new JOptionPane();
			successDialog.showMessageDialog(null, "Done", "Success", JOptionPane.INFORMATION_MESSAGE);
			/*
			 * byte[] tmp=new byte[1024]; while(true){ while(in.available()>0){ int
			 * i=in.read(tmp, 0, 1024); if(i<0)break; System.out.print(new String(tmp, 0,
			 * i)); } if(channel.isClosed()){ if(in.available()>0) continue;
			 * System.out.println("exit-status: "+channel.getExitStatus()); break; }
			 * try{Thread.sleep(1000);}catch(Exception ee){} }
			 */
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static String[] checkCommand(String[] command) {
		ConnectionInfo ci = new ConnectionInfo();
		String filepath = ci.getFilePath();
		if (command[0].equals("kill")) {
			command[1] = "sudo pkill amos-ss17-proj4";
		} else if (command[0].equals("restart")) {
			command[1] = "cd /home/pi/Desktop/python/ && python startServer.py";

		}else if(command[0].equals("delete")) {
			if(command[1].equals("all")){
				command[1] = "cd " + filepath +  "&& rm -R ./*";
			}else if(command[1] != null ) {
				String file = command[1];
				command[1] ="cd " + filepath +  "&& rm " + command[1];
			}
		}
		else {
			System.out.println("error! Command " + command[0] + " is unknown.... Retry with another command.");
		}

		return command;

	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive, ClientInfo {
		private String password;

		public String getPassword() {
			password = PASSWORD;
			return password;
		}

		public boolean promptYesNo(String str) {
			int foo = 0;
			return foo == 0;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		private Container panel;

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx = 0;
			panel.add(new JLabel(instruction), gbc);
			gbc.gridy++;

			gbc.gridwidth = GridBagConstraints.RELATIVE;

			JTextField[] texts = new JTextField[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridx = 0;
				gbc.weightx = 1;
				panel.add(new JLabel(prompt[i]), gbc);

				gbc.gridx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weighty = 1;
				if (echo[i]) {
					texts[i] = new JTextField(20);
				} else {
					texts[i] = new JPasswordField(20);
				}
				panel.add(texts[i], gbc);
				gbc.gridy++;
			}

			if (JOptionPane.showConfirmDialog(null, panel, destination + ": " + name, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
				String[] response = new String[prompt.length];
				for (int i = 0; i < prompt.length; i++) {
					response[i] = texts[i].getText();
				}
				return response;
			} else {
				return null; // cancel
			}
		}
	}
}
