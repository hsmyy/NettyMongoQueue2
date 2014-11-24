package com.fc.queue;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by fc on 14-11-24.
 */
public abstract class MongoAbstractQueue<R> implements Queue<R>{

    protected MongoClient mongoClient;

    private static final Logger LOG = LoggerFactory.getLogger(MongoAbstractQueue.class);

    protected MongoClient connection(Properties props) throws UnknownHostException {
        if(props.getProperty(MongoConst.MONGO_USER) != null && props.getProperty(MongoConst.MONGO_USER).length() > 0){
            MongoCredential credential = MongoCredential.createMongoCRCredential(props.getProperty(MongoConst.MONGO_USER), MongoConst.ADMIN, props.getProperty(MongoConst.MONGO_PASSWORD).toCharArray());
            mongoClient = new MongoClient(new ServerAddress( props.getProperty(MongoConst.MONGO_HOST), Integer.parseInt(props.getProperty(MongoConst.MONGO_PORT))), Arrays.asList(credential));
            LOG.debug("credential: use");
        }else{
            mongoClient = new MongoClient(new ServerAddress( props.getProperty(MongoConst.MONGO_HOST), Integer.parseInt(props.getProperty(MongoConst.MONGO_PORT))));
            LOG.debug("credential: not use");
        }
        return mongoClient;
    }
}
