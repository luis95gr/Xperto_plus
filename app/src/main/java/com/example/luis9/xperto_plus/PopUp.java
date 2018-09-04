package com.example.luis9.xperto_plus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.example.luis9.xpertp.R;

import java.io.IOException;
import java.util.Objects;

public class PopUp extends AppCompatActivity {


    //VARIABLES
    int width,height,contadorMedicionesFatiga,contadorFatigaCansado;
    Intent intent;
    TextView textDuracion,textSpeed,textHr,textBr,textFatiga,textMood,textBp;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        //CAST
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
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85),(int)(height*.55));
        //
        //INFORMACION
        //
        textDuracion.setText(intent.getExtras().getString("tiempo"));
        textSpeed.setText(intent.getExtras().getDouble("promedioSpeed") + " km/hr");
        textHr.setText(intent.getExtras().getDouble("promedioHr") + " lpm");
        textBr.setText(intent.getExtras().getDouble("promedioBr") + " rpm");
        textBp.setText(intent.getExtras().getDouble("promedioBpmax") + "/" + intent.getExtras().getDouble("promedioBpmin") + " mmHg");
        textFatiga.setText(intent.getExtras().getInt("contadorCansado") + " veces");
        textMood.setText(intent.getExtras().get("contadorDeprimido") + " veces");
        //
        contadorMedicionesFatiga = intent.getExtras().getInt("contadorMedicionesFatiga");
        contadorFatigaCansado = intent.getExtras().getInt("contadorCansado");
        if (contadorMedicionesFatiga*.3 > contadorFatigaCansado){
            textFatiga.setTextColor(getColor(R.color.md_red_A400));
        }


    }
}
