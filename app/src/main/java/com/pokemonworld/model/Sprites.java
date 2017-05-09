package com.pokemonworld.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J. Paulo on 07/05/2017.
 */

public class Sprites implements Serializable{
    @Expose
    String front_default;

    public Sprites(String front_default) {
        this.front_default = front_default;
    }

    public String getFront_default() {
        return front_default;
    }

    public void setFront_default(String front_default) {
        this.front_default = front_default;
    }
}
