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
import android.preference.PreferenceManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xpertp.R;
import com.worldgn.connector.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class diagnosticoR extends AppCompatActivity {

    //VARIABLES
    SharedPreferences spLogin,spDiagnostico;
    Button iniciar;
    TextView textFatiga,textHr,textBr,textBp,textMood,textHrDiagn,textBrDiagn,textBpDiagn,textBle,textMidiendo,detalles;
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
    //DIAGNOSTICOS
    Double doubleMaxHr,doubleMinHr;
    String stringDiagnosticHr,stringDiagnosticBr,stringDiagnosticBp;
    int intNormalBrmin,intNormalBpmax,intNormalBpmin,intNormalBrmax;
    //
    //ENVIAR
    String ip = "http://smarth.xperto.com.mx/mean/registraquick.php?usuario=";
    //
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
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        spDiagnostico = PreferenceManager.getDefaultSharedPreferences(this);
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
        detalles = findViewById(R.id.detalles);
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

    public void detalles(View view){
        startActivity(new Intent(diagnosticoR.this, popUpEstadisticas.class));
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
            iniciar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.md_red_A400)));
            detalles.setVisibility(View.INVISIBLE);
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
            SharedPreferences.Editor spDiagnosticoEditor = spDiagnostico.edit();
            if (intent != null && intent.getAction() != null) {
                //PRESION
                if (intent.getAction().equals(BROADCAST_ACTION_BP_MEASUREMENT)) {
                    Log.i("service123","entro a broadcast");
                    max = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MAX);
                    min = intent.getStringExtra(INTENT_KEY_BP_MEASUREMENT_MIN);
                    spDiagnosticoEditor.putString("diagnosticoBp",bpDiagnostic(Integer.parseInt(max),Integer.parseInt(min)));
                    spDiagnosticoEditor.apply();
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textBp.setText(max + "/" + min);
                            if (bpDiagnostic(Integer.parseInt(max),Integer.parseInt(min)).contains("normal")){
                                textBpDiagn.setText("Bien");
                                textBpDiagn.setTextColor(Color.parseColor("#90EE90"));
                            } else {
                                textBpDiagn.setText("Cuidado");
                                textBpDiagn.setTextColor(getColor(R.color.md_yellow_300));
                            }
                        }
                    });
                    //RESPIRACION
                } else if (intent.getAction().equals("com.worldgn.w22.ble.BluetoothLeService.ACTION_MAIN_DATA_BP")){
                    Log.i("service123","entro a com.worldn");

                } else if (intent.getAction().equals(BROADCAST_ACTION_BR_MEASUREMENT)) {
                    br = intent.getStringExtra(INTENT_KEY_BR_MEASUREMENT);
                    spDiagnosticoEditor.putString("diagnosticoBr",brDiagnostic(Integer.parseInt(br)));
                    spDiagnosticoEditor.apply();
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (brDiagnostic(Integer.parseInt(br)).contains("normal")){
                                textBrDiagn.setText("Bien");
                                textBrDiagn.setTextColor(Color.parseColor("#90EE90"));
                            } else {
                                textBrDiagn.setText("Cuidado");
                                textBrDiagn.setTextColor(getColor(R.color.md_yellow_300));
                            }
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
                    spDiagnosticoEditor.putString("diagnosticoHr",hrDiagnostic(Integer.parseInt(hr)));
                    spDiagnosticoEditor.apply();
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textHr.setText(hr);

                            if (hrDiagnostic(Integer.parseInt(hr)).contains("normal") || hrDiagnostic(Integer.parseInt(hr)).contains("Ligera")){
                                textHrDiagn.setTextColor(Color.parseColor("#90EE90"));
                                textHrDiagn.setText("Bien");
                            } else {
                                textHrDiagn.setText("Cuidado");
                                textHrDiagn.setTextColor(getColor(R.color.md_yellow_300));
                            }
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

    private void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.diagnosticoR), "Guardado", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(diagnosticoR.this, "Error!" , Toast.LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
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
                iniciar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("0099ff")));
                detalles.setVisibility(View.VISIBLE);
                VolleyPetition(ip + spLogin.getString("id","id") + "&hr=" + hr + "&fatiga=" + fatigue + "&mood=" + mood +
                    "&br=" + br + "&bpmax=" + max + "&bpmin" + min + "&hora=" + hour() + "&fecha=" + dates());
            }
        }.start();
    }


    ////////DIAGNOSTICOS
    ///HR
    protected String hrDiagnostic(int intMeasureHr){
        //StringdiagnosticHr = "Normal";
        if (spLogin.getString("gender","Masculino").equalsIgnoreCase("Masculino")) {
            doubleMaxHr = 203.7 / (1 + Math.exp(0.033 * (age() - 104.3)));
        }
        if (spLogin.getString("gender","Masculino").equalsIgnoreCase("Femenino")) {
            doubleMaxHr = 190.2 / (1 + Math.exp(0.0453 * (age() - 107.5)));
        }
        doubleMinHr = -5.4*((double)(spLogin.getInt("exInt",1)))+69.2;
        //
        //OUT OF RANGE
        if (intMeasureHr < doubleMinHr || intMeasureHr > doubleMaxHr){
            if (intMeasureHr < doubleMinHr){
                stringDiagnosticHr = "Bradicardia";
            }
            if (intMeasureHr > doubleMaxHr){
                stringDiagnosticHr = "Taquicardia";
            }
        }
        //NORMAL RANGE
        if (intMeasureHr <= doubleMaxHr-90 && intMeasureHr >= doubleMinHr+10 ){
            stringDiagnosticHr = "Valor normal";
        }
        //BRADICARDIA
        if (intMeasureHr > doubleMinHr && intMeasureHr < doubleMinHr+10){
            stringDiagnosticHr = "Ligera bradicardia";
        }
        //TAQUICARDIA
        if (intMeasureHr < doubleMaxHr && intMeasureHr > doubleMaxHr-90){
            stringDiagnosticHr = "Ligera taquicardia";
        }
        //
        return stringDiagnosticHr;
    }
    //
    //BR
    protected String brDiagnostic(int intMeasureBr){
        if (age() >= 18){
            intNormalBrmin = 12;
            intNormalBrmax = 20;
        }
        //HIGH NORMAL BREATH RATE
        if (intMeasureBr <= intNormalBrmax && intMeasureBr >= intNormalBrmax-1){
            stringDiagnosticBr = "Frecuencia respiratoria alta normal";
        }
        //NORMAL BREATH RATE
        if (intMeasureBr <= intNormalBrmax-2 && intMeasureBr >= intNormalBrmin+2){
            stringDiagnosticBr = "Frecuencia respiratoria normal";
        }
        //LOW NORMAL BREATH RATE
        if (intMeasureBr <= intNormalBrmin+1 && intMeasureBr >= intNormalBrmin){
            stringDiagnosticBr = "Frecuencia respiratoria baja normal";
        }
        //TACHYPNEA
        if (intMeasureBr > intNormalBrmax && intMeasureBr <= intNormalBrmax+5) {
            stringDiagnosticBr = "Taquipnea ligera";
        }
        if (intMeasureBr >= intNormalBrmax+6){
            stringDiagnosticBr = "Taquipnea severa";
        }
        //BRADIPNEA
        if (intMeasureBr < intNormalBrmin && intMeasureBr >= intNormalBrmin-5){
            stringDiagnosticBr = "Bradipnea ligera";
        }
        if (intMeasureBr <= intNormalBrmin-6){
            stringDiagnosticBr = "Bradipnea severa";
        }
        //
        return stringDiagnosticBr;
    }
    //
    //BP
    protected String bpDiagnostic(int intMeasureBpmax,int intMeasureBpmin){
        //CHECK BP SYSTOLIC VALUES
        if(20 <= age() && age()<= 24) {
            intNormalBpmax = 120;
            intNormalBpmin =79;
        }
        if (25 <= age() && age()<= 29){
            intNormalBpmax = 121;
            intNormalBpmin =80;
        }
        if (30 <= age() && age()<= 34){
            intNormalBpmax = 122;
            intNormalBpmin =81;
        }
        if (35 <= age() && age()<= 39){
            intNormalBpmax = 123;
            intNormalBpmin =82;
        }
        if (40 <= age() && age()<= 44){
            intNormalBpmax = 125;
            intNormalBpmin =83;
        }
        if (45 <= age() && age()<= 49){
            intNormalBpmax = 127;
            intNormalBpmin =84;
        }
        if (50 <= age() && age()<= 54){
            intNormalBpmax = 129;
            intNormalBpmin =85;
        }
        if (55 <= age() && age()<= 59){
            intNormalBpmax = 131;
            intNormalBpmin =86;
        }
        /////NORMAL VALUE//////////
        if(intMeasureBpmax >= intNormalBpmax-29 && intMeasureBpmax <= intNormalBpmax+19 &&
                intMeasureBpmin >= intNormalBpmin-19 && intMeasureBpmin <= intNormalBpmin+9) {
            //HIGH NORMAL VALUE
            if (intMeasureBpmax > intNormalBpmax && intMeasureBpmin > intNormalBpmin){
                stringDiagnosticBp = "Alta presion normal";
            }
            //NORMAL VALUE
            if (intMeasureBpmax == intNormalBpmax && intMeasureBpmin == intNormalBpmin){
                stringDiagnosticBp = "Presion normal";
            }
            //LOW NORMAL VALUE
            if (intMeasureBpmax < intNormalBpmax && intMeasureBpmin < intNormalBpmin){
                stringDiagnosticBp = "Baja presion normal";
            }
            if (intMeasureBpmax > intNormalBpmax && intMeasureBpmin < intNormalBpmin){
                if (intMeasureBpmax - intNormalBpmax > intNormalBpmin - intMeasureBpmin){
                    stringDiagnosticBp = "Alta presion normal";
                } else stringDiagnosticBp = "Baja presion normal";

            }
        }
        //////LOW VALUE///////
        if (intMeasureBpmax <= intNormalBpmax-30 || intMeasureBpmin <= intNormalBpmin-20){
            //DANGEROUSLY LOW VALUE
            if (intMeasureBpmax <= intNormalBpmax-70 || intMeasureBpmin <= intNormalBpmin-47){
                stringDiagnosticBp = "Presion peligrosamente baja";
            }
            //TOO LOW VALUE
            if (intMeasureBpmax <= intNormalBpmax-60 && intMeasureBpmax >= intNormalBpmax-69 ||
                    intMeasureBpmin <= intNormalBpmin-40 && intMeasureBpmin >= intNormalBpmin-46){
                stringDiagnosticBp = "Presion muy baja";
            }
            //BORDER LOW VALUE
            if (intMeasureBpmax <= intNormalBpmax-30 && intMeasureBpmax >= intNormalBpmax-59 ||
                    intMeasureBpmin <= intNormalBpmin-20 && intMeasureBpmin >= intNormalBpmin-39){
                stringDiagnosticBp = "Limite de presion baja";
            }
        }
        //////HIGH VALUE/////
        if (intMeasureBpmax >= intNormalBpmax+20 && intMeasureBpmin >= intNormalBpmin+10){
            //STAGE 1
            if (intMeasureBpmax < intNormalBpmax+40 && intMeasureBpmax >= intNormalBpmax+20 ||
                    intMeasureBpmin <= intNormalBpmin+19 && intMeasureBpmin >= intNormalBpmin+10){
                stringDiagnosticBp = "Hipertension nivel 1";
            }
            //STAGE 2
            if (intMeasureBpmax < intNormalBpmax+60 && intMeasureBpmax >= intNormalBpmax+40 ||
                    intMeasureBpmin <= intNormalBpmin+29 && intMeasureBpmin >= intNormalBpmin+20){
                stringDiagnosticBp = "Hipertension nivel 2";
            }
            //STAGE 3
            if (intMeasureBpmax < intNormalBpmax+90 && intMeasureBpmax >= intNormalBpmax+60 ||
                    intMeasureBpmin <= intNormalBpmin+39 && intMeasureBpmin >= intNormalBpmin+30){
                stringDiagnosticBp = "Hipertension nivel 3";
            }
            //STAGE 4
            if (intMeasureBpmax >= intNormalBpmax+90 || intMeasureBpmin >= intNormalBpmin+40){
                stringDiagnosticBp = "Hipertension nivel 4";
            }
        }
        //ISOLATED VALUES
        //HIGH ISOLATED SYSTOLIC
        if (intMeasureBpmax >= intNormalBpmax+20 && intMeasureBpmax <= intNormalBpmax+39 &&
                intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipertension sistolica aislada nivel 1";
        }
        if (intMeasureBpmax >= intNormalBpmax+40 && intMeasureBpmax <= intNormalBpmax+59 &&
                intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipertension sistolica aislada nivel 2";
        }
        if (intMeasureBpmax >= intNormalBpmax+60 && intMeasureBpmax <= intNormalBpmax+89 &&
                intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipertension sistolica aislada nivel 3";
        }
        if (intMeasureBpmax >= intNormalBpmax+90 && intMeasureBpmin>= intNormalBpmin-19 && intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipertension sistolica aislada nivel 4";
        }
        //LOW ISOLATED SYSTOLIC
        if (intMeasureBpmax <= intNormalBpmax-30 && intMeasureBpmax >= intNormalBpmax-59 &&
                intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Limite de hipotension sistolica aislada";
        }
        if (intMeasureBpmax <= intNormalBpmax-60 && intMeasureBpmax >= intNormalBpmax-69 &&
                intMeasureBpmin>= intNormalBpmin-19 &&intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipotension sistolica aislada muy baja";
        }
        if (intMeasureBpmax <= intNormalBpmax-70  && intMeasureBpmin>= intNormalBpmin-19 &&
                intMeasureBpmin<= intNormalBpmin+9){
            stringDiagnosticBp = "Hipotension sistolica peligrosa";
        }
        //
        return stringDiagnosticBp;
    }


    ///////////////////////////
    public int age() {
        //YEAR,MONTH,DAY
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
        String stringYear = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("MM");
        String stringMonth = sdfDate.format(date);
        sdfDate = new SimpleDateFormat("dd");
        String stringDay = sdfDate.format(date);
        int intYearActual = Integer.parseInt(stringYear);
        int intMonthActual = Integer.parseInt(stringMonth);
        int intDayActual = Integer.parseInt(stringDay);
        //
        //BIRTH
        String stringBirth = spLogin.getString("birth","Birth");
        StringTokenizer tokenBirth = new StringTokenizer(stringBirth, "-");
        //TOKENS
        String tokenYear = tokenBirth.nextToken();
        String tokenMonth = tokenBirth.nextToken();
        String tokenDay = tokenBirth.nextToken();
        int year = Integer.parseInt(tokenYear);
        int month = Integer.parseInt(tokenMonth);
        int day = Integer.parseInt(tokenDay);
        //
        //CALCULATE AGE
        int ageYears = intYearActual - year;
        //
        if (intMonthActual-month < 0) {
            ageYears = ageYears-1;
            if(intDayActual-day< 0){
                ageYears = ageYears-1;
            }
        }

        return ageYears;
    }

    public void chronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public String dates(){
        date = Calendar.getInstance().getTime();
        sdfDate = new SimpleDateFormat("yyyy.MM.dd");
        stringDate = sdfDate.format(date);
        return stringDate;
    }
    public String hour(){
        date = Calendar.getInstance().getTime();
        sdfHour = new SimpleDateFormat("HH:mm:ss");
        stringHour = sdfHour.format(date);
        return stringHour;
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
