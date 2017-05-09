package com.pokemonworld.model.dto;

import com.google.gson.annotations.Expose;
import com.pokemonworld.model.Sprites;

/**
 * Created by J. Paulo on 08/05/2017.
 */

public class ImagemPokemonRetorno {
    @Expose
    private Sprites sprites;

    public ImagemPokemonRetorno(Sprites sprites) {
        this.sprites = sprites;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }
}
