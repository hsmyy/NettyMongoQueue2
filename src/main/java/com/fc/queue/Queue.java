package com.fc.queue;

/**
 * Created by fc on 14-11-1.
 */
public interface Queue<R> {
    public void enqueue(R obj);
    public R dequeue();
    public long len();
}
