package com.fc.util;

import com.fc.server.protocol.MongoQueueProtocol;
import com.fc.server.protocol.TelnetProtocol;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by fc on 14-11-24.
 */
public class GuiceDI {
    public static Injector injector;

    static{
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(TelnetProtocol.class).to(MongoQueueProtocol.class);
            }
        });
    }
}
