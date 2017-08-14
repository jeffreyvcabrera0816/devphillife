package com.tidalsolutions.phillife.models;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class User_Model {


    String firstname, lastname, image_url, local_image_url, employee_no, region, division, station_no, mobile;
    String email, date_created;
    String session_cookie;
    int user_id;
    String gcm;
    String poster_image;

    public String getPoster_image() {
        return poster_image;
    }

    public void setPoster_image(String poster_image) {
        this.poster_image = poster_image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLocal_image_url() {
        return local_image_url;
    }

    public void setLocal_image_url(String local_image_url) {
        this.local_image_url = local_image_url;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getStation_no() {
        return station_no;
    }

    public void setStation_no(String station_no) {
        this.station_no = station_no;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getSession_cookie() {
        return session_cookie;
    }

    public void setSession_cookie(String session_cookie) {
        this.session_cookie = session_cookie;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGcm() {
        return gcm;
    }

    public void setGcm(String gcm) {
        this.gcm = gcm;
    }
}
