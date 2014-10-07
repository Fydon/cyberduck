﻿// 
// Copyright (c) 2010-2012 Yves Langisch. All rights reserved.
// http://cyberduck.ch/
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
// 
// Bug fixes, suggestions and comments should be sent to:
// yves@cyberduck.ch
// 

using System;
using System.Net;
using ch.cyberduck.core;

using InetSocketAddress = java.net.InetSocketAddress;

namespace Ch.Cyberduck.Core
{
    public class SystemProxy : AbstractProxyFinder
    {
        private readonly IWebProxy _system = WebRequest.GetSystemWebProxy();

        public override Proxy find(Host host)
        {
            string target = new HostUrlProvider(false).get(host);
            if (_system.IsBypassed(new Uri(target)))
            {
                return Proxy.DIRECT;
            }
            Uri proxy = _system.GetProxy(new Uri(target));
            return new Proxy(Proxy.Type.valueOf(proxy.Scheme.ToUpper()), new InetSocketAddress(proxy.Host, proxy.Port));
        }

        public static void Register()
        {
            ProxyFactory.addFactory(ch.cyberduck.core.Factory.NATIVE_PLATFORM, new Factory());
        }

        private class Factory : ProxyFactory
        {
            protected override object create()
            {
                return new SystemProxy();
            }
        }
    }
}