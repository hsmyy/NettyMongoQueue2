package com.fc.queue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by fc on 14-11-24.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String _id;
    private String username;
    private int answer_num;
    private int follower_num;
    private String url;
    private String title;
    private int view_num;
}
