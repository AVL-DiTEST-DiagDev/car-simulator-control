#!/usr/bin/env python3
import threading, socket

class thread(threading.Thread):
    discoveryRequest = bytes([0x41,0x56,0x4c,0x44,0x69,0x54,0x45,0x53,0x54,0x2d,0x43,0x61,0x72,0x53,0x69,0x6d,0x2d,0x44,0x69,0x73,0x63,0x6f,0x76,0x65,0x72,0x79,0x00,0x00,0xFF,0xFF])
    discoveryResponse = bytes([0x41,0x56,0x4c,0x44,0x69,0x54,0x45,0x53,0x54,0x2d,0x43,0x61,0x72,0x53,0x69,0x6d,0x2d,0x44,0x69,0x73,0x63,0x6f,0x76,0x65,0x72,0x79,0x00,0x01,0xFF,0xFE])
    discoveryPort = 15123

    def __init__ (self):
        threading.Thread.__init__(self)
        self.serversocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.serversocket.bind(("0.0.0.0", self.discoveryPort))
    
    def run(self):
        print('Starting discovery listen thread')
        while True:
            print('Waiting for packets to arrive')
            receivedBytes, remoteAddress = self.serversocket.recvfrom(1500)
            if not receivedBytes:
                print('Socket closed')
                break
            print('Received Package from', remoteAddress, receivedBytes)
            if receivedBytes == self.discoveryRequest:
                print('Discovery Package OK - replying')
                self.serversocket.sendto(self.discoveryResponse, remoteAddress)
            else: 
                print('No discovery package - expected: ', self.discoveryRequest)
        print('Discovery listen thread finished')

    def terminate(self):
        print('Closing UDP socket')
        try:
            self.serversocket.shutdown(socket.SHUT_RDWR)
        except:
            print('Exception caught as expected')
            # For some reason this does what it is expected to
            # but subsequently throws an error 
        self.serversocket.close()
        print('Waiting for thread to finish')
        self.join()
        print('Discovery listen thread exited')

