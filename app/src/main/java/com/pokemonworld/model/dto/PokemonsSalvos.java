package com.pokemonworld.model.dto;

import com.google.gson.annotations.Expose;
import com.pokemonworld.model.Pokemon;

import java.util.List;

/**
 * Created by J. Paulo on 07/05/2017.
 */

public class PokemonsSalvos {
    @Expose
    private List<PokedexRetorno> pokemons;

    public PokemonsSalvos(List<PokedexRetorno> pokemons) {
        this.pokemons = pokemons;
    }

    public List<PokedexRetorno> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<PokedexRetorno> pokemons) {
        this.pokemons = pokemons;
    }
}
