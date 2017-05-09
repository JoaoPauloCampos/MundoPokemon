package com.pokemonworld.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.pokemonworld.R;
import com.pokemonworld.model.Form;
import com.pokemonworld.model.Pokedex;
import com.pokemonworld.model.Pokemon;
import com.pokemonworld.ui.activity.BatalhaActivity;
import com.pokemonworld.utils.Constantes;
import com.pokemonworld.utils.DadosPreferences;
import com.pokemonworld.utils.Util;
import com.pokemonworld.webservice.WebService;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {

    private final Context context;
    private List<Pokedex> itens;

    public PokedexAdapter(Context context, List<Pokedex> itens) {
        this.context = context;
        this.itens = itens;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_pokedex, parent, false);
        return new ViewHolder(view);
    }

    public void refresh(List<Pokedex> itens) {
        this.itens = itens;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Pokedex item = itens.get(position);
        if (!Strings.isNullOrEmpty(itens.get(position).getName())) {
            //String pokemonName =
            holder.name.setText(Util.mudaPrimeiraLetra(itens.get(position).getName()));
        }
        final DadosPreferences dados = new DadosPreferences(context);
        String result = dados.getPokemon(item.getUrl());
        Pokemon pokemon = (Pokemon) WebService.parser(result, Pokemon.class);
        Picasso.with(context).
                load(pokemon.getPhoto()).
                into(holder.icon);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.name.getText(),Toast.LENGTH_SHORT).show();
                Intent it = new Intent(context, BatalhaActivity.class);
                it.putExtra(Constantes.EXTRA_POKEMON, item);
                (context).startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final ImageView icon;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.name);
            icon = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
