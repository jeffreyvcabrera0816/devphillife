package com.tidalsolutions.phillife.models;

/**
 * Created by SantusIgnis on 14/09/2016.
 */
public class Clients_model {

    int _id;
    String client_name, client_hash_key, date;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_hash_key() {
        return client_hash_key;
    }

    public void setClient_hash_key(String client_hash_key) {
        this.client_hash_key = client_hash_key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
