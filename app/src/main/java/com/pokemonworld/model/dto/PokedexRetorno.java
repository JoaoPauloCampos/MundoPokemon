package com.pokemonworld.model.dto;

import com.google.gson.annotations.Expose;
import com.pokemonworld.model.Pokedex;

import java.util.List;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class PokedexRetorno  {
    @Expose
    private String count;
    @Expose
    private String previous;
    @Expose
    private String next;
    @Expose
    private List<Pokedex> results;

    public PokedexRetorno(String count, String previous, String next, List<Pokedex> results) {
        this.count = count;
        this.previous = previous;
        this.next = next;
        this.results = results;
    }

    public PokedexRetorno() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Pokedex> getResults() {
        return results;
    }

    public void setResults(List<Pokedex> results) {
        this.results = results;
    }
}
