package com.example.luis9.xperto_plus;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xperto_plus.Helo.conexionHeloBluetooth;
import com.example.luis9.xperto_plus.Helo.cuentaHelo;
import com.example.luis9.xperto_plus.estadisticas.estadisticas;
import com.example.luis9.xpertp.R;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.worldgn.connector.Connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class main extends AppCompatActivity {

    TextView textBienvenido,textTituloCard,textDuracion,textFecha,textVeloc,textFatiga;
    Button buttonManejar;
    SharedPreferences spLogin,spDiagnostico;
    FloatingActionButton floatingActionButton;
    PowerManager pm;
    String ip = "http://smarth.xperto.com.mx/mean/recuperajson.php?usuario=";
    String stringId;
    JSONArray jsonArray;
    int fatiga;
    Animation animation;
    ImageView imageView;
    boolean regresar = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        menu();
        //CAST
        spLogin = getSharedPreferences("login", MODE_PRIVATE);
        spDiagnostico = PreferenceManager.getDefaultSharedPreferences(this);
        floatingActionButton = findViewById(R.id.floatingButton);
        textBienvenido = (TextView) findViewById(R.id.textBienvenido);
        textTituloCard = (TextView) findViewById(R.id.textTituloCard);
        textDuracion = findViewById(R.id.textDuracion);
        textFecha = findViewById(R.id.textFecha);
        textVeloc = findViewById(R.id.textVelocidad);
        textFatiga = findViewById(R.id.textFatiga);
        buttonManejar = (Button) findViewById(R.id.buttonManejar);
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        textBienvenido.setTypeface(font);
        textTituloCard.setTypeface(font);
        buttonManejar.setTypeface(font);
        stringId = spLogin.getString("id","id");
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        imageView = findViewById(R.id.imageR);
        //
        textBienvenido.setText(getString(R.string.tBienvenido) +"\n" +spLogin.getString("name","")); //DEJAR ESTE
        //AHORRO DE ENERGIA DESACTIVAR
        pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            Intent intent = new Intent();
            intent.setData(Uri.parse("package:" + getApplication().getPackageName()));
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            startActivityForResult(intent,1);
        }
        //
        textDuracion.setText(spLogin.getString("duracion","0"));
        textFecha.setText(spLogin.getString("fecha","0"));
        textVeloc.setText(spLogin.getString("velocidad","0"));
        textFatiga.setText(String.valueOf(spLogin.getInt("fatiga",0)));
    }

    //BOTONES
    public void manejar(View view){
        SharedPreferences.Editor spDiagnosticoEditor = spDiagnostico.edit();
        spDiagnosticoEditor.putBoolean("diagnosticoR",false);
        spDiagnosticoEditor.apply();
        startActivity(new Intent(main.this, cuentaHelo.class));
        //startActivity(new Intent(main.this, popUpEstadisticas.class));
    }
    public void reload(View view){
        //VolleyPetition(ip + stringId);
        if (internet()) {
            VolleyPetition(ip + stringId);
            imageView.startAnimation(animation);
        } else Toast.makeText(this, "No hay internet", Toast.LENGTH_SHORT).show();
    }
    //
    public void floating(View view){
        SharedPreferences.Editor spDiagnosticoEditor = spDiagnostico.edit();
        spDiagnosticoEditor.putBoolean("diagnosticoR",true);
        spDiagnosticoEditor.apply();
        startActivity(new Intent(main.this, cuentaHelo.class));
    }
    //

    protected void onResume(){
        super.onResume();
    }

    protected void onPause (){
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (pm.isIgnoringBatteryOptimizations(getPackageName())) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main), "DESACTIVADO", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    Snackbar.make(findViewById(R.id.main),"Algunas funcionalidades pueden no estar disponibles",Snackbar.LENGTH_LONG)
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setData(Uri.parse("package:" + getApplication().getPackageName()));
                            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                            startActivityForResult(intent,1);
                        }
                    }).show();
                }

        }
    }
    public void VolleyPetition(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //
                try {
                    jsonArray = new JSONArray(response);
                    String a;
                    a = jsonArray.getString(0);
                    JSONObject jsonObject = new JSONObject(a);
                    textDuracion.setText(jsonObject.getString("duracion"));
                    textFecha.setText(jsonObject.getString("fecha"));
                    textVeloc.setText(jsonObject.getString("velocidad"));
                    fatiga = Integer.parseInt(jsonObject.getString("tired"));
                    fatiga += Integer.parseInt(jsonObject.getString("very_tired"));
                    textFatiga.setText(String.valueOf(fatiga));
                    imageView.clearAnimation();
                    Toast.makeText(main.this, "Actualizado", Toast.LENGTH_SHORT).show();
                } catch (JSONException e){
                    e.printStackTrace();
                }

                //
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
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
                deleteCache(this);
                ((ActivityManager)this.getSystemService(ACTIVITY_SERVICE))
                        .clearApplicationUserData();
                Toast.makeText(this, "Cache eliminado", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(main.this, estadisticas.class));
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
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else if(dir.isFile()) { return dir.delete(); }
        else {
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        if (regresar) super.onBackPressed();
    }

}
