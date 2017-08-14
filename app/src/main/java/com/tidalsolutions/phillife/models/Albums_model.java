package com.tidalsolutions.phillife.models;

import java.io.Serializable;

/**
 * Created by SantusIgnis on 14/09/2016.
 */
public class Albums_model implements Serializable {

    int _id;
    int agent_id;
    int is_draft;
    int status;


    int page_count;
    String form_id;
    String client_name;
    String name;
    String description;
    String date_created;
    String date_submitted;
    String date_rejected;
    String date_approved;
    String rejected_description;
    String date_updated;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    String note;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getIs_draft() {
        return is_draft;
    }

    public void setIs_draft(int is_draft) {
        this.is_draft = is_draft;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
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

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_submitted() {
        return date_submitted;
    }

    public void setDate_submitted(String date_submitted) {
        this.date_submitted = date_submitted;
    }

    public String getDate_rejected() {
        return date_rejected;
    }

    public void setDate_rejected(String date_rejected) {
        this.date_rejected = date_rejected;
    }

    public String getDate_approved() {
        return date_approved;
    }

    public void setDate_approved(String date_approved) {
        this.date_approved = date_approved;
    }

    public String getRejected_description() {
        return rejected_description;
    }

    public void setRejected_description(String rejected_description) {
        this.rejected_description = rejected_description;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
}
