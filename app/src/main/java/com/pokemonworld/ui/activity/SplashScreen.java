package com.pokemonworld.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.pokemonworld.R;
import com.pokemonworld.model.Form;
import com.pokemonworld.model.Pokedex;
import com.pokemonworld.model.Pokemon;
import com.pokemonworld.model.dto.ImagemPokemonRetorno;
import com.pokemonworld.model.dto.PokedexRetorno;
import com.pokemonworld.utils.Constantes;
import com.pokemonworld.utils.DadosPreferences;
import com.pokemonworld.webservice.WebService;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends BaseActivity {
    private String url;
    private String urlDetalhePokemon;
    private TextView text_loading;
    private ProgressBar dialog_text;
    private Button btn_iniciar;
    private boolean solicitadoConexao = false;
    private int index = 0;
    private PokedexRetorno pokes;
    private List<Pokedex> pokemonPokedex = new ArrayList<>();
    private Dialog dialog;
    private String next;
    private DadosPreferences dados;
    private List<Form> listaPoke = new ArrayList<>();
    private int indexListaPoke = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        iniciaCampos();
        btn_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //dados = new DadosPreferences(getApplicationContext());
        if (!Strings.isNullOrEmpty(dados.getPokedex(Constantes.SITE))
                && !Strings.isNullOrEmpty(dados.getPokemon(Constantes.SITE + "1/"))) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            if (isOnline(SplashScreen.this)) {
                url = Constantes.SITE;
                dialog = progressDialog(this);
                dialog.show();
                loadPokedex(url);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("Serviços de internet não ativados");
                builder.setMessage("Ative o serviço de conexão com internet");
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (solicitadoConexao) {
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_volte_mais_tarde), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.msg_conectar_internet), Toast.LENGTH_LONG).show();
                            solicitadoConexao = true;
                            onResume();
                        }
                    }
                });
                builder.setPositiveButton("Ligar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
                Dialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }

    }

    public void iniciaCampos() {
        text_loading = (TextView) findViewById(R.id.text_loading);
        btn_iniciar = (Button) findViewById(R.id.btn_iniciar);
        dialog_text = (ProgressBar) findViewById(R.id.progress_splash);
        dados = new DadosPreferences(getApplicationContext());
    }

    public static boolean isOnline(final Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            if (!connected) {

                return connected;
            }
        } catch (Exception e) {
            Toast.makeText(context, "CheckConnectivity Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return connected;
    }

    private void loadPokedex(String url) {
        text_loading.setText(getString(R.string.msg_sincronizar));
        new WebService(url, null, this, WebService.GET) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    checkResult(result);
                } else {
                    tratarExectionToast(exception);
                }
            }
        }.execute();
    }

    private void checkResult(String result) {
        PokedexRetorno retorno = (PokedexRetorno) WebService.parser(result, PokedexRetorno.class);
        if (retorno != null) {
            if (retorno.getResults() != null) {
                //dados = new DadosPreferences(getApplicationContext());
                if (!retorno.getResults().isEmpty()) {
                    pokemonPokedex = retorno.getResults();
                    if (retorno.getPrevious() != null) {
                        dados.salvarPokedex(result, url);
                    } else {
                        dados.salvarPokedex(result, Constantes.SITE);
                    }
                    //TODO REMOVER ESTE IF PARA TRAZER TODOS
                    if (!retorno.getNext().contains("60")) {
                        //if (!Strings.isNullOrEmpty(retorno.getNext())) {
                        url = retorno.getNext();
                        loadPokedex(url);
                    } else {
                        pokemonPokedex.clear();
                        PokedexRetorno ret = (PokedexRetorno) WebService.parser(dados.getPokedex(Constantes.SITE), PokedexRetorno.class);
                        next = ret.getNext();
                        pokemonPokedex = ret.getResults();
                        urlDetalhePokemon = pokemonPokedex.get(index).getUrl();
                        loadPokemon(urlDetalhePokemon);
                    }
                }

            }
        }
    }

    private void loadPokemon(String url) {
        new WebService(url, null, this, WebService.GET) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    checkResultPokemon(result);
                } else {
                    tratarExectionToast(exception);
                }

            }
        }.execute();
    }

    private void checkResultPokemon(String result) {
        Pokemon object = (Pokemon) WebService.parser(result, Pokemon.class);
        if (object != null) {
            if (object != null) {
                listaPoke.add(indexListaPoke, new Form(object.getForms().get(indexListaPoke).getUrl(), urlDetalhePokemon));
                //final DadosPreferences dados = new DadosPreferences(getApplicationContext());
                dados.salvarPokemon(result, urlDetalhePokemon);
                index++;
                if (index < pokemonPokedex.size()) {
                    urlDetalhePokemon = pokemonPokedex.get(index).getUrl();
                    loadPokemon(urlDetalhePokemon);
                } else {
                    String res = dados.getPokedex(next);
                    if (!Strings.isNullOrEmpty(res) && !next.contains("60")) {
                        PokedexRetorno ret2 = (PokedexRetorno) WebService.parser(res, PokedexRetorno.class);
                        next = ret2.getNext();
                        index = 0;
                        pokemonPokedex.clear();
                        pokemonPokedex = ret2.getResults();
                        urlDetalhePokemon = pokemonPokedex.get(index).getUrl();
                        loadPokemon(urlDetalhePokemon);
                    } else {
                        index = 0;
                        String listPok = WebService.unparser(listaPoke);
                        dados.salvarListaPoke(listPok, Constantes.LISTA_POKE);
                        loadImgPokemon(listaPoke.get(index).getUrl());
                    }
                }
                /*if (Strings.isNullOrEmpty(next)) {
                    text_loading.setText(getString(R.string.msg_sincronizado));
                    dialog_text.setVisibility(View.GONE);
                    dialog.dismiss();
                }*/
            }
        }
    }

    private void loadImgPokemon(String url) {
        new WebService(url, null, this, WebService.GET) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    checkResultImgPokemon(result);
                } else {
                    tratarExectionToast(exception);
                }

            }
        }.execute();
    }

    private void checkResultImgPokemon(String result) {
        ImagemPokemonRetorno object = (ImagemPokemonRetorno) WebService.parser(result, ImagemPokemonRetorno.class);
        if (object != null) {
            if (object != null) {
                String rst = dados.getPokemon(listaPoke.get(index).getName());
                Pokemon pkm = (Pokemon) WebService.parser(rst, Pokemon.class);
                pkm.setPhoto(object.getSprites().getFront_default());
                String novoPokemon = WebService.unparser(pkm);
                dados.salvarPokemon(novoPokemon, listaPoke.get(index).getName());
                index++;
                if (index < listaPoke.size()) {
                    loadImgPokemon(listaPoke.get(index).getUrl());
                }else {
                    text_loading.setText(getString(R.string.msg_sincronizado));
                    dialog_text.setVisibility(View.GONE);
                    btn_iniciar.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        }
    }
}
