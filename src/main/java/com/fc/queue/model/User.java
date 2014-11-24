package com.fc.queue.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by fc on 14-11-24.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String _id;
    private String username;
    private int ask_num;
    private String update_time;
    private List<String> jobs;
    private String description;
    private int follower_num;
    private String url;
    private String tencentweibo;
    private String industry;
    private int post_num;
    private int view_num;
    private String sex;
    private int collection_num;
    private String sinaweibo;
    private String location;
    private int followee_num;
    private int log_num;
    private List<String> educations;
    private String nickname;
    private int answer_num;

}
