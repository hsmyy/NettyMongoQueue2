package com.fc.server.protocol;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.util.Map;

/**
 * Created by fc on 14-11-24.
 */
public class MongoDataprotocol extends TelnetProtocol{

    public static final String GET = "GET";

    private MongoClient mongoClient;
    private Map<String,DBCollection> collectionMap;

    @Override
    protected String process(String request) {
        return null;
    }
}
