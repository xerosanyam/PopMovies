package com.example.sanyam.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sanyam on 20/3/16.
 */
public class Favorite extends RealmObject {
    @PrimaryKey
    String mid;

    public String getId() {
        return mid;
    }

    public void setId(String id) {
        this.mid = id;
    }
}
