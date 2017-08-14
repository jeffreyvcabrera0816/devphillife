package com.tidalsolutions.phillife.models;

import java.io.Serializable;

/**
 * Created by SantusIgnis on 14/09/2016.
 */
public class Pictures_model implements Serializable
{


    int _id, sequence;
    String name, description, forms_hashkey, date, file_path;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForms_hashkey() {
        return forms_hashkey;
    }

    public void setForms_hashkey(String forms_hashkey) {
        this.forms_hashkey = forms_hashkey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
