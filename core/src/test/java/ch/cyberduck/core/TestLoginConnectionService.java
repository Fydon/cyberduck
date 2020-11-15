package ch.cyberduck.core;

/*
 * Copyright (c) 2002-2016 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.threading.CancelCallback;

public class TestLoginConnectionService extends LoginConnectionService {
    public TestLoginConnectionService() {
        super(new DisabledLoginCallback(), new DisabledHostKeyCallback(), new DisabledPasswordStore(), new DisabledProgressListener());
    }

    @Override
    public void connect(final Session<?> session, final CancelCallback callback) {
        //
    }
}
