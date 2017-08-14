package com.tidalsolutions.phillife.models;

import java.util.ArrayList;

/**
 * Created by SantusIgnis on 12/07/2016.
 */
public class Community_Model {

    int id, notifications, isHidden;
    String username, fullname;
    String title;
    String detail;
    int thread_user_id;
    int comment_id;
    String poster_image;
    String post_image;

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPoster_image() {
        return poster_image;
    }

    public void setPoster_image(String poster_image) {
        this.poster_image = poster_image;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getThread_user_id() {
        return thread_user_id;
    }

    public void setThread_user_id(int thread_user_id) {
        this.thread_user_id = thread_user_id;
    }

    String date;
    ArrayList<Community_Model>community_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<Community_Model> getCommunity_list() {
        return community_list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCommunity_list(ArrayList<Community_Model> community_list) {
        this.community_list = community_list;
    }
}
