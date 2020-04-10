package org.openhab.binding.deebot930.internal;

import de.caterdev.vaccumclean.deebot.smack.packets.Query;
import org.jivesoftware.smack.iqrequest.AbstractIqRequestHandler;
import org.jivesoftware.smack.packet.IQ;

public class Deebot930IqRequestHandler extends AbstractIqRequestHandler {

    protected Deebot930IqRequestHandler() {
        super(Query.ELEMENT, Query.NAMESPACE, IQ.Type.set, Mode.sync);
    }

    @Override
    public IQ handleIQRequest(IQ iqRequest) {
        return iqRequest;
    }
}
