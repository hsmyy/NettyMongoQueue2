package com.fc.server.protocol;

import com.fc.queue.MongoURLQueue;
import com.fc.queue.Queue;
import com.fc.queue.model.URLRequest;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fc on 14-11-13.
 */
public class MongoQueueProtocol extends TelnetProtocol{
    public static final String GET = "GET";
    public static final String LEN = "LEN";
    public static final String OK = "OK";
    public static final int LOG_SEGMENT = 100;
    public static final String PV = "[PV] ";

    private Queue<URLRequest> mongoQueue;

    private static AtomicInteger pv = new AtomicInteger(0);

    private Logger LOG = LoggerFactory.getLogger(MongoQueueProtocol.class);

    @Override
    protected String process(String request) {
        String response;
        if(request.equals(GET)){
            URLRequest urlRequest = mongoQueue.dequeue();
            response = urlRequest != null ? urlRequest.getUrl() : "";
        }else if(request.equals(LEN)){
            long len = mongoQueue.len();
            response = String.valueOf(len);
        }else{
            URLRequest urlRequest = new URLRequest(request, System.currentTimeMillis());
            mongoQueue.enqueue(urlRequest);
            response = OK;
        }

        pv.incrementAndGet();
        if(pv.get() % LOG_SEGMENT == 0){
            LOG.debug(PV,pv.get());
        }

        return response;
    }

    @Inject
    public void setMongoQueue(MongoURLQueue mongoQueue) {
        this.mongoQueue = mongoQueue;
    }
}
