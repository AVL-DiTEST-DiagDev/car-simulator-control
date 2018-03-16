#!/usr/bin/env python3

import discovery_listen_thread,time

if __name__ == '__main__':
    disc = discovery_listen_thread.thread()
    disc.start()
    time.sleep(60)
    disc.terminate()

