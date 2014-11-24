package com.fc.queue;

import com.fc.queue.model.User;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fc on 14-11-24.
 */
public class MongoUserQueue extends MongoAbstractQueue<User>{

    private DBCollection collection;

    private Logger LOG = LoggerFactory.getLogger(MongoUserQueue.class);

    public MongoUserQueue(){
        try(InputStream is = this.getClass().getResourceAsStream(MongoConst.NETTYMONGO_PROPERTIES)){
            Properties props = new Properties();
            props.load(is);

            connection(props);

            DB db = mongoClient.getDB(props.getProperty(MongoConst.MONGO_DB));
            LOG.debug("use db: " + props.getProperty(MongoConst.MONGO_DB));
            db.slaveOk();
            String prefix = props.getProperty(MongoConst.MONGO_PREFIX);
            collection = db.getCollection(prefix + "_user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enqueue(User obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User dequeue() {
        DBCursor cursor = null;

        return null;
    }

    @Override
    public long len() {
        return 0;
    }
}
