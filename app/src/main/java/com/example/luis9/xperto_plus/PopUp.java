package com.example.luis9.xperto_plus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xpertp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class PopUp extends AppCompatActivity {


    //VARIABLES
    int width,height,contadorMedicionesFatiga,contadorFatigaCansado;
    Intent intent;
    TextView textDuracion,textSpeed,textHr,textBr,textFatiga,textMood,textBp;
    SharedPreferences spLogin;
    String stringEmail,stringID,stringDate,stringHour,tiempo,viaje,horaInicio;
    Date date;
    SimpleDateFormat sdfDate,sdfHour;
    DecimalFormat decimalFormat = new DecimalFormat("##");
    String promedioSpeed,promedioHr,promedioBr,promedioBpmax,promedioBpmin;
    int contadorCansado,contadorDeprimido,contadorCansadoEnviar,contadorMuyCansadoEnviar,contadorNormalEnviar,contadorCalmadoEnviar,
    contadorDeprimidoEnviar,contadorEmocionadoEnviar;
    String ip = "http://smarth.xperto.com.mx/mean/registramean.php?hr=";

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        //CAST
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        textDuracion = findViewById(R.id.textDuracion2);
        textSpeed = findViewById(R.id.textVelocPro2);
        textHr = findViewById(R.id.textHrProm2);
        textBr = findViewById(R.id.textBrProm2);
        textFatiga = findViewById(R.id.textFatigaProm2);
        textMood = findViewById(R.id.textMoodProm2);
        textBp = findViewById(R.id.textBpProm2);
        intent = getIntent();
        //STRUCTURE
        android.support.v7.widget.Toolbar toolbar;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width * .85), (int) (height * .60));
        //
        //INFORMACION
        stringEmail = spLogin.getString("email", null);
        stringID = spLogin.getString("id", null);
        //
        tiempo = intent.getExtras().getString("tiempo");
        promedioSpeed = decimalFormat.format(intent.getExtras().getDouble("promedioSpeed"));
        Log.i("service123",promedioSpeed);
        promedioHr = decimalFormat.format(intent.getExtras().getDouble("promedioHr"));
        promedioBr = decimalFormat.format(intent.getExtras().getDouble("promedioBr"));
        promedioBpmax = decimalFormat.format(intent.getExtras().getDouble("promedioBpmax"));
        promedioBpmin = decimalFormat.format(intent.getExtras().getDouble("promedioBpmin"));
        contadorCansado = intent.getExtras().getInt("contadorCansado");
        contadorDeprimido = intent.getExtras().getInt("contadorDeprimido");
        horaInicio = intent.getExtras().getString("horaInicio");
        //
        contadorCansadoEnviar = intent.getExtras().getInt("contadorCansadoEnviar");
        contadorMuyCansadoEnviar = intent.getExtras().getInt("contadorMuyCansadoEnviar");
        contadorNormalEnviar = intent.getExtras().getInt("contadorNormalEnviar");
        contadorCalmadoEnviar = intent.getExtras().getInt("contadorCalmadoEnviar");
        contadorDeprimidoEnviar = intent.getExtras().getInt("contadorDeprimidoEnviar");
        contadorEmocionadoEnviar = intent.getExtras().getInt("contadorEmocionadoEnviar");
        //
        textDuracion.setText(tiempo);
        textSpeed.setText(promedioSpeed + " km/hr");
        textHr.setText(promedioHr + " lpm");
        textBr.setText(promedioBr + " rpm");
        textBp.setText(promedioBpmax + "/" + promedioBpmin + " mmHg");
        textFatiga.setText(contadorCansado + " veces");
        textMood.setText(contadorDeprimido + " veces");
        //
        contadorMedicionesFatiga = intent.getExtras().getInt("contadorMedicionesFatiga");
        contadorFatigaCansado = intent.getExtras().getInt("contadorCansado");
        if (contadorMedicionesFatiga * .3 > contadorFatigaCansado) {
            textFatiga.setTextColor(getColor(R.color.md_red_A400));
        }

        viaje = ip + promedioHr + "&usuario=" + stringID + "&br=" + promedioBr + "&bpmax=" + promedioBpmax + "&fecha=" + dates() + "&horainicio=" + horaInicio
                + "&horafin=" + hour() + "&velocidad=" + promedioSpeed + "&duracion=" + tiempo + "&bpmin=" + promedioBpmin + "&tired=" + contadorCansadoEnviar +
                "&very_tired=" + contadorMuyCansadoEnviar + "&calm=" + contadorCalmadoEnviar + "&normal=" + contadorNormalEnviar + "&excitement=" +
                contadorEmocionadoEnviar + "&depression=" + contadorDeprimidoEnviar;
    }

    public void cerrar (View view){
        if (internet()) {
            //FALTA HORA INICIO
            VolleyPetition(viaje);
        } else {
            String path;
            try {
                path = Environment.getExternalStorageDirectory() + "/SmartHealthcareViajes";
                File myDir = new File(path);
                myDir.mkdirs();
                File file = new File(myDir, hour() + "-" + dates() + stringID + stringEmail);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(viaje.getBytes());
                fos.close();
                Toast.makeText(this, "No hay internet, guardado en dispositivo", Toast.LENGTH_LONG).show();
                finish();
            } catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
    }


    private void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PopUp.this, "Guardado", Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PopUp.this, "Error!" , Toast.LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
    }

    public String dates(){
        date = Calendar.getInstance().getTime();
        sdfDate = new SimpleDateFormat("yyyy.MM.dd");
        stringDate = sdfDate.format(date);
        return stringDate;
    }
    public String hour(){
        date = Calendar.getInstance().getTime();
        sdfHour = new SimpleDateFormat("kk:mm:ss");
        stringHour = sdfHour.format(date);
        return stringHour;
    }

    protected boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    //


}
