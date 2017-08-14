package com.tidalsolutions.phillife.models;

/**
 * Created by SantusIgnis on 24/06/2016.
 */
public class SimpleImageTextMenuModel {

    String text; int id;

    public SimpleImageTextMenuModel(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public SimpleImageTextMenuModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
