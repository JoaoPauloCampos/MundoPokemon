package com.pokemonworld.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by J. Paulo on 07/05/2017.
 */

public class Preferences {
    private SharedPreferences prefs;
    public Preferences(Context context, String name)
    {
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public void salvarString(String nome, String valor)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(nome, valor);
        editor.commit();
    }

    public String getString(String nome)
    {
        String valor = prefs.getString(nome, "");
        return valor;
    }

    public void salvarInt(String nome, int valor)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(nome, valor);
        editor.commit();
    }

    public Integer getInt(String nome)
    {
        Integer valor = prefs.getInt(nome, 0);
        return valor;
    }

    public void salvarBool(String nome, boolean valor)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(nome, valor);
        editor.commit();
    }

    public boolean getBool(String nome)
    {
        boolean valor = prefs.getBoolean(nome, false);
        return valor;
    }

}
