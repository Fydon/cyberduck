package ch.cyberduck.core.googledrive;

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

import ch.cyberduck.core.Cache;
import ch.cyberduck.core.ListProgressListener;
import ch.cyberduck.core.ListService;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.exception.BackgroundException;

public class DriveDefaultListService extends AbstractDriveListService {

    private final DriveFileidProvider fileid;

    public DriveDefaultListService(final DriveSession session, final DriveFileidProvider fileid) {
        super(session);
        this.fileid = fileid;
    }

    public DriveDefaultListService(final DriveSession session, final DriveFileidProvider fileid, final int pagesize) {
        super(session, pagesize);
        this.fileid = fileid;
    }

    protected String query(final Path directory, final ListProgressListener listener) throws BackgroundException {
        return String.format("'%s' in parents", fileid.getFileid(directory, listener));
    }

    @Override
    public ListService withCache(final Cache<Path> cache) {
        fileid.withCache(cache);
        return this;
    }
}
