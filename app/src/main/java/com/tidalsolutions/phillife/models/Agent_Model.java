package com.tidalsolutions.phillife.models;

/**
 * Created by SantusIgnis on 23/06/2016.
 */
public class Agent_Model {

    int _id, agent_type;
    Boolean isLogged = false;
    String email, username, employee_no, agent_code, mobile, location, last_login, date_registered, cookie;

    public Boolean getLogged() { return isLogged; }

    public void setLogged(Boolean isLogged) { this.isLogged = isLogged; }

    public int get_agent_type() {
        return agent_type;
    }

    public void set_agent_type(int agent_type) {
        this.agent_type = agent_type;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getDate_registered() {
        return date_registered;
    }

    public void setDate_registered(String date_registered) {
        this.date_registered = date_registered;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
