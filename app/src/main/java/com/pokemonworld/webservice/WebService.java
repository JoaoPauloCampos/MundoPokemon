package com.pokemonworld.webservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.pokemonworld.R;
import com.pokemonworld.model.TransacaoEnvio;
import com.pokemonworld.utils.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class WebService extends AsyncTask<String, Void, String> {

    public static final int POST = 1;
    public static final int DELETE = 2;
    public static final int GET = 3;
    public static final int PUT = 4;
    public static final String WS_POST = "POST";
    public static final String WS_DELETE = "DELETE";
    public static final String WS_GET = "GET";
    public static final String WS_PUT = "PUT";
    private static final int READ_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 60000;
    private final String URL_STRING;
    private final TransacaoEnvio transacaoEnvio;
    private final Activity context;
    public Exception exception;
    private String requesMehod = "";
    private int metodo;

    public WebService(String URL_STRING, TransacaoEnvio transacaoEnvio, Activity context, int metodo) {
        this.URL_STRING = URL_STRING;
        this.transacaoEnvio = transacaoEnvio;
        this.context = context;
        this.metodo = metodo;
    }

    public WebService(String URL_STRING, TransacaoEnvio transacaoEnvio, Activity context, int metodo, boolean showDialog) {
        this.URL_STRING = URL_STRING;
        this.transacaoEnvio = transacaoEnvio;
        this.context = context;
        this.metodo = metodo;
    }


    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            result = doWebservice();
        } catch (Exception e) {
            exception = e;
        }
        return result;
    }

    private String doWebservice() throws Exception {
        String result;
        if (Strings.isNullOrEmpty(requesMehod)) {
            switch (metodo) {
                case POST:
                    requesMehod = "POST";
                    break;
                case GET:
                    requesMehod = "GET";
                    break;
                case PUT:
                    requesMehod = "PUT";
                    break;
                case DELETE:
                    requesMehod = "DELETE";
                    break;
                default:
                    break;
            }
        }
        result = doMethod();
        Log.d("Webservice", "Result: " + result);
        return result;
    }

    private String doMethod() throws Exception {
        String result;
        try {
            if (Util.isConnectionAvailable(context)) {
                InputStream inputStream;
                String host;
                host = URL_STRING;
                Thread.currentThread().setName(URL_STRING);
                URL url = new URL(host);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(CONNECT_TIMEOUT);
                con.setReadTimeout(READ_TIMEOUT);

                con.setRequestMethod(requesMehod);

                Log.d("Webservice", "URL : " + host);
                Log.d("Webservice", "RequestMethod: " + requesMehod);

                con.setRequestProperty("Content-type", "application/json;charset=UTF-8");

                if (transacaoEnvio != null) {
                    con.setDoOutput(true);
                    setOutput(con);
                }
                con.connect();
                inputStream = con.getInputStream();
                Log.d("Webservice", "Response code: " + con.getResponseCode());
                if (!url.getHost().equals(con.getURL().getHost())) {
                    // we were redirected! Kick the user out to the browser to sign on?
                    Util.iniciaUrl(context, con.getURL().getHost());
                    throw new RedeNaoDisponivelException(context.getString(R.string.msg_erro_sem_conexao));
                }
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    inputStream.close();
                } else {
                    result = "";
                }
                con.disconnect();
            } else {
                throw new RedeNaoDisponivelException(context.getString(R.string.msg_erro_sem_conexao));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    private void setOutput(HttpURLConnection con) throws Exception {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        String json = gson.toJson(transacaoEnvio);
        Log.d("Webservice", "JSON " + json);
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
        writer.write(json.trim());
        writer.flush();
        writer.close();
        os.close();
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }

    public static <T> Object parser(String result, Type type) {
        T mensagensRetorno = null;
        if ((result != null) && (!result.equals(""))) {
            try {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                        .create();
                //TODO tirar após testes
                JsonReader reader = new JsonReader(new StringReader(result));
                reader.setLenient(false);
                mensagensRetorno = gson.fromJson(reader, type);
                //mensagensRetorno = gson.fromJson(result, type);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                Log.d("WebServer", "parser error: " + e.getMessage());
                return null;
            }
        }
        return mensagensRetorno;
    }

    public static String unparser(Object objeto) {
        String json;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        json = gson.toJson(objeto);
        return json;
    }

}
