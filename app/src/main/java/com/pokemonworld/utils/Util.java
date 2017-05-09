package com.pokemonworld.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class Util {

    public static boolean isConnectionAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static String formataUrl(String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            return "http://" + url;
        } else {
            return url;
        }
    }

    public static void iniciaUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(formataUrl(url)));
        context.startActivity(intent);
    }

    public static String mudaPrimeiraLetra(String text){
        text = Character.toString(text.charAt(0)).toUpperCase()+text.substring(1);
        return text;
    }

}
