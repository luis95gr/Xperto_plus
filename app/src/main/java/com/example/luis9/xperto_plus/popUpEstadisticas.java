package com.example.luis9.xperto_plus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.luis9.xpertp.R;

import java.util.Objects;

public class popUpEstadisticas extends AppCompatActivity {

    TextView textHr,textBp,textBr;
    int width,height;
    Intent intent;
    SharedPreferences spDiagnostic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_estadisticas);
        //STRUCTURE
        android.support.v7.widget.Toolbar toolbar;
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85),(int)(height*.40));
        //CAST
        spDiagnostic = PreferenceManager.getDefaultSharedPreferences(this);
        intent = getIntent();
        textHr = findViewById(R.id.textHr);
        textBp = findViewById(R.id.textPresion);
        textBr = findViewById(R.id.textRespiracion);
        //INFORMACION
        textHr.setText("Ritmo: " + spDiagnostic.getString("diagnosticoHr","diagnostico"));
        textBr.setText("Respiración: " + spDiagnostic.getString("diagnosticoBr","diagnostico"));
        textBp.setText("Presión: " + spDiagnostic.getString("diagnosticoBp","diagnostico"));
    }

    public void cerrar(View view){
        finish();
    }
}
