package ch.cyberduck.core.worker;

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

import ch.cyberduck.core.LoginCallback;
import ch.cyberduck.core.PasswordStore;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Session;
import ch.cyberduck.core.cryptomator.impl.CryptoVault;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.LoginCanceledException;
import ch.cyberduck.core.pool.SessionPool;

public class CreateVaultWorker extends CreateDirectoryWorker {

    private final SessionPool pool;
    private final Path directory;
    private final PasswordStore keychain;
    private final LoginCallback login;

    public CreateVaultWorker(final SessionPool pool, final Path directory, final String region, final PasswordStore keychain, final LoginCallback login) {
        super(directory, region);
        this.pool = pool;
        this.directory = directory;
        this.keychain = keychain;
        this.login = login;
    }

    @Override
    public Boolean run(final Session<?> session) throws BackgroundException {
        try {
            if(super.run(session)) {
                session.withVault(new CryptoVault(pool, directory, keychain, login).create());
            }
        }
        catch(LoginCanceledException e) {
            return false;
        }
        return true;
    }
}
