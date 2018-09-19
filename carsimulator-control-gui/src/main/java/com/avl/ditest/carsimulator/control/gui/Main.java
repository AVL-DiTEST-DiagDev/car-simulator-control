package com.avl.ditest.carsimulator.control.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.avl.ditest.carsimulator.control.gui.CarSimulatorControlImpl;
import com.avl.ditest.carsimulator.control.cli.ICarSimulatorControl.SimulatorInfo;

/**
 *
 * This programm opens a window with two buttons. Each calls a seperate gui for
 * either exec or ScpTo
 *
 * Author: Julian Anders Last modified: 27 Feb 2018
 */

public class Main {

	// the label used to display selected dir/file.
	Label label;

	Button buttonSelectDir;
	Button buttonSelectFile;
	ArrayList<SimulatorInfo> discoveredCarSims = new ArrayList();
	String selectedDir;
	String fileFilterPath = "C:\\Users\\";
	// defined in buttonUseDiscovered
	String hostname;
	public Main(Display display) {

		initUI(display);
	}

	private void initUI(Display display) {
		CarSimulatorControlImpl inter = new CarSimulatorControlImpl();
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
		RowLayout layout = new RowLayout();
		layout.marginLeft = 30;
		layout.marginTop = 30;
		layout.marginBottom = 150;
		layout.marginRight = 150;

		shell.setLayout(layout);

		// Create a group to contain 2 radio (Male & Female)
		Group containerGroup = new Group(shell, SWT.NONE | SWT.WRAP);
		containerGroup.setLayout(new RowLayout(SWT.VERTICAL));
		Group discoverGroup = new Group(containerGroup, SWT.NONE | SWT.WRAP);
		discoverGroup.setLayout(new RowLayout(SWT.VERTICAL));
		Group commandGroup = new Group(containerGroup, SWT.NONE | SWT.WRAP);
		commandGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		commandGroup.setText("Select your command:");
		Group uploadContainerGroup = new Group(containerGroup, SWT.NONE | SWT.WRAP);
		uploadContainerGroup.setLayout(new RowLayout(SWT.VERTICAL));
		uploadContainerGroup.setText("Upload A file:");
		Group uploadBtnGroup = new Group(uploadContainerGroup, SWT.NONE);
		uploadBtnGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		SelectionListener selectionListener = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {

			}

		};
		
		final List carsim = new List(discoverGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		
		carsim.addSelectionListener(new SelectionListener() {

		      public void widgetSelected(SelectionEvent event) {
		        int selections = carsim.getSelectionIndex();
		        String outText = carsim.getItem(selections);
		      }

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		    });
		
		Button buttonDiscovery = new Button(discoverGroup, SWT.PUSH);
		buttonDiscovery.setText("Discover Car Simulators");
		//SimulatorInfo carSim = new SimulatorInfo();
		
		buttonDiscovery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					discoveredCarSims = inter.discoverSimulators();
					for (SimulatorInfo carSim : discoveredCarSims) {
						String info = "Host: "+carSim.getAddress().toString() + " Version: " + carSim.getMajorVersion()+ "."+carSim.getMinorVersion();
						carsim.add(info);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
					
			}
		});
		Button buttonUseDiscovered = new Button(discoverGroup, SWT.PUSH);
		buttonUseDiscovered.setText("Use Selected carSim");
		buttonUseDiscovered.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//gets selected entry from list
				int selected = carsim.getSelectionIndex();
				//substrings the / in the beginning of the address
				hostname = discoveredCarSims.get(selected).getAddress().toString().substring(1);
				
					
			}
		});
		
		
		
		Button buttonKill = new Button(commandGroup, SWT.RADIO);
		buttonKill.setText("shutdown server");
		buttonKill.addSelectionListener(selectionListener);
		
		Button buttonRestart = new Button(commandGroup, SWT.RADIO);
		buttonRestart.setText("start server");
		buttonRestart.addSelectionListener(selectionListener);

		Button runBtn = new Button(commandGroup, SWT.PUSH);
		runBtn.setText("Run");
		runBtn.setLayoutData(new RowData(80, 30));
		runBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean restartState = buttonRestart.getSelection();
				boolean killState = buttonKill.getSelection();
				if (restartState == true) {
					String command = "restart";
					inter.execCommand(hostname, command);
				} else if (killState == true) {
					String command = "kill";
					inter.execCommand(hostname, command);
				}
			}

		});

		Label pathLabel = new Label(uploadContainerGroup, SWT.BORDER | SWT.WRAP);
		pathLabel.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		Button buttonSelectFile = new Button(uploadBtnGroup, SWT.PUSH);
		buttonSelectFile.setText("Select a file file");
		buttonSelectFile.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SINGLE);
				fileDialog.setFilterPath(fileFilterPath);
				fileDialog.setFilterExtensions(new String[] { "*.lua", "*.txt", "*.*" });
				fileDialog.setFilterNames(new String[] { "LUA Files", "Text Files", "Any" });
				String firstFile = fileDialog.open();
				if (firstFile != null) {
					fileFilterPath = fileDialog.getFilterPath();
					String[] selectedFiles = fileDialog.getFileNames();
					StringBuffer sb = new StringBuffer(fileDialog.getFilterPath() + "\\");
					for (int i = 0; i < selectedFiles.length; i++) {
						sb.append(selectedFiles[i]);
					}
					pathLabel.setText(sb.toString());
				}
			}
		});
		
		Button uploadBtn = new Button(uploadBtnGroup, SWT.PUSH);
		uploadBtn.setText("Uplaod");
		uploadBtn.setLayoutData(new RowData(80, 30));
		uploadBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = pathLabel.getText();
				
				if (path != "") {
					
					inter.upload(hostname, path);
					String command ="kill";
					inter.execCommand(hostname, command);
					command ="restart";
					inter.execCommand(hostname, command);
				} else {
					System.out.println("no path given");
				}
			}
		});
		
		pathLabel.setBounds(0, 0, 400, 60);
		shell.pack();
		centerWindow(shell);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void centerWindow(Shell shell) {
		Rectangle bds = shell.getDisplay().getBounds();
		Point p = shell.getSize();
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;
		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Display display = new Display();
		Main a = new Main(display);
		display.dispose();
	}
}