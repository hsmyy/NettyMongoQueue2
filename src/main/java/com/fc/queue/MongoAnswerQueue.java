package com.fc.queue;

import com.fc.queue.model.Answer;

/**
 * Created by fc on 14-11-24.
 */
public class MongoAnswerQueue extends MongoAbstractQueue<Answer>{
    @Override
    public void enqueue(Answer obj) {

    }

    @Override
    public Answer dequeue() {
        return null;
    }

    @Override
    public long len() {
        return 0;
    }
}
