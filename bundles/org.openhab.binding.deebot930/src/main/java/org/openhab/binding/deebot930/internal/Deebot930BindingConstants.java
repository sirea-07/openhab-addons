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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.DefaultSystemChannelTypeProvider;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link Deebot930BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author sirea - Initial contribution
 */
@NonNullByDefault
public class Deebot930BindingConstants {

    private static final String BINDING_ID = "deebot930";

    // List of all Thing Type UIDs
    public static final ThingTypeUID BRIDGE_TYPE_XMPP_SERVER = new ThingTypeUID(BINDING_ID, "server");
    public static final ThingTypeUID THING_TYPE_DEEBOT = new ThingTypeUID(BINDING_ID, "deebot");

    // List of all Channel ids
    public static final String CHANNEL_CLEAN = "clean";
    public static final String CHANNEL_BATTERY_LEVEL = DefaultSystemChannelTypeProvider.SYSTEM_CHANNEL_BATTERY_LEVEL
            .getUID().getId();
}
