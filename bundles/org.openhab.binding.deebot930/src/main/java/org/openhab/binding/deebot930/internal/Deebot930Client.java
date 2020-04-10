package org.openhab.binding.deebot930.internal;

import de.caterdev.vaccumclean.deebot.smack.packets.action.CleanPacket;
import de.caterdev.vaccumclean.deebot.smack.packets.action.GetBatteryInfoPacket;
import de.caterdev.vaccumclean.deebot.smack.packets.response.GetBatteryInfoResponsePacket;
import de.caterdev.vacuumclean.deebot.core.constants.CleanAction;
import de.caterdev.vacuumclean.deebot.core.constants.CleanSpeed;
import de.caterdev.vacuumclean.deebot.core.constants.CleanType;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.Jid;

import java.util.concurrent.TimeUnit;

public class Deebot930Client {

    private XMPPTCPConnection serverConnection;

    public Deebot930Client(XMPPTCPConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void clean(Jid from, Jid to, CleanAction action) {
        CleanPacket cleanPacket = new CleanPacket(from, to, CleanType.Auto.getType(), CleanSpeed.Standard.getValue(), action.getValue(), null);

        serverConnection.sendIqRequestAsync(cleanPacket);
    }


    public String getBatteryLevel(Jid from, Jid to) {
        GetBatteryInfoPacket batteryInfoPacket = new GetBatteryInfoPacket(from, to);

        GetBatteryInfoResponsePacket responsePacket;
        serverConnection.registerIQRequestHandler(new Deebot930IqRequestHandler());
        try {
            responsePacket = (GetBatteryInfoResponsePacket) serverConnection.sendIqRequestAsync(batteryInfoPacket).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return responsePacket.getPower();
    }
}
