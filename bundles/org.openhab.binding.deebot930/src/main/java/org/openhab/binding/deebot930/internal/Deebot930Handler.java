/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.deebot930.internal;

import static org.openhab.binding.deebot930.internal.Deebot930BindingConstants.*;

import de.caterdev.vaccumclean.deebot.smack.packets.action.ActionPacket;
import de.caterdev.vaccumclean.deebot.smack.packets.action.CleanPacket;
import de.caterdev.vaccumclean.deebot.smack.packets.response.ResponsePacket;
import de.caterdev.vacuumclean.deebot.core.constants.CleanAction;
import de.caterdev.vacuumclean.deebot.core.constants.CleanSpeed;
import de.caterdev.vacuumclean.deebot.core.constants.CleanType;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.ExceptionCallback;
import org.jivesoftware.smack.util.SuccessCallback;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Deebot930Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author sirea - Initial contribution
 */
@NonNullByDefault
public class Deebot930Handler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(Deebot930Handler.class);
    private @Nullable Deebot930ServerHandler bridge;

    public Deebot930Handler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.warn("############ Deebot930Handler.handleCommand");
        if (CHANNEL_CLEAN.equals(channelUID.getId())) {
            if (command instanceof OnOffType) {
                try {
                    String deebotJid = (String) thing.getConfiguration().get("jid");
                    String username = (String) bridge.getThing().getConfiguration().get("username");
                    String domain = (String) bridge.getThing().getConfiguration().get("domain");
                    String resource = (String) bridge.getThing().getConfiguration().get("resource");

                    CleanAction action = OnOffType.ON.equals(command) ? CleanAction.Start : CleanAction.Stop;

                    sendPacket(new CleanPacket(JidCreate.from(username, domain, resource), JidCreate.from(deebotJid), CleanType.Auto.getType(), CleanSpeed.Standard.getValue(), action.getValue(), null));
                } catch (XmppStringprepException e) {
                    logger.error("", e);
                }
            }
        }
    }

    @Override
    public void initialize() {
        logger.warn("############ Deebot930Handler.initialize");
        // logger.debug("Start initializing!");

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly. Also, before leaving this method a thing
        // status from one of ONLINE, OFFLINE or UNKNOWN must be set. This might already be the real thing status in
        // case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);
        bridge = (Deebot930ServerHandler) getBridge().getHandler();

        // Example for background initialization:
        scheduler.execute(() -> {
            boolean thingReachable = true; // <background task with long running initialization here>
            // when done do:
            if (thingReachable) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }
        });

        // logger.debug("Finished initializing!");

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }

    private void sendPacket(CleanPacket packet) {
        bridge.getServerConnection().sendIqRequestAsync(packet)
                .onSuccess(result -> logger.info("SUCCESS"))
                .onError(exception -> logger.error("ERROR", exception));
    }
}
