/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.deebot930.internal;

import static org.openhab.binding.deebot930.internal.Deebot930BindingConstants.*;

import java.util.*;

import discovery.Deebot930DiscoveryService;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Deebot930HandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author sirea - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.deebot930", service = ThingHandlerFactory.class)
public class Deebot930HandlerFactory extends BaseThingHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(Deebot930HandlerFactory.class);

    private static final Collection<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Arrays.asList(BRIDGE_TYPE_XMPP_SERVER, THING_TYPE_DEEBOT);

    private final Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        logger.warn("############ Deebot930HandlerFactory.createHandler");
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        logger.warn("############ Deebot930HandlerFactory.createHandler");
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_DEEBOT.equals(thingTypeUID)) {
            return new Deebot930Handler(thing);
        }

        if (BRIDGE_TYPE_XMPP_SERVER.equals(thingTypeUID)) {
            Deebot930ServerHandler handler = new Deebot930ServerHandler((Bridge) thing);
            registerDeebotDiscoveryService(handler);
            return handler;
        }

        return null;
    }

    private synchronized void registerDeebotDiscoveryService(Deebot930ServerHandler bridgeHandler) {
        Deebot930DiscoveryService discoveryService = new Deebot930DiscoveryService(bridgeHandler);
        discoveryService.activate();
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof Deebot930ServerHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                // remove discovery service, if bridge handler is removed
                Deebot930DiscoveryService service = (Deebot930DiscoveryService) bundleContext
                        .getService(serviceReg.getReference());
                serviceReg.unregister();
                if (service != null) {
                    service.deactivate();
                }
            }
        }
    }
}
