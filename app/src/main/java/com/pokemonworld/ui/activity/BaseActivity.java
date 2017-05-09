package com.pokemonworld.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.pokemonworld.R;
import com.pokemonworld.component.ProgressDialogCustom;
import com.pokemonworld.utils.Constantes;
import com.pokemonworld.webservice.RedeNaoDisponivelException;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class BaseActivity extends AppCompatActivity {
    protected Dialog dialog;

    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        if (isRunning(context))
            dialog.show();
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    public void showProgressDialog(Context context, View view, int exibir) {

        switch (exibir) {
            case Constantes.PROGRESS_EXIBIR_LOADING:
                if (dialog == null) {
                    dialog = ProgressDialogCustom.ProgressDialog(context);
                } else {
                    dialog.dismiss();
                    dialog = ProgressDialogCustom.ProgressDialog(context);
                }
                break;
        }
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(ctx.getPackageName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void tratarExectionToast(Exception e) {
        String msg;
        if (e instanceof RedeNaoDisponivelException) {
            msg = e.getMessage();
        } else {
            if (e instanceof SocketTimeoutException) {
                msg = getString(R.string.msg_erro_servidor_timeout);
            } else {
                msg = getString(R.string.msg_erro_resultado_nao_carregado);
            }
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}