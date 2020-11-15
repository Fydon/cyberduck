package ch.cyberduck.core.onedrive;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.DisabledCancelCallback;
import ch.cyberduck.core.DisabledHostKeyCallback;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.DisabledPasswordStore;
import ch.cyberduck.core.DisabledProgressListener;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.LoginConnectionService;
import ch.cyberduck.core.LoginOptions;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Profile;
import ch.cyberduck.core.ProtocolFactory;
import ch.cyberduck.core.Scheme;
import ch.cyberduck.core.serializer.impl.dd.ProfilePlistReader;
import ch.cyberduck.core.ssl.DefaultX509KeyManager;
import ch.cyberduck.core.ssl.DefaultX509TrustManager;
import ch.cyberduck.test.IntegrationTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Category(IntegrationTest.class)
public class OneDriveMeContextLoginTest {

    protected OneDriveSession session;

    @After
    public void disconnect() throws Exception {
        session.close();
    }

    @Before
    public void setup() throws Exception {
        final ProtocolFactory factory = new ProtocolFactory(new HashSet<>(Collections.singleton(new OneDriveProtocol())));
        final Profile profile = new ProfilePlistReader(factory).read(
            this.getClass().getResourceAsStream("/Microsoft OneDrive.cyberduckprofile"));
        final Host host = new Host(profile, profile.getDefaultHostname(), new Credentials("cyberduck"));
        session = new OneDriveSession(host, new DefaultX509TrustManager(), new DefaultX509KeyManager());
        final LoginConnectionService login = new LoginConnectionService(new DisabledLoginCallback() {
            @Override
            public Credentials prompt(final Host bookmark, final String username, final String title, final String reason, final LoginOptions options) {
                fail(reason);
                return null;
            }
        }, new DisabledHostKeyCallback(),
            new DisabledPasswordStore() {
                @Override
                public String getPassword(Scheme scheme, int port, String hostname, String user) {
                    if(user.endsWith("Microsoft OneDrive (cyberduck) OAuth2 Access Token")) {
                        return System.getProperties().getProperty("onedrive.accesstoken");
                    }
                    if(user.endsWith("Microsoft OneDrive (cyberduck) OAuth2 Refresh Token")) {
                        return System.getProperties().getProperty("onedrive.refreshtoken");
                    }
                    return null;
                }

                @Override
                public String getPassword(String hostname, String user) {
                    return super.getPassword(hostname, user);
                }
            }, new DisabledProgressListener());
        login.check(session, new DisabledCancelCallback());
    }

    @Test
    public void testLogin() throws Exception {
        final Path home = new OneDriveHomeFinderService(session).find();
        assertEquals("My Files", home.getName());
    }
}
