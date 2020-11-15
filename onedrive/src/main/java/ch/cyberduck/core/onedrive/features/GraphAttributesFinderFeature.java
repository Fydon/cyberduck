package ch.cyberduck.core.onedrive.features;

/*
 * Copyright (c) 2002-2018 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.DefaultIOExceptionMappingService;
import ch.cyberduck.core.DescriptiveUrl;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.features.AttributesFinder;
import ch.cyberduck.core.onedrive.GraphExceptionMappingService;
import ch.cyberduck.core.onedrive.GraphSession;

import org.apache.log4j.Logger;
import org.nuxeo.onedrive.client.OneDriveAPIException;
import org.nuxeo.onedrive.client.OneDriveFolder;
import org.nuxeo.onedrive.client.OneDriveItem;
import org.nuxeo.onedrive.client.OneDriveRemoteItem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GraphAttributesFinderFeature implements AttributesFinder {
    private static final Logger log = Logger.getLogger(GraphAttributesFinderFeature.class);

    private final GraphSession session;

    public GraphAttributesFinderFeature(final GraphSession session) {
        this.session = session;
    }

    @Override
    public PathAttributes find(final Path file) throws BackgroundException {
        if(file.isRoot()) {
            return PathAttributes.EMPTY;
        }
        final OneDriveItem item = session.toItem(file);
        try {
            final OneDriveItem.Metadata metadata = item.getMetadata();
            return this.toAttributes(metadata);
        }
        catch(OneDriveAPIException e) {
            throw new GraphExceptionMappingService().map("Failure to read attributes of {0}", e, file);
        }
        catch(IOException e) {
            throw new DefaultIOExceptionMappingService().map("Failure to read attributes of {0}", e, file);
        }
    }

    public PathAttributes toAttributes(final OneDriveItem.Metadata metadata) {
        final PathAttributes attributes = new PathAttributes();
        attributes.setETag(metadata.getETag());
        attributes.setSize(metadata.getSize());
        final OneDriveFolder.Reference parent = metadata.getParentReference();
        if(metadata instanceof OneDriveRemoteItem.Metadata) {
            final OneDriveItem.Metadata remoteMetadata = ((OneDriveRemoteItem.Metadata) metadata).getRemoteItem();
            final OneDriveFolder.Reference remoteParent = remoteMetadata.getParentReference();
            if(parent == null) {
                attributes.setVersionId(String.join(String.valueOf(Path.DELIMITER),
                    remoteParent.getDriveId(), remoteParent.getId()));
            }
            else {
                attributes.setVersionId(String.join(String.valueOf(Path.DELIMITER),
                    parent.getDriveId(), metadata.getId(),
                    remoteParent.getDriveId(), remoteMetadata.getId()));
            }
        }
        else {
            attributes.setVersionId(String.join(String.valueOf(Path.DELIMITER), parent.getDriveId(), metadata.getId()));
        }
        try {
            attributes.setLink(new DescriptiveUrl(new URI(metadata.getWebUrl()), DescriptiveUrl.Type.http));
        }
        catch(URISyntaxException e) {
            log.warn(String.format("Cannot set link. Web URL returned %s", metadata.getWebUrl()), e);
        }
        if(null != metadata.getFileSystemInfo()) {
            if(-1L == metadata.getFileSystemInfo().getLastModifiedDateTime().toInstant().toEpochMilli()) {
                attributes.setModificationDate(metadata.getLastModifiedDateTime().toInstant().toEpochMilli());
            }
            else {
                attributes.setModificationDate(metadata.getFileSystemInfo().getLastModifiedDateTime().toInstant().toEpochMilli());
            }
            if(-1 == metadata.getFileSystemInfo().getCreatedDateTime().toInstant().toEpochMilli()) {
                attributes.setCreationDate(metadata.getCreatedDateTime().toInstant().toEpochMilli());
            }
            else {
                attributes.setCreationDate(metadata.getFileSystemInfo().getCreatedDateTime().toInstant().toEpochMilli());
            }
        }
        else {
            attributes.setModificationDate(metadata.getLastModifiedDateTime().toInstant().toEpochMilli());
            attributes.setCreationDate(metadata.getCreatedDateTime().toInstant().toEpochMilli());
        }
        return attributes;
    }
}
