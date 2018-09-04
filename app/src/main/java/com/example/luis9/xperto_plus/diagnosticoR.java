package com.example.luis9.xperto_plus;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luis9.xpertp.R;
import com.worldgn.connector.Connector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class diagnosticoR extends AppCompatActivity {

    //VARIABLES
    Button iniciar;
    TextView textFatiga,textHr,textBr,textBp,textMood,textHrDiagn,textBrDiagn,textBpDiagn,textBle,textMidiendo;
    android.support.v7.widget.LinearLayoutCompat tabla;
    CountDownTimer countDownTimerIniciar,countDownTimerMedir;
    boolean booleanIniciar = false;
    ProgressBar progressBar;
    //MEDICIONES
    CountDownTimer countDownTimerHr,countDownTimerBp,countDownTimerBr,countDownTimerMF;
    Date date;
    SimpleDateFormat sdfHour,sdfDate;
    String stringDate,stringHour;
    IntentFilter intentFilter;
    MeasurementReceiver heloMeasurementReceiver;
    String max,min,br,fatigue,hr,mood,steps;
    Snackbar snackbar;
    Chronometer chronometer;
    //
    public final String BROADCAST_ACTION_BP_MEASUREMENT = "com.worldgn.connector_plus.BP_MEASUREMENT";
    public static final String BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE = "com.worldgn.connector_plus.MEASURE_WRITE_FAILURE";
    public static final String BROADCAST_ACTION_HR_MEASUREMENT = "com.worldgn.connector_plus.HR_MEASUREMENT";
    public static final String BROADCAST_ACTION_BR_MEASUREMENT = "com.worldgn.connector_plus.BR_MEASUREMENT";
    public static final String BROADCAST_ACTION_FATIGUE_MEASUREMENT = "com.worldgn.connector_plus.FATIGUE_MEASUREMENT";
    public static final String BROADCAST_ACTION_MOOD_MEASUREMENT = "com.worldgn.connector_plus.MOOD_MEASUREMENT";
    public static final String BROADCAST_ACTION_STEPS_MEASUREMENT = "com.worldgn.connector_plus.STEPS_MEASUREMENT";
    public static final String BROADCAST_ACTION_HELO_DISCONNECTED = "com.worldgn.connector_plus.ACTION_HELO_DISCONNECTED";
    public static final String BROADCAST_ACTION_HELO_CONNECTED = "com.worldgn.connector_plus.ACTION_HELO_CONNECTED";
    public static final String BROADCAST_ACTION_HELO_BONDED = "com.worldgn.connector_plus.ACTION_HELO_BONDED";
    //
    public static final String INTENT_KEY_HR_MEASUREMENT = "HR_MEASUREMENT";
    public static final String INTENT_KEY_BR_MEASUREMENT = "BR_MEASUREMENT";
    public static final String INTENT_KEY_BP_MEASUREMENT_MAX = "BP_MEASUREMENT_MAX";
    public static final String INTENT_KEY_BP_MEASUREMENT_MIN = "BP_MEASUREMENT_MIN";
    public static final String INTENT_KEY_MOOD_MEASUREMENT = "MOOD_MEASUREMENT";
    public static final String INTENT_KEY_FATIGUE_MEASUREMENT = "FATIGUE_MEASUREMENT";
    public static final String INTENT_MEASUREMENT_WRITE_FAILURE = "MEASUREMENT_WRITE_FAILURE";
    public static final String INTENT_KEY_STEPS_MEASUREMENT = "STEPS_MEASUREMENT";
    //AVF
    AdapterViewFlipper AVF;
    int[] images = {R.drawable.i1,R.drawable.i2,R.drawable.i3};
    String[] names = {"Coloca la pulsera ajustada y a 5 cm de tu muñeca", "Realiza las pruebas sentado y cómodo",
    "Respira tranquilo y profundo durante 15 segundos antes de empezar"};
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostico_r);
        menu();
        startcountDownTimerIniciar();
        //CAST
        textFatiga = findViewById(R.id.textFatiga);
        textHr = findViewById(R.id.textHr);
        textBr = findViewById(R.id.textBr);
        textBp = findViewById(R.id.textBp);
        textHrDiagn = findViewById(R.id.textHrDiagn);
        textBrDiagn = findViewById(R.id.textBrDiagn);
        textBpDiagn = findViewById(R.id.textBpDiagn);
        textMood = findViewById(R.id.textMood);
        textBle = findViewById(R.id.textBle);
        textMidiendo = findViewById(R.id.textMidiendo);
        iniciar = findViewById(R.id.iniciar);
        tabla = findViewById(R.id.tabla);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
        chronometer = findViewById(R.id.chronometer);
        //AVF CAST
        AVF = findViewById(R.id.AVF);
        CustomAdapterImage customAdapter = new CustomAdapterImage(getApplicationContext(),names,images);
        AVF.setAdapter(customAdapter);
        AVF.setFlipInterval(5000);
        AVF.startFlipping();
        //
        //HELO CAST
        intentFilter = new IntentFilter();
        heloMeasurementReceiver = new MeasurementReceiver();
        intentFilter.addAction(BROADCAST_ACTION_STEPS_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BP_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_BR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_FATIGUE_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_MOOD_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_HR_MEASUREMENT);
        intentFilter.addAction(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE);
        intentFilter.addAction(BROADCAST_ACTION_HELO_DISCONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_CONNECTED);
        intentFilter.addAction(BROADCAST_ACTION_HELO_BONDED);
        intentFilter.addAction("com.worldgn.w22.ble.BluetoothLeService.ACTION_MAIN_DATA_BP");
        //CHECK CONNECTION
        Connector.getInstance().getStepsData();
        //
    }

    public void iniciar(View view){
        if (booleanIniciar) {
            AVF.stopFlipping();
            AVF.setVisibility(View.INVISIBLE);
            tabla.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            textMidiendo.setVisibility(View.VISIBLE);
            iniciar.setEnabled(false);
            startcountDownTimerMediciones();
            progressBar.animate();
            chronometer();
            //
            Connector.getInstance().measureMF();
            textMidiendo.setText("Midiendo: " + getString(R.string.FatigaSin));
            textBp.setText("");
            textMood.setText("");
            textFatiga.setText("");
            textBr.setText("");
            textHr.setText("");
            startcountDownTimerHr();
            //
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.diagnosticoR), "Espera a ver las instrucciones", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    //////////////////////MEDICIONES TIMERS////////////////////////////////////////////
    //COUNTDOWN MEASURES
    public void startcountDownTimerHr(){
        countDownTimerHr = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureHR();
                //TEXTS
                textMidiendo.setText("Midiendo: " + getString(R.string.RitmoSin));
                startcountDownTimerBp();
            }
        }.start();
    }
    public void startcountDownTimerBp(){
        countDownTimerBp = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureBP();
                //TEXTS
                textMidiendo.setText("Midiendo: " + getString(R.string.PresionSin));
                startcountDownTimerBr();
            }
        }.start();
    }
    public void startcountDownTimerBr(){
        countDownTimerBr = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Connector.getInstance().measureBr();
                //TEXTS
                textMidiendo.setText("Midiendo: " + getString(R.string.RespiracionSin));
                startcountDownTimerMF();
            }
        }.start();
    }
    public void startcountDownTimerMF(){
        countDownTimerMF = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                countDownTimerHr.cancel();
                countDownTimerBp.cancel();
                countDownTimerMF.cancel();
                countDownTimerBr.cancel();
            }
        }.start();
    }
    //


    public class MeasurementReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {
                //PRESION
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    Log.i("service123","entro a broadcast");
                    max = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    min = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textBp.setText(max + "/" + min);
                        }
                    });
                    //RESPIRACION
                } else if (intent.getAction().equals("com.worldgn.w22.ble.BluetoothLeService.ACTION_MAIN_DATA_BP")){
                    Log.i("service123","entro a com.worldn");

                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {
                    br = intent.getStringExtra(INTENT_KEY_BR_MEASUREMENT);
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textBr.setText(br);
                        }
                    });
                    //FATIGA Y ANIMO
                } else if (intent.getAction().equals(BROADCAST_ACTION_FATIGUE_MEASUREMENT)) {
                    fatigue = intent.getStringExtra(INTENT_KEY_FATIGUE_MEASUREMENT);
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (fatigue.equalsIgnoreCase("tired")) textFatiga.setText("Cansado");
                            if (fatigue.equalsIgnoreCase("Very_Tired")) textFatiga.setText("Muy cansado");
                            if (fatigue.equalsIgnoreCase("normal")) textFatiga.setText("Normal");
                        }
                    });
                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_MOOD_MEASUREMENT)) {
                    mood = intent.getStringExtra(INTENT_KEY_MOOD_MEASUREMENT);
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mood.equalsIgnoreCase("Calm")) textMood.setText("Calmado");
                            if (mood.equalsIgnoreCase("Excitement")) textMood.setText("Emocionado");
                            if (mood.equalsIgnoreCase("Depression")) textMood.setText("Deprimido");
                        }
                    });
                } else if (intent.getAction().equals(BROADCAST_ACTION_HR_MEASUREMENT)) {
                    hr = intent.getStringExtra(INTENT_KEY_HR_MEASUREMENT);
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textHr.setText(hr);
                        }
                    });
                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_MEASUREMENT_WRITE_FAILURE)) {
                    String message = intent.getStringExtra(INTENT_MEASUREMENT_WRITE_FAILURE);
                    Toast.makeText(diagnosticoR.this, message, Toast.LENGTH_LONG).show();
                    //
                } else if (intent.getAction().equals(BROADCAST_ACTION_STEPS_MEASUREMENT)) {
                    steps = intent.getStringExtra(INTENT_KEY_STEPS_MEASUREMENT);
                    if (!steps.isEmpty()){
                        textBle.setText(R.string.Conectado);
                        textBle.setTextColor(Color.parseColor("#90EE90"));
                    }
                } else if (intent.getAction().equals(BROADCAST_ACTION_HELO_DISCONNECTED)) {
                    textBle.setText(getString(R.string.tConexionDesconectado));
                    textBle.setTextColor(Color.parseColor("#F44336"));
                    //
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(heloMeasurementReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(heloMeasurementReceiver);
    }

    //METODOS UTILES
    public void startcountDownTimerIniciar() {
        countDownTimerIniciar = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                booleanIniciar = true;
                countDownTimerIniciar.cancel();
            }
        }.start();
    }

    public void startcountDownTimerMediciones() {
        countDownTimerMedir = new CountDownTimer(250000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setMax(100);
                long pr = Math.abs(((millisUntilFinished / 1000) * 100000 / 250000)-100);
                int progress = (int) pr;
                progressBar.setProgress(progress);
            }
            @Override
            public void onFinish() {
                countDownTimerMedir.cancel();
                chronometer.stop();
                textMidiendo.setText("TERMINADO");
                iniciar.setEnabled(true);
            }
        }.start();
    }

    public void chronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void menu (){
        android.support.v7.widget.Toolbar toolbar;
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
