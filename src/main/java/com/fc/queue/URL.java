package com.fc.queue;

import com.mongodb.DBObject;
import lombok.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by fc on 14-11-1.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class URL {
    private String url;
    private long timestamp;

    private String hash;

    public URL(String url, long timestamp){
        this.url = url;
        this.timestamp = timestamp;
        this.hash = URL.hash(url);
    }

    public URL(DBObject obj){
        this.url = (String)obj.get("url");
        this.timestamp = (Long)obj.get("timestamp");
        this.hash = (String)obj.get("hash");
    }

    public static void main(String[] args){
        Object o = null;
        String s = (String)o;
    }

    public static String hash(String str) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = str.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            return Base64.getEncoder().encodeToString(passHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
