package ch.cyberduck.core.http;

/*
 * Copyright (c) 2002-2015 David Kocher. All rights reserved.
 * http://cyberduck.io/
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
 * Bug fixes, suggestions and comments should be sent to:
 * feedback@cyberduck.io
 */

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.jets3t.service.io.UnrecoverableIOException;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.barchart.udt.ExceptionUDT;

/**
 * @version $Id$
 */
public class ExtendedHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

    private static final List<Class<? extends IOException>> exceptions = Arrays.asList(
            UnrecoverableIOException.class,
            InterruptedIOException.class,
            UnknownHostException.class,
            ConnectException.class,
            ExceptionUDT.class,
            // Not providing SSLException.class, because broken pipe failures are wrapped in SSL Exceptions.
            // "Broken pipe".equals(ExceptionUtils.getRootCause(failure).getMessage())
            SSLHandshakeException.class);

    public ExtendedHttpRequestRetryHandler(final int retryCount) {
        super(retryCount, false, exceptions);
    }

    @Override
    public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
        if(ExceptionUtils.getRootCause(exception) != null) {
            if(ExceptionUtils.getRootCause(exception) instanceof RuntimeException) {
                return false;
            }
        }
        return super.retryRequest(exception, executionCount, context);
    }
}
