package com.fc.queue;

import com.fc.queue.model.Question;

/**
 * Created by fc on 14-11-24.
 */
public class MongoQuestionQueue extends MongoAbstractQueue<Question>{
    @Override
    public void enqueue(Question obj) {

    }

    @Override
    public Question dequeue() {
        return null;
    }

    @Override
    public long len() {
        return 0;
    }
}
