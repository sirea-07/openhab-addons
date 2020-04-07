package discovery;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.openhab.binding.deebot930.internal.Deebot930ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

import static org.openhab.binding.deebot930.internal.Deebot930BindingConstants.THING_TYPE_DEEBOT;

@NonNullByDefault
public class Deebot930DiscoveryService extends AbstractDiscoveryService implements ThingHandlerService {
    private final static Logger logger = LoggerFactory.getLogger(Deebot930DiscoveryService.class);

    private static final int DISCOVERY_TIMEOUT = 15;

    private @Nullable Deebot930ServerHandler bridgeHandler;

    public Deebot930DiscoveryService(Deebot930ServerHandler bridgeHandler) {
        super(Collections.singleton(THING_TYPE_DEEBOT), DISCOVERY_TIMEOUT, false);
        this.bridgeHandler = bridgeHandler;
    }

    @Override
    protected void startScan() {
        logger.info("Starting Discovery of Deebots.");
        try {
            Roster.getInstanceFor(this.bridgeHandler.getServerConnection()).reloadAndWait();
            Set<RosterEntry> entries = Roster.getInstanceFor(this.bridgeHandler.getServerConnection()).getEntries();
            for (RosterEntry entry : entries) {
                ThingUID thingUID = new ThingUID(THING_TYPE_DEEBOT,
                        String.format("%s-%s", entry.getJid().getLocalpartOrThrow().toString(), entry.getJid().getDomain().toString().replace(".","_")));

                DiscoveryResult discoveryResult = DiscoveryResultBuilder
                        .create(thingUID)
                        .withThingType(THING_TYPE_DEEBOT)
                        .withProperty("jid", entry.getJid().asUnescapedString() + "/" + entry.getGroups().get(0).getName())
                        .withProperty("name", entry.getName())
                        .withBridge(bridgeHandler.getThing().getUID())
                        .withLabel(entry.getName()).build();

                thingDiscovered(discoveryResult);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof Deebot930ServerHandler) {
            bridgeHandler = (Deebot930ServerHandler) handler;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return bridgeHandler;
    }

    @Override
    public void activate() {
        super.activate(null);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }
}
