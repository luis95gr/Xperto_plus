package com.example.luis9.xperto_plus;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xpertp.R;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class serviceInternet extends Service {
    public serviceInternet() {
    }

    //VARIABLES
    StringTokenizer tokenDataBpmax,tokenDataBpmin,tokenDataBr,tokenDataMood,tokenDataFatigue,tokenDataHr = null;
    //
    String[] stringTokenBpmax = new String[20];
    String[] stringTokenBpmin = new String[20];
    String[] stringTokenBr = new String[20];
    String[] stringTokenMood = new String[20];
    String[] stringTokenFatigue = new String[20];
    String[] stringTokenHr = new String[20];
    //
    String[] stringBpmaxSaved = new String[20];
    String[] stringBpminSaved = new String[20];
    String[] stringBrSaved = new String[20];
    String[] stringMoodSaved = new String[20];
    String[] stringFatigueSaved = new String[20];
    String[] stringHrSaved = new String[20];
    //
    String stringEmail,stringPass,stringID;
    SharedPreferences spMeasuresSaved,spLogin;
    int contBp,contBr,contMood,contFatigue,contHr;
    int contTotal = 0;
    Timer timer = new Timer();
    boolean booleanTermino;
    String ip = "http://smarth.xperto.com.mx/registrovar/index.php?usuario=";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        Log.i("service123","empezo");
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        //SHARED CAST
        spMeasuresSaved = PreferenceManager.getDefaultSharedPreferences(this);
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        //CAST
        stringEmail = spLogin.getString("email",null);
        stringPass = spLogin.getString("pass",null);
        stringID = spLogin.getString("id",null);

        booleanTermino = false;
        contBp = spMeasuresSaved.getInt("contBp", 0);
        contBr = spMeasuresSaved.getInt("contBr",0);
        contMood = spMeasuresSaved.getInt("contMood",0);
        contFatigue = spMeasuresSaved.getInt("contFatigue",0);
        contHr = spMeasuresSaved.getInt("contHr",0);
        Log.i("service123","CONTBP:" +contBp);
        Log.i("service123","CONTBR:" +contBr);
        Log.i("service123","CONTMOOD:" +contMood);
        Log.i("service123","CONTFATIGUE:" +contFatigue);
        Log.i("service123","CONTHR:" +contHr);
        //
        //
        //RECEIVE DATA
        ///////BP//////
        for (int i = 1; i <= contBp; i++) {
            stringBpmaxSaved[i] = spMeasuresSaved.getString("stringBpmaxSaved" + i, "0");
            stringBpminSaved[i] = spMeasuresSaved.getString("stringBpminSaved" + i, "0");
        }
        ///////BR//////
        for (int j = 1; j <= contBr; j++) {
            stringBrSaved[j] = spMeasuresSaved.getString("stringBrSaved" + j, "0");
        }
        ///////MOOD//////
        for (int k = 1; k <= contMood; k++) {
            stringMoodSaved[k] = spMeasuresSaved.getString("stringMoodSaved" + k, "0");
        }
        ///////FATIGUE//////
        for (int m = 1; m <= contFatigue; m++) {
            stringFatigueSaved[m] = spMeasuresSaved.getString("stringFatigueSaved" + m, "0");
        }
        //////HR/////////////
        for (int z = 1; z <= contHr; z++) {
            stringHrSaved[z] = spMeasuresSaved.getString("stringHrSaved" + z , "0");
        }

        //SPLIT CONTENT
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (internet() && !booleanTermino) {
                    for (int q = 1; q <= contBp; q++) {
                        tokenDataBpmax = new StringTokenizer(stringBpmaxSaved[q], ",");
                        tokenDataBpmin = new StringTokenizer(stringBpminSaved[q], ",");
                        //
                        stringTokenBpmax[1] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[2] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[3] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[4] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[5] = tokenDataBpmax.nextToken();
                        stringTokenBpmax[6] = tokenDataBpmax.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenBpmax[1] + "&valor=" + stringTokenBpmax[2] + "&fecha=" +
                                stringTokenBpmax[3] + "&hora=" + stringTokenBpmax[4] + "&velocidad=" + stringTokenBpmax[5] + "&diagnostico="
                                + stringTokenBpmax[6]);
                        //
                        stringTokenBpmin[1] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[2] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[3] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[4] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[5] = tokenDataBpmin.nextToken();
                        stringTokenBpmin[6] = tokenDataBpmin.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenBpmin[1] + "&valor=" + stringTokenBpmin[2] + "&fecha=" +
                                stringTokenBpmin[3] + "&hora=" + stringTokenBpmin[4] + "&velocidad=" + stringTokenBpmin[5] + "&diagnostico="
                                + stringTokenBpmin[6]);
                        //
                    }
                    //
                    for (int s = 1; s <= contBr; s++) {
                        tokenDataBr = new StringTokenizer(stringBrSaved[s], ",");
                        //
                        stringTokenBr[1] = tokenDataBr.nextToken();
                        stringTokenBr[2] = tokenDataBr.nextToken();
                        stringTokenBr[3] = tokenDataBr.nextToken();
                        stringTokenBr[4] = tokenDataBr.nextToken();
                        stringTokenBr[5] = tokenDataBr.nextToken();
                        stringTokenBr[6] = tokenDataBr.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenBr[1] + "&valor=" + stringTokenBr[2] + "&fecha=" +
                                stringTokenBr[3] + "&hora=" + stringTokenBr[4] + "&velocidad=" + stringTokenBr[5] + "&diagnostico=" +
                                stringTokenBr[6]);
                    }
                    //
                    for (int d = 1; d <= contMood; d++) {
                        tokenDataMood = new StringTokenizer(stringMoodSaved[d], ",");
                        //
                        stringTokenMood[1] = tokenDataMood.nextToken();
                        stringTokenMood[2] = tokenDataMood.nextToken();
                        stringTokenMood[3] = tokenDataMood.nextToken();
                        stringTokenMood[4] = tokenDataMood.nextToken();
                        stringTokenMood[5] = tokenDataMood.nextToken();
                        stringTokenMood[6] = tokenDataMood.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenMood[1] + "&valor=" + stringTokenMood[2] + "&fecha=" +
                                stringTokenMood[3] + "&hora=" + stringTokenMood[4] + "&velocidad=" + stringTokenMood[5] + "&diagnostico=" +
                                stringTokenMood[6]);
                    }
                    //
                    for (int f = 1; f <= contFatigue; f++) {
                        tokenDataFatigue = new StringTokenizer(stringFatigueSaved[f], ",");
                        //
                        stringTokenFatigue[1] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[2] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[3] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[4] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[5] = tokenDataFatigue.nextToken();
                        stringTokenFatigue[6] = tokenDataFatigue.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenFatigue[1] + "&valor=" + stringTokenFatigue[2] + "&fecha=" +
                                stringTokenFatigue[3] + "&hora=" + stringTokenFatigue[4] + "&velocidad=" + stringTokenFatigue[5]
                                + "&diagnostico=" + stringTokenFatigue[6]);
                    }
                    for (int g = 1; g <= contHr; g++) {
                        tokenDataHr = new StringTokenizer(stringHrSaved[g], ",");
                        //
                        stringTokenHr[1] = tokenDataHr.nextToken();
                        stringTokenHr[2] = tokenDataHr.nextToken();
                        stringTokenHr[3] = tokenDataHr.nextToken();
                        stringTokenHr[4] = tokenDataHr.nextToken();
                        stringTokenHr[5] = tokenDataHr.nextToken();
                        stringTokenHr[6] = tokenDataHr.nextToken();
                        VolleyPetition(ip + stringID + "&var=" + stringTokenHr[1] + "&valor=" + stringTokenHr[2] + "&fecha=" + stringTokenHr[3]
                                + "&hora=" + stringTokenHr[4] + "&velocidad=" + stringTokenHr[5] + "&diagnostico=" + stringTokenHr[6]);
                    }
                }
            }
        },0,5000);
        return START_NOT_STICKY;

    }

    private void VolleyPetition(String URL) {
        if (!booleanTermino) {
            Log.i("url", "" + URL);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //DATA SENT
                    contTotal++;
                    Log.i("service123", "ENVIADOS:" + contTotal);
                    if (contTotal == contBp * 2 + contBr + contMood + contFatigue + contHr) {
                        contBp = contBr = contMood = contFatigue = contHr = 0;
                        Log.i("service123", "CONTOTAL:" + contTotal);
                        booleanTermino = true;
                        contTotal = 0;
                        timer.cancel();
                        stopSelf();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            });
            queue.add(stringRequest);
        }
    }

    private void sendBroadcast (){
        Intent intent = new Intent ("enviado");
        intent.putExtra("enviado", true);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sendBroadcast();
        Log.i("service123","Servicio destruido");
    }


    private boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
