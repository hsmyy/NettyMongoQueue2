package com.fc.server.protocol;

import com.fc.queue.MongoQueue;
import com.fc.queue.URL;
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
    private MongoQueue mongoQueue = new MongoQueue();

    private static AtomicInteger pv = new AtomicInteger(0);

    private Logger LOG = LoggerFactory.getLogger(MongoQueueProtocol.class);



    @Override
    protected String process(String request) {
        String response;
        if(request.equals(GET)){
            URL url = mongoQueue.dequeue();
            response = url != null ? url.getUrl() : "";
        }else if(request.equals(LEN)){
            long len = mongoQueue.len();
            response = String.valueOf(len);
        }else{
            URL url = new URL(request, System.currentTimeMillis());
            mongoQueue.enqueue(url);
            response = OK;
        }

        pv.incrementAndGet();
        if(pv.get() % 100 == 0){
            LOG.debug("pv is " + pv.get());
        }

        return response;
    }
}
