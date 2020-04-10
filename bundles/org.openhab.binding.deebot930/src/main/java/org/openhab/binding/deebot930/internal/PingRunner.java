package org.openhab.binding.deebot930.internal;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.ping.PingManager;

public class PingRunner implements Runnable {
    private XMPPTCPConnection connection;

    public PingRunner(XMPPTCPConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        PingManager pingManager = PingManager.getInstanceFor(connection);
        pingManager.setPingInterval(120);
    }
}
