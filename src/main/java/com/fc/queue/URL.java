package com.fc.queue;

import com.mongodb.DBObject;
import lombok.*;

/**
 * Created by fc on 14-11-1.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class URL {
    private String url;
    private long timestamp;

    public URL(DBObject obj){
        this.url = (String)obj.get("url");
        this.timestamp = (Long)obj.get("timestamp");
    }
}
