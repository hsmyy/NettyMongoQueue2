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
public class Answer {
    private String _id;
    private int agree_num;
    private String ask_title;
    private String ask_url;
    private int comment_num;
    private String content;
    private String summary;
    private String url;
    private String username;
}
