package com.pokemonworld.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.common.base.Strings;
import com.pokemonworld.R;
import com.pokemonworld.component.DividerItemDecoration;
import com.pokemonworld.model.Pokedex;
import com.pokemonworld.model.Pokemon;
import com.pokemonworld.model.dto.PokedexRetorno;
import com.pokemonworld.ui.adapter.PokedexAdapter;
import com.pokemonworld.utils.Constantes;
import com.pokemonworld.utils.DadosPreferences;
import com.pokemonworld.utils.Util;
import com.pokemonworld.webservice.WebService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    Context context = this;
    private List<Pokedex> pokemons = new ArrayList<Pokedex>();
    private PokedexAdapter adapter;
    private RecyclerView pokedexRecyclerView;
    private DadosPreferences dados;
    private EditText edtPesquisa;
    private List<Pokedex> pokemonBuscado = new ArrayList<>();
    private PokedexRetorno retorno;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        iniciaCampos();
        dados = new DadosPreferences(getApplicationContext());
        //VERIFICA SE TODOS POKEMONS JA FORAM EXIBIDOS ANTERIORMENTE
        if (!Strings.isNullOrEmpty(dados.getPokedex(Constantes.POKEMONS_KEY))) {
            retorno = (PokedexRetorno) WebService.parser(dados.getPokedex(Constantes.POKEMONS_KEY), PokedexRetorno.class);
            pokemons = retorno.getResults();
        } else {
            retorno = (PokedexRetorno) WebService.parser(dados.getPokedex(Constantes.SITE), PokedexRetorno.class);
            if (retorno != null) {
                pokemons = retorno.getResults();
                url = retorno.getNext();
                while (!Strings.isNullOrEmpty(url) && !url.contains("60")) {
                    PokedexRetorno ret = (PokedexRetorno) WebService.parser(dados.getPokedex(url), PokedexRetorno.class);
                    if (!Strings.isNullOrEmpty(url)) {
                        url = ret.getNext();
                        pokemons.addAll(ret.getResults());
                    }
                }
                PokedexRetorno pokedexRetorno = new PokedexRetorno();
                pokedexRetorno.setResults(pokemons);
                String p = WebService.unparser(pokedexRetorno);
                dados.salvarPokedex(p, Constantes.POKEMONS_KEY);
            }
        }
        pokedexRecyclerView.setAdapter(adapter);
        adapter.refresh(pokemons);
        edtPesquisa.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarParceiro(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void iniciaCampos() {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, R.drawable.divider);
        pokedexRecyclerView = (RecyclerView) findViewById(R.id.list_pokedex);
        pokedexRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pokedexRecyclerView.addItemDecoration(dividerItemDecoration);
        pokedexRecyclerView.setNestedScrollingEnabled(true);
        adapter = new PokedexAdapter(context, pokemons);
        pokedexRecyclerView.setAdapter(adapter);
        edtPesquisa = (EditText) findViewById(R.id.edt_pesquisa);
    }

    public void buscarParceiro(String texto) {
        texto = tiraEspacoELowerCase(texto);
        pokemonBuscado.clear();
        if (texto.length() > 3) {
            for (int i = 0; i < pokemons.size(); i++) {
                Pokedex item = pokemons.get(i);
                if (!Strings.isNullOrEmpty(item.getName())) {
                    String pokemon = tiraEspacoELowerCase(item.getName());
                    if (pokemon.contains(texto)) {
                        pokemonBuscado.add(pokemons.get(i));
                    }
                }
            }
        }
        if (pokemonBuscado.isEmpty() && texto.length() > 3) {
            adapter.notifyDataSetChanged();
        } else if (pokemonBuscado.isEmpty() && texto.length() <= 3) {
            retorno = (PokedexRetorno) WebService.parser(dados.getPokedex(Constantes.SITE), PokedexRetorno.class);
            pokemons = retorno.getResults();
            adapter = new PokedexAdapter(context, pokemons);
            pokedexRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new PokedexAdapter(context, pokemonBuscado);
            pokedexRecyclerView.setAdapter(adapter);
        }
    }

    public String tiraEspacoELowerCase(String text) {
        text = text.replace(" ", "");
        text = text.toLowerCase();
        return text;
    }

}
