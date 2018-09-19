/*
 * @author: Julian Anders
 * Company: AVL DiTEST
 * Date: 19.02.2018
 * Version: 0.0.1
 * Based on JSch Example "ScpTo"*/
/**
 * run config with arguments like:
 * //C:\Users\Your username\Desktop\file.txt pi@raspberryIpHere:~/PathOnRaspberry/
 */
package com.avl.ditest.carsimulator.control.cli;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class ScpTo {
	public void transfer(final String hostname, final String path) {
		if (hostname == null || path == null) {

			System.err.println("usage: java ScpTo pathToFile ");
			System.exit(-1);
		}

		FileInputStream fis = null;

		try {

			final String lfile = path;
			final boolean exi = checkExists(lfile);
			if (exi == true) {

				final JSch jsch = new JSch();

				final ConnectionInfo ci = new ConnectionInfo();
				final Session session = jsch.getSession(ci.getUsername(), hostname, 22);

				// username and password will be given via UserInfo interface.
				final UserInfo ui = new MyUserInfo();
				session.setUserInfo(ui);
				session.connect();

				final boolean ptimestamp = true;

				// exec 'scp -t rfile' remotely
				String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + ci.getFilePath();
				final Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);

				// get I/O streams for remote scp
				final OutputStream out = channel.getOutputStream();
				final InputStream in = channel.getInputStream();

				channel.connect();

				if (checkAck(in) != 0) {
					System.exit(0);
				}

				final File _lfile = new File(lfile);

				if (ptimestamp) {
					command = "T" + _lfile.lastModified() / 1000 + " 0";
					// The access time should be sent hee,
					// but it is not accessible with JavaAPI ;-<
					command += " " + _lfile.lastModified() / 1000 + " 0\n";
					out.write(command.getBytes());
					out.flush();
					if (checkAck(in) != 0) {
						System.exit(0);
					}
				}

				// send "C0644 filesize filename", where filename should not include '/'
				final long filesize = _lfile.length();
				command = "C0644 " + filesize + " ";
				if (lfile.lastIndexOf('/') > 0) {
					command += lfile.substring(lfile.lastIndexOf('/') + 1);
				} else if (lfile.lastIndexOf('\\') > 0) {
					command += lfile.substring(lfile.lastIndexOf('\\') + 1);
				} else {
					command += lfile;
				}
				command += "\n";
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
				// send a content of lfile
				fis = new FileInputStream(lfile);
				final byte[] buf = new byte[1024];
				while (true) {
					final int len = fis.read(buf, 0, buf.length);
					if (len <= 0) {
						break;
					}
					out.write(buf, 0, len); // out.flush();
				}
				fis.close();
				fis = null;
				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
				out.close();

				channel.disconnect();
				session.disconnect();

			} else {
				// if file doesn't exist
				System.out.println("The file you wanted to transfer does not exist. Please double check the path.");
			}
		} catch (final Exception e) {
			// Error code goes here..
			System.out.println(e);
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (final Exception ee) {
			}
		}

	}

	static int checkAck(final InputStream in) throws IOException {
		final int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0) {
			return b;
		}
		if (b == -1) {
			return b;
		}

		if (b == 1 || b == 2) {
			final StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

	// checks if the file given in the argument exists and return bool
	public static boolean checkExists(final String directory) {
		final boolean check = new File(directory).exists();
		return check;
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive, ClientInfo {
		private String password;

		@Override
		public String getPassword() {
			password = PASSWORD;
			return password;
		}

		@Override
		public boolean promptYesNo(final String str) {
			final int foo = 0;
			return foo == 0;
		}

		@Override
		public String getPassphrase() {
			return null;
		}

		@Override
		public boolean promptPassphrase(final String message) {
			return true;
		}

		@Override
		public boolean promptPassword(final String message) {
			return true;
		}

		@Override
		public void showMessage(final String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0);
		private Container panel;

		@Override
		public String[] promptKeyboardInteractive(final String destination, final String name, final String instruction, final String[] prompt,
				final boolean[] echo) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx = 0;
			panel.add(new JLabel(instruction), gbc);
			gbc.gridy++;

			gbc.gridwidth = GridBagConstraints.RELATIVE;

			final JTextField[] texts = new JTextField[prompt.length];
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
				final String[] response = new String[prompt.length];
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