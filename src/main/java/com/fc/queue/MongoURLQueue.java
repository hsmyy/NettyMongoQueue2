package com.fc.queue;

import com.fc.queue.model.URLRequest;
import com.google.inject.Singleton;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fc on 14-11-1.
 */
@Singleton
public class MongoURLQueue extends MongoAbstractQueue<URLRequest>{

    private DBCollection requestCollection;
    private DBCollection dedupCollection;

    private Logger LOG = LoggerFactory.getLogger(MongoURLQueue.class);

    private static ReentrantLock enLock = new ReentrantLock();
    private static ReentrantLock deLock = new ReentrantLock();

    public MongoURLQueue(){
        try(InputStream is = this.getClass().getResourceAsStream(MongoConst.NETTYMONGO_PROPERTIES)){
            Properties props = new Properties();
            props.load(is);
//            if(props.getProperty(MongoConst.MONGO_USER) != null && props.getProperty(MongoConst.MONGO_USER).length() > 0){
//                MongoCredential credential = MongoCredential.createMongoCRCredential(props.getProperty(MongoConst.MONGO_USER), MongoConst.ADMIN, props.getProperty(MongoConst.MONGO_PASSWORD).toCharArray());
//                mongoClient = new MongoClient(new ServerAddress( props.getProperty(MongoConst.MONGO_HOST), Integer.parseInt(props.getProperty(MongoConst.MONGO_PORT))), Arrays.asList(credential));
//                LOG.debug("credential: use");
//            }else{
//                mongoClient = new MongoClient(new ServerAddress( props.getProperty(MongoConst.MONGO_HOST), Integer.parseInt(props.getProperty(MongoConst.MONGO_PORT))));
//                LOG.debug("credential: not use");
//            }
            connection(props);

            DB db = mongoClient.getDB(props.getProperty(MongoConst.MONGO_DB));
            LOG.debug("use db: " + props.getProperty(MongoConst.MONGO_DB));
            db.slaveOk();
            String prefix = props.getProperty(MongoConst.MONGO_PREFIX);
            requestCollection = db.getCollection(prefix + MongoConst.REQUEST);
            dedupCollection = db.getCollection(prefix + MongoConst.DEDUP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enqueue(URLRequest obj) {
        DBCursor cursor = null;
        try {
            enLock.lock();
            cursor = dedupCollection.find(new BasicDBObject().append(MongoConst.HASH, obj.getHash()));
            if(cursor.count() <= 0){
                WriteResult result = requestCollection.insert(new BasicDBObject().append(MongoConst.URL,obj.getUrl()).append(MongoConst.TIMESTAMP,obj.getTimestamp()).append(MongoConst.HASH, obj.getHash()));
                if(result.getN() == 0){
                    LOG.warn("[ENQUEUE] url["+ obj.getUrl()+ "] insert but no effect");
                }
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
            enLock.unlock();

        }
    }

    @Override
    public URLRequest dequeue() {
        DBCursor cursor = null;
        deLock.lock();
        try{
            cursor = requestCollection.find().sort(new BasicDBObject().append(MongoConst.TIMESTAMP,1)).limit(1);
            if(cursor.count() > 0){
                DBObject obj = cursor.next();
                URLRequest urlRequest = new URLRequest(obj);
                WriteResult result = requestCollection.remove(new BasicDBObject().append(MongoConst.URL, urlRequest.getUrl()));
                if(result.getN() == 0){
                    LOG.warn("[DEQUEUE] url["+ urlRequest.getUrl()+ "] remove but no effect");
                }
                if(urlRequest.getHash() == null || urlRequest.getHash().length() == 0){
                    try {
                        urlRequest.setHash(hash(urlRequest.getUrl()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                result = dedupCollection.insert(new BasicDBObject().append(MongoConst.URL, urlRequest.getUrl()).append(MongoConst.TIMESTAMP, urlRequest.getTimestamp()).append(MongoConst.HASH, urlRequest.getHash()));
                if(result.getN() == 0){
                    LOG.warn("[DEQUEUE] url["+ urlRequest.getUrl()+ "] add dedup but no effect");
                }
                return urlRequest;
            }
            return null;
        } finally {
            if(cursor != null){
                cursor.close();
            }
            deLock.unlock();
        }
    }

    @Override
    public long len() {
        return requestCollection.count();
    }

    public void close(){
        if(mongoClient != null){
            mongoClient.close();
        }
    }

    private String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance(MongoConst.SHA_256);
        byte[] passBytes = str.getBytes();
        byte[] passHash = sha256.digest(passBytes);
        return Base64.getEncoder().encodeToString(passHash);
    }
}
