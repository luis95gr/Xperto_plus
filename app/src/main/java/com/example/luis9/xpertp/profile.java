package com.example.luis9.xpertp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class profile extends AppCompatActivity {

    //VARIABLES
    TextView textNombre,textPais,textGenero,textBir,textAltura,textPeso,textCorreo,textCondicion,textId;
    String nombre,pais,genero,bir,altura,peso,correo,condicion,id;
    SharedPreferences sp;
    ImageView animation;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        menu();
        //CAST
        sp = getSharedPreferences("login",MODE_PRIVATE);
        textNombre = findViewById(R.id.text_name);
        textPais = findViewById(R.id.text_country);
        textGenero = findViewById(R.id.text_gender);
        textBir = findViewById(R.id.text_birthday);
        textAltura = findViewById(R.id.text_height);
        textPeso = findViewById(R.id.text_weight);
        textCorreo = findViewById(R.id.text_correo);
        textCondicion = findViewById(R.id.text_condicion);
        textId = findViewById(R.id.text_id);
        animation = findViewById(R.id.animation_view);
        //
        if (sp.getString("gender",null).equalsIgnoreCase("masculino")){
            animation.setImageResource(R.drawable.hombre);
        } else animation.setImageResource(R.drawable.mujer);
        //
        nombre = "<font color=#000000>" + getString(R.string.tNombre) + "<br></font> <font color=#cc0029>" +
                sp.getString("name",null) + "</font>";
        textNombre.setText(Html.fromHtml(nombre));
        //
        pais = "<font color=#000000>" + getString(R.string.tPais) + "<br></font> <font color=#cc0029>" +
                sp.getString("country",null) + "</font>";
        textPais.setText(Html.fromHtml(pais));
        //
        genero = "<font color=#000000>" + getString(R.string.tGenero) + "<br></font> <font color=#cc0029>" +
                sp.getString("gender",null) + "</font>";
        textGenero.setText(Html.fromHtml(genero));
        //
        bir = "<font color=#000000>" + getString(R.string.Edad) + "<br></font> <font color=#cc0029>" +
                age() + getString(R.string.Años) + "</font>";
        textBir.setText(Html.fromHtml(bir));
        //
        altura = "<font color=#000000>" + getString(R.string.tAltura) + "<br></font> <font color=#cc0029>" +
                sp.getInt("height",0) + " cm" + "</font>";
        textAltura.setText(Html.fromHtml(altura));
        //
        peso = "<font color=#000000>" + getString(R.string.tPeso) + "<br></font> <font color=#cc0029>" +
                sp.getInt("weight",0) + " kg" + "</font>";
        textPeso.setText(Html.fromHtml(peso));
        //
        correo = "<font color=#000000>" + getString(R.string.tCorreo) + "<br></font> <font color=#cc0029>" +
                sp.getString("email",null) + "</font>";
        textCorreo.setText(Html.fromHtml(correo));
        //
        condicion = "<font color=#000000>" + getString(R.string.tEjercicio) + "<br></font> <font color=#cc0029>" +
                sp.getInt("exercise",0) + "</font>";
        textCondicion.setText(Html.fromHtml(condicion));
        //
        id = "<font color=#000000>" + getString(R.string.tTrabajador) + "<br></font> <font color=#cc0029>" +
                sp.getString("id",null) + "</font>";
        textId.setText(Html.fromHtml(id));
    }







    //METODOS FUNCIONALES
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
        String stringBirth = sp.getString("birth","Birth");
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
    public void menu() {
        //
        android.support.v7.widget.Toolbar toolbar;
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        String[] titulos = {getString(R.string.tituloPrincipal),getString(R.string.tituloPerfil),getString(R.string.tituloEstadisticas)};
        String[] subTitulos = {getString(R.string.subPrincipal),getString(R.string.subPerfil),getString(R.string.subEstadisticas)};
        int[] images = {R.drawable.outline_directions_car_white_36dp,R.drawable.outline_account_circle_white_36dp,R.drawable.outline_poll_white_36dp};
        final BoomMenuButton bmb;
        bmb = (BoomMenuButton)findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        //bmb.setBackgroundResource(R.drawable.round_border);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(images[i])
                    .normalText(titulos[i])
                    .subNormalText(subTitulos[i]);
            bmb.addBuilder(builder);
        }
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if (index == 0) {
                    startActivity(new Intent(profile.this, main.class));
                }
                if (index == 1){
                    bmb.reboom();
                }
                if (index == 2){
                    Toast.makeText(profile.this, "Coca", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onBackgroundClick() {
            }
            @Override
            public void onBoomWillHide() {
            }
            @Override
            public void onBoomDidHide() {
            }
            @Override
            public void onBoomWillShow() {
            }
            @Override
            public void onBoomDidShow() {
            }
        });
    }
}
