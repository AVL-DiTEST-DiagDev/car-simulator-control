This is the discovery server that answers discovery requests.
It is implemented in the class discovery_listen_thread.py

Here are examples for the python code:

To start the thread:

    disc = discovery_listen_thread.thread()
    disc.start()
	
To stop the thread:

    disc.terminate()
	

There is a short demo in the __main__.py that keeps the server open for 60sec.
you can start it using the command line:
    python3 carsim_discovery
	
In order to start the discovery server when the Raspberry Pi boots up, the appropriate command could be added to the file /etc/rc.local
