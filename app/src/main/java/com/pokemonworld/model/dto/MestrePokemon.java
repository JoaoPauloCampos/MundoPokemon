package com.pokemonworld.model.dto;

import com.pokemonworld.model.Pokemon;

/**
 * Created by J. Paulo on 07/05/2017.
 */

public class MestrePokemon {
    Pokemon pokemon;
    int id;

    public MestrePokemon(Pokemon pokemon, int id) {
        this.pokemon = pokemon;
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
