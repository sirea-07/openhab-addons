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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link Deebot930HandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author sirea - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.deebot930", service = ThingHandlerFactory.class)
public class Deebot930HandlerFactory extends BaseThingHandlerFactory {

    private static final Collection<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Arrays.asList(BRIDGE_TYPE_XMPP_SERVER, THING_TYPE_DEEBOT);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_DEEBOT.equals(thingTypeUID)) {
            return new Deebot930Handler(thing);
        }

        if (BRIDGE_TYPE_XMPP_SERVER.equals(thingTypeUID)) {
            return new Deebot930ServerHandler((Bridge)thing);
        }

        return null;
    }
}
