package com.pokemonworld.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pokemonworld.R;
import com.pokemonworld.model.Pokedex;
import com.pokemonworld.model.Pokemon;
import com.pokemonworld.model.dto.MestrePokemon;
import com.pokemonworld.model.dto.PokedexRetorno;
import com.pokemonworld.utils.Constantes;
import com.pokemonworld.utils.DadosPreferences;
import com.pokemonworld.utils.Util;
import com.pokemonworld.webservice.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BatalhaActivity extends BaseActivity {

    Context context;
    private List<Pokedex> pokedex = new ArrayList<>();
    private Pokedex pokemonUser;
    private PokedexRetorno retorno;
    private Pokemon pokemonJogador;
    private TextView nomePokemon;
    private TextView altura;
    private TextView peso;
    private TextView level;
    private TextView nomeOponenteUm;
    private TextView alturaOponenteUm;
    private TextView pesoOponenteUm;
    private TextView levelOponenteUm;
    private TextView nomeOponenteDois;
    private TextView alturaOponenteDois;
    private TextView pesoOponenteDois;
    private TextView levelOponenteDois;
    private TextView nomeOponenteTres;
    private TextView alturaOponenteTres;
    private TextView pesoOponenteTres;
    private TextView levelOponenteTres;
    private ImageView imagemPokemon;
    private ImageView imagemOponenteUm;
    private ImageView imagemOponenteDois;
    private ImageView imagemOponenteTres;
    private TextView txtResultadoUm;
    private TextView txtResultadoDois;
    private TextView txtResultadoTres;
    private TextView resultadoUm;
    private TextView resultadoDois;
    private TextView resultadoTres;
    private Button btn_iniciar;
    private boolean havePlaying = false;
    private DadosPreferences dados;
    private Random newInt;
    private List<MestrePokemon> oponenteList = new ArrayList<>();
    private MestrePokemon jogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batalha);
        iniciaCampos();
        recebeExtras();
        dados = new DadosPreferences(getApplicationContext());
        retorno = (PokedexRetorno) WebService.parser(dados.getPokedex(Constantes.SITE), PokedexRetorno.class);
        pokemonJogador = (Pokemon) WebService.parser(dados.getPokedex(pokemonUser.getUrl()), Pokemon.class);
        jogador = new MestrePokemon(pokemonJogador, 100);
        adicionaAtributosJogador(jogador.getPokemon());
        if (retorno.getResults() != null) {
            pokedex = retorno.getResults();
        }
        sorteiaOponentes(pokedex);
        preencherTela();
        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (havePlaying) {
                    finish();
                } else {
                    boolean resultadoBatalha = batalha();
                    if (resultadoBatalha) {
                        btn_iniciar.setBackgroundColor(getResources().getColor(R.color.color_vitoria));
                        btn_iniciar.setText(getString(R.string.jogar_novamente));
                        criaDialog(resultadoBatalha);
                    } else {
                        btn_iniciar.setBackgroundColor(getResources().getColor(R.color.color_derrota));
                        btn_iniciar.setText(getString(R.string.jogar_novamente));
                        criaDialog(resultadoBatalha);
                    }
                }

            }
        });
    }

    public void recebeExtras() {
        if (getIntent().getSerializableExtra(Constantes.EXTRA_POKEMON) != null) {
            pokemonUser = (Pokedex) getIntent().getSerializableExtra(Constantes.EXTRA_POKEMON);
        }
    }

    public void sorteiaOponentes(List<Pokedex> pokedex) {
        for (int cont = 0; cont < 3; cont++) {
            int sort = newInt.nextInt(20);
            String p = dados.getPokemon(pokedex.get(sort).getUrl());
            Pokemon pokee = (Pokemon) WebService.parser(p, Pokemon.class);
            adicionaAtributosOponente(pokee);
            oponenteList.add(cont, new MestrePokemon(pokee, cont));
            //oponentes.add(pokee);
        }

    }

    public void adicionaAtributosJogador(Pokemon pokemon) {
        pokemon.setLevel(newInt.nextInt(100));
        pokemon.setLife(10);
        pokemon.setAgility(newInt.nextInt(100));
        pokemon.setForce(newInt.nextInt(100));
    }

    public void adicionaAtributosOponente(Pokemon pokemon) {
        pokemon.setLevel(newInt.nextInt(60));
        pokemon.setLife(10);
        pokemon.setAgility(newInt.nextInt(60));
        pokemon.setForce(newInt.nextInt(60));
    }

    public boolean batalha() {
        int contadorVitorias = 0;
        boolean batalhaUm;
        boolean batalhaDois;
        boolean batalhaTres;
        batalhaUm = lutar(jogador, oponenteList.get(0));
        batalhaDois = lutar(jogador, oponenteList.get(1));
        batalhaTres = lutar(jogador, oponenteList.get(2));

        if (batalhaUm) {
            contadorVitorias++;
            txtResultadoUm.setVisibility(View.VISIBLE);
            resultadoUm.setText(getString(R.string.vitoria));
            resultadoUm.setTextColor(getResources().getColor(R.color.color_vitoria));
        } else {
            txtResultadoUm.setVisibility(View.VISIBLE);
            resultadoUm.setText(getString(R.string.derrota));
            resultadoUm.setTextColor(getResources().getColor(R.color.color_derrota));
        }

        if (batalhaDois) {
            contadorVitorias++;
            txtResultadoDois.setVisibility(View.VISIBLE);
            resultadoDois.setText(getString(R.string.vitoria));
            resultadoDois.setTextColor(getResources().getColor(R.color.color_vitoria));
        } else {
            txtResultadoDois.setVisibility(View.VISIBLE);
            resultadoDois.setText(getString(R.string.derrota));
            resultadoDois.setTextColor(getResources().getColor(R.color.color_derrota));
        }

        if (batalhaTres) {
            contadorVitorias++;
            txtResultadoTres.setVisibility(View.VISIBLE);
            resultadoTres.setText(getString(R.string.vitoria));
            resultadoTres.setTextColor(getResources().getColor(R.color.color_vitoria));
        } else {
            txtResultadoTres.setVisibility(View.VISIBLE);
            resultadoTres.setText(getString(R.string.derrota));
            resultadoTres.setTextColor(getResources().getColor(R.color.color_derrota));
        }
        havePlaying = true;
        return contadorVitorias == 3;

    }

    public boolean lutar(MestrePokemon um, MestrePokemon dois) {
        int pontosJogador = 0;
        int pontosOponente = 0;

        if (um.getPokemon().getAgility() != dois.getPokemon().getAgility()) {
            if (um.getPokemon().getAgility() > dois.getPokemon().getAgility()) {
                pontosJogador++;
            } else {
                pontosOponente++;
            }
        }
        if (um.getPokemon().getForce() != dois.getPokemon().getForce()) {
            if (um.getPokemon().getForce() > dois.getPokemon().getForce()) {
                pontosJogador++;
            } else {
                pontosOponente++;
            }
        }
        if (!um.getPokemon().getBase_experience().equals(dois.getPokemon().getBase_experience())) {
            if (Integer.parseInt(um.getPokemon().getBase_experience()) > Integer.parseInt(dois.getPokemon().getBase_experience())) {
                pontosJogador++;
            } else {
                pontosOponente++;
            }
        }
        if (um.getPokemon().getLevel() != dois.getPokemon().getLevel()) {
            if (um.getPokemon().getLevel() > dois.getPokemon().getLevel()) {
                pontosJogador++;
            } else {
                pontosOponente++;
            }
        }
        if (um.getPokemon().getWeight().equals(dois.getPokemon().getWeight())) {
            if (Integer.parseInt(um.getPokemon().getWeight()) > Integer.parseInt(dois.getPokemon().getWeight())) {
                pontosJogador++;
            } else {
                pontosOponente++;
            }
        }
        return pontosJogador > pontosOponente;

    }

    public void criaDialog(boolean venceu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BatalhaActivity.this);
        builder.setTitle(getString(R.string.resultado));
        if (venceu) {
            builder.setMessage(getString(R.string.vitoria));
        } else {
            builder.setMessage(getString(R.string.derrota));
        }
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void preencherTela() {
        nomePokemon.setText(Util.mudaPrimeiraLetra(jogador.getPokemon().getName()));
        altura.setText(jogador.getPokemon().getHeight());
        peso.setText(jogador.getPokemon().getWeight());
        level.setText(String.valueOf(jogador.getPokemon().getLevel()));
        Picasso.with(context).
                load(jogador.getPokemon().getPhoto()).
                into(imagemPokemon);

        int indexOponent = 0;
        nomeOponenteUm.setText(Util.mudaPrimeiraLetra(oponenteList.get(indexOponent).getPokemon().getName()));
        alturaOponenteUm.setText(oponenteList.get(indexOponent).getPokemon().getHeight());
        pesoOponenteUm.setText(oponenteList.get(indexOponent).getPokemon().getWeight());
        levelOponenteUm.setText(String.valueOf(oponenteList.get(indexOponent).getPokemon().getLevel()));
        Picasso.with(context).
                load(oponenteList.get(indexOponent).getPokemon().getPhoto()).
                into(imagemOponenteUm);

        indexOponent++;
        nomeOponenteDois.setText(Util.mudaPrimeiraLetra(oponenteList.get(indexOponent).getPokemon().getName()));
        alturaOponenteDois.setText(oponenteList.get(indexOponent).getPokemon().getHeight());
        pesoOponenteDois.setText(oponenteList.get(indexOponent).getPokemon().getWeight());
        levelOponenteDois.setText(String.valueOf(oponenteList.get(indexOponent).getPokemon().getLevel()));
        Picasso.with(context).
                load(oponenteList.get(indexOponent).getPokemon().getPhoto()).
                into(imagemOponenteDois);

        indexOponent++;
        nomeOponenteTres.setText(Util.mudaPrimeiraLetra(oponenteList.get(indexOponent).getPokemon().getName()));
        alturaOponenteTres.setText(oponenteList.get(indexOponent).getPokemon().getHeight());
        pesoOponenteTres.setText(oponenteList.get(indexOponent).getPokemon().getWeight());
        levelOponenteTres.setText(String.valueOf(oponenteList.get(indexOponent).getPokemon().getLevel()));
        Picasso.with(context).
                load(oponenteList.get(indexOponent).getPokemon().getPhoto()).
                into(imagemOponenteTres);
    }

    public void iniciaCampos() {
        nomePokemon = (TextView) findViewById(R.id.nome_pokemon_user);
        altura = (TextView) findViewById(R.id.altura_pokemon_user);
        peso = (TextView) findViewById(R.id.peso_pokemon_user);
        level = (TextView) findViewById(R.id.level_pokemon_user);
        nomeOponenteUm = (TextView) findViewById(R.id.nome_oponente_1);
        alturaOponenteUm = (TextView) findViewById(R.id.altura_oponente_1);
        pesoOponenteUm = (TextView) findViewById(R.id.peso_oponente_1);
        levelOponenteUm = (TextView) findViewById(R.id.level_oponente_1);
        nomeOponenteDois = (TextView) findViewById(R.id.nome_oponente_2);
        alturaOponenteDois = (TextView) findViewById(R.id.altura_oponente_2);
        pesoOponenteDois = (TextView) findViewById(R.id.peso_oponente_2);
        levelOponenteDois = (TextView) findViewById(R.id.level_oponente_2);
        nomeOponenteTres = (TextView) findViewById(R.id.nome_oponente_3);
        alturaOponenteTres = (TextView) findViewById(R.id.altura_oponente_3);
        pesoOponenteTres = (TextView) findViewById(R.id.peso_oponente_3);
        levelOponenteTres = (TextView) findViewById(R.id.level_oponente_3);
        imagemPokemon = (ImageView) findViewById(R.id.img_pokemon);
        imagemOponenteUm = (ImageView) findViewById(R.id.img_oponente_1);
        imagemOponenteDois = (ImageView) findViewById(R.id.img_oponente_2);
        imagemOponenteTres = (ImageView) findViewById(R.id.img_oponente_3);
        txtResultadoUm = (TextView) findViewById(R.id.txt_resultado_um);
        txtResultadoDois = (TextView) findViewById(R.id.txt_resultado_dois);
        txtResultadoTres = (TextView) findViewById(R.id.txt_resultado_tres);
        resultadoUm = (TextView) findViewById(R.id.resultado_oponente_1);
        resultadoDois = (TextView) findViewById(R.id.resultado_oponente_2);
        resultadoTres = (TextView) findViewById(R.id.resultado_oponente_3);
        btn_iniciar = (Button) findViewById(R.id.iniciar_batalha);
        newInt = new Random();
    }

}
