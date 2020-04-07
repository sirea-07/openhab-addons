package org.openhab.binding.deebot930.internal;

import de.caterdev.vaccumclean.deebot.smack.packets.Query;
import de.caterdev.vacuumclean.core.ssl.VacuumCleanSSLContext;
import discovery.Deebot930DiscoveryService;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.eclipse.smarthome.core.types.Command;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.parts.Resourcepart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@NonNullByDefault
public class Deebot930ServerHandler extends BaseBridgeHandler {
    private static final Logger logger = LoggerFactory.getLogger(Deebot930ServerHandler.class);
    private @Nullable XMPPTCPConnection serverConnection;

    public Deebot930ServerHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.warn("############ Deebot930ServerHandler.initialize");
        updateStatus(ThingStatus.UNKNOWN);
        scheduler.schedule(this::connectToServer, 0, TimeUnit.SECONDS);
    }

    public @Nullable XMPPTCPConnection getServerConnection() {
        return serverConnection;
    }

    private void connectToServer() {
        logger.warn("############ Deebot930ServerHandler.connectToServer");
        Deebot930Configuration config = getConfigAs(Deebot930Configuration.class);
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();

        try {
            configBuilder.setXmppDomain(config.domain);
            configBuilder.setHost(config.serverAddress);
            configBuilder.setPort(config.serverPort);
            configBuilder.enableDefaultDebugger();
            configBuilder.setSendPresence(false);
            //TODO: not setting this might enable communication with ecovacs server?
            configBuilder.setCustomSSLContext(VacuumCleanSSLContext.get());
            Roster.setRosterLoadedAtLoginDefault(false);

            serverConnection = new XMPPTCPConnection(configBuilder.build());
            ProviderManager.addIQProvider(Query.ELEMENT, Query.NAMESPACE, new Query.Provider());

            serverConnection.setFromMode(XMPPConnection.FromMode.USER);

            logger.debug("Connecting to Deebot XMPP Server...");
            serverConnection.connect();

            logger.debug("Login to Deebot XMPP Server...");
            serverConnection.login(config.username, config.password, Resourcepart.from(config.resource));

            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.warn("############ Deebot930ServerHandler.handleCommand");
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singletonList(Deebot930DiscoveryService.class);
    }


}
