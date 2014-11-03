package com.fc.queue;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fc on 14-11-1.
 */
public class MongoQueue implements Queue<URL>{

    private MongoClient mongoClient;
    private DBCollection requestCollection;
    private DBCollection dedupCollection;

    private Logger LOG = LoggerFactory.getLogger(MongoQueue.class);

    private static ReentrantLock enLock = new ReentrantLock();
    private static ReentrantLock deLock = new ReentrantLock();

    public MongoQueue(){
        try(InputStream is = this.getClass().getResourceAsStream("/nettymongo.properties")){
            Properties props = new Properties();
            props.load(is);

            mongoClient = new MongoClient(props.getProperty("mongoHost"), Integer.parseInt(props.getProperty("mongoPort")));
            DB db = mongoClient.getDB(props.getProperty("mongoDB"));
            String prefix = props.getProperty("mongoPrefix");
            requestCollection = db.getCollection(prefix + "_request");
            dedupCollection = db.getCollection(prefix + "_dedup");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enqueue(URL obj) {
        DBCursor cursor = null;
        try {
            enLock.lock();
            String hash = hash(obj.getUrl());
            cursor = dedupCollection.find(new BasicDBObject().append("url", hash));
            if(cursor.count() <= 0){
                WriteResult result = requestCollection.insert(new BasicDBObject().append("url",obj.getUrl()).append("timestamp",obj.getTimestamp()));
                if(result.getN() == 0){
                    LOG.warn("[ENQUEUE] url[" + obj.getUrl() +"] insert but no effect");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            if(cursor != null){
                cursor.close();
            }
            enLock.unlock();

        }
    }

    @Override
    public URL dequeue() {
        DBCursor cursor = null;
        deLock.lock();
        try{
            cursor = requestCollection.find().sort(new BasicDBObject().append("timestamp",1)).limit(1);
            if(cursor.count() > 0){
                DBObject obj = cursor.next();
                URL url = new URL(obj);
                String hashUrl = hash(url.getUrl());
                WriteResult result = requestCollection.remove(new BasicDBObject().append("url", url.getUrl()));
                if(result.getN() == 0){
                    LOG.warn("[DEQUEUE] url[" + url.getUrl() + "] remove but no effect");
                }

                result = dedupCollection.insert(new BasicDBObject().append("url", hashUrl).append("timestamp",url.getTimestamp()));
                if(result.getN() == 0){
                    LOG.warn("[DEQUEUE] url[" + url.getUrl() + "] add dedup but no effect");
                }
                return url;
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            if(cursor != null){
                cursor.close();
            }
            deLock.unlock();
        }
    }

    public void close(){
        if(mongoClient != null){
            mongoClient.close();
        }
    }

    private String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] passBytes = str.getBytes();
        byte[] passHash = sha256.digest(passBytes);
        return Base64.getEncoder().encodeToString(passHash);
    }
}
