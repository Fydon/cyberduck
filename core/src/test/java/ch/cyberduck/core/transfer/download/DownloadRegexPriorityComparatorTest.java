package ch.cyberduck.core.transfer.download;

/*
 * Copyright (c) 2002-2015 David Kocher. All rights reserved.
 * http://cyberduck.ch/
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
 *
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.Path;

import org.junit.Test;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;

public class DownloadRegexPriorityComparatorTest {

    @Test
    public void testCompare() {
        assertEquals(-1, new DownloadRegexPriorityComparator(".*\\.html").compare(
                new Path("f.html", EnumSet.of(Path.Type.file)), new Path("g.t", EnumSet.of(Path.Type.file))));
        assertEquals(1, new DownloadRegexPriorityComparator(".*\\.html").compare(
                new Path("f.htm", EnumSet.of(Path.Type.file)), new Path("g.html", EnumSet.of(Path.Type.file))));
        assertEquals(0, new DownloadRegexPriorityComparator(".*\\.html").compare(
                new Path("f.html", EnumSet.of(Path.Type.file)), new Path("g.html", EnumSet.of(Path.Type.file))));
    }
}