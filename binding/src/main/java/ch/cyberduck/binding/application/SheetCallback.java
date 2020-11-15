package ch.cyberduck.binding.application;

/*
 *  Copyright (c) 2009 David Kocher. All rights reserved.
 *  http://cyberduck.ch/
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *  dkocher@cyberduck.ch
 */

public interface SheetCallback {

    /**
     * Use default option; 'OK'
     */
    int DEFAULT_OPTION = NSAlert.NSAlertDefaultReturn;
    /**
     * Cancel option
     */
    int CANCEL_OPTION = NSAlert.NSAlertOtherReturn;
    /**
     * Alternate action
     */
    int ALTERNATE_OPTION = NSAlert.NSAlertAlternateReturn;

    /**
     * Called after the sheet has been dismissed by the user.
     *
     * @param returncode Selected button
     */
    void callback(int returncode);
}
