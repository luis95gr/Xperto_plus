package com.example.luis9.xpertp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.luis9.xpertp.Helo.cuentaHelo;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.Iterator;

public class main extends AppCompatActivity {

    TextView textBienvenido,textTituloCard;
    Button buttonManejar;
    SharedPreferences spLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        menu();
        setupWindowAnimations();
        //CAST
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        textBienvenido = (TextView) findViewById(R.id.textBienvenido);
        textTituloCard = (TextView) findViewById(R.id.textTituloCard);
        buttonManejar = (Button) findViewById(R.id.buttonManejar);
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        textBienvenido.setTypeface(font);
        textTituloCard.setTypeface(font);
        buttonManejar.setTypeface(font);
        //
        textBienvenido.setText(getString(R.string.tBienvenido) +"\n" +spLogin.getString("name","")); //DEJAR ESTE
        ArrayList<Integer> intBr = new ArrayList<>();
        intBr.add(10);
        intBr.add(5);
        intBr.add(5);
        intBr.add(10);
        double promedio;
        Iterator<Integer> iterator = intBr.iterator();
        int element = 0;
        while(iterator.hasNext()){
            element += iterator.next();
        }
        promedio = (double) element/intBr.size();
        Toast.makeText(this, ""+promedio, Toast.LENGTH_SHORT).show();
        Log.i("service123","SUMA" + element);
        Log.i("service123","TAMAÑO" + intBr.size());
        Log.i("service123","PROMEDIO" + promedio);
    }

    //BOTONES
    public void manejar(View view){
        startActivity(new Intent(main.this, cuentaHelo.class));
        //startActivity(new Intent(main.this, PopUp.class));

    }
    //


    protected void onResume(){
        super.onResume();
    }

    protected void onPause (){
        super.onPause();
    }









    //MENU 3 DOTS
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.acercaNos:
                Toast.makeText(this, "Xperto App", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout:
                SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.apply();
                SharedPreferences spLogin = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor spLoginEditor = spLogin.edit();
                spLoginEditor.putBoolean("success",false);
                spLoginEditor.apply();
                startActivity(new Intent(main.this,login.class));
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    //END MENU 3 DOTS





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
                    bmb.reboom();
                }
                if (index == 1){
                    startActivity(new Intent(main.this, profile.class));
                }
                if (index == 2){
                    Toast.makeText(main.this, "Coca", Toast.LENGTH_SHORT).show();
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
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK ? true : super.onKeyDown(keyCode, event));
    }

}
