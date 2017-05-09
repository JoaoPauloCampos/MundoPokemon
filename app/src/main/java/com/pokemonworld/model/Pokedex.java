package com.pokemonworld.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class Pokedex implements Serializable {
    @Expose
    String url;
    @Expose
    String name;

    public Pokedex(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
