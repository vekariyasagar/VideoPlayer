package com.silverorange.videoplayer.Model;

import java.io.Serializable;

public class Author implements Serializable {

    // Model class for author

    String id, name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}