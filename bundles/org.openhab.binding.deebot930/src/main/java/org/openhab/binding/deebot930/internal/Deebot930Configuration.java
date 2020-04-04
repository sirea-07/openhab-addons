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

import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link Deebot930Configuration} class contains fields mapping thing configuration parameters.
 *
 * @author sirea - Initial contribution
 */
public class Deebot930Configuration {

    public @Nullable String serverAddress;
    public Integer serverPort = 5223;
    public String username = "";
    public String password = "";
    public String domain = "";
    public String resource = "";
}
