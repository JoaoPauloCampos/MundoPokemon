package com.pokemonworld.utils;

import android.content.Context;

/**
 * Created by J. Paulo on 07/05/2017.
 */

public class DadosPreferences {
    Context context;
    private Preferences prefs;

    public DadosPreferences(Context context) {
        this.context = context;
        prefs = new Preferences(context, "Dados");
    }

    public void salvarPokedex(String valor, String key){
        prefs.salvarString(key, valor);
    }

    public String getPokedex(String key){
        String valorEnc = prefs.getString(key);
        return valorEnc;
    }

    public void salvarPokemon(String valor, String key){
        prefs.salvarString(key, valor);
    }

    public String getPokemon(String key){
        String valorEnc = prefs.getString(key);
        return valorEnc;
    }

    public void salvarListaPoke(String valor, String key){
        prefs.salvarString(key, valor);
    }

    public String getListaPoke(String key){
        String valorEnc = prefs.getString(key);
        return valorEnc;
    }

}
