package com.example.luis9.xperto_plus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xpertp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity implements TextToSpeech.OnInitListener {

    //VARIABLES
    SharedPreferences spLogin;
    EditText etEmail,etPass;
    Button btnLog;
    String ip = "smarth.xperto.com.mx/sesioncel/index.php?";
    TextView txtCreateA;
    JSONArray jsonArray;
    int acceso=0;
    LottieAnimationView animationView;
    TextToSpeech mTts;
    MediaPlayer mediaPlayer;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setupWindowAnimations();
        //CAST
        animationView = (LottieAnimationView)findViewById(R.id.animation_view);
        animationView.setMinAndMaxFrame(0,20);
        animationView.playAnimation();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLog);
        txtCreateA = (TextView) findViewById(R.id.txtCreateA);
        mediaPlayer = MediaPlayer.create(this,R.raw.login);
        //
        //LOGIN AUTO
        spLogin = getSharedPreferences("login",MODE_PRIVATE);
        if(spLogin.contains("email") && spLogin.contains("pass")){
            startActivity(new Intent(login.this,main.class));
            finish();   //finish current activity
        }
        //END LOGIN AUTO
    }

    @Override
    public void onInit(int status) {
        mTts = new TextToSpeech(this, this);
    }



    //BUTTONS
    public void logins(View view){
        if (!internet()){
            Toast.makeText(this, "Paired device not found", Toast.LENGTH_LONG).show();
        }
        VolleyPetition("http://"+ ip +"correo=" + etEmail.getText().toString()
                + "&password=" +etPass.getText().toString());
        //VolleyPetition2("http://smarth.xperto.com.mx/mean/recuperajson.php?usuario=" + spLogin.getString("id","id"));
        VolleyPetition2("http://smarth.xperto.com.mx/mean/recuperajson.php?usuario=" + "2");
    }
    public void create(View view){
        Intent i = new Intent(getApplicationContext(), createAccount.class);
        startActivity(i);
    }
    //
    public void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    GlobalVars g = (GlobalVars)getApplication();
                    g.setId(jsonArray.getString(1));
                    g.setPass(jsonArray.getString(2));
                    g.setName(jsonArray.getString(3));
                    g.setEmail(jsonArray.getString(4));
                    g.setTel(jsonArray.getString(5));
                    g.setGender(jsonArray.getString(6));
                    g.setWeight(jsonArray.getInt(7));
                    g.setHeight(jsonArray.getInt(8));
                    g.setBirth(jsonArray.getString(9));
                    g.setCountry(jsonArray.getString(10));
                    g.setEjercicio(jsonArray.getInt(11));
                    //
                    if (etEmail.getText().toString().equals(g.getEmail()) &
                            etPass.getText().toString().equals(g.getPass())) {
                        //mTts.speak("Conectado",TextToSpeech.QUEUE_FLUSH,null,"hi");
                        mediaPlayer.start();
                        //
                        SharedPreferences.Editor spLoginEditor = spLogin.edit();
                        spLoginEditor.putString("email",etEmail.getText().toString());
                        spLoginEditor.putString("pass",etPass.getText().toString());
                        spLoginEditor.putString("name",g.getName());
                        spLoginEditor.putString("country",g.getCountry());
                        spLoginEditor.putString("imagen",g.getImage());
                        spLoginEditor.putString("birth",g.getBirth());
                        spLoginEditor.putString("gender",g.getGender());
                        spLoginEditor.putInt("weight",g.getWeight());
                        spLoginEditor.putInt("height",g.getHeight());
                        spLoginEditor.putString("id",g.getID());
                        spLoginEditor.putBoolean("connected",true);
                        spLoginEditor.putInt("exercise",g.getEjercicio());
                        spLoginEditor.apply();
                        //
                        Toast.makeText(getApplicationContext(), "Bienvenido " + g.getName(), Toast.LENGTH_SHORT).show();
                        LottieAnimationView animationView = (LottieAnimationView)findViewById(R.id.animation_view);
                        animationView.setSpeed(2f);
                        animationView.playAnimation(20,100);
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                //
                                Intent intent = new Intent(login.this,main.class);
                                startActivity(intent);
                            }
                        },1500);
                    } else {
                        Toast.makeText(getApplicationContext(), "Contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public void VolleyPetition2(String URL) {
        Log.i("service123","volley2");
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
                    SharedPreferences.Editor spLoginEditor = spLogin.edit();
                    spLoginEditor.putString("velocidad",jsonObject.getString("velocidad"));
                    spLoginEditor.putString("fecha",jsonObject.getString("fecha"));
                    spLoginEditor.putString("duracion",jsonObject.getString("duracion"));
                    int fatiga;
                    fatiga = Integer.parseInt(jsonObject.getString("tired"));
                    fatiga += Integer.parseInt(jsonObject.getString("very_tired"));
                    spLoginEditor.putInt("fatiga",fatiga);
                    Log.i("service123",fatiga+"");
                    spLoginEditor.apply();
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

    public void onPause (){
        super.onPause();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mieditor = datos.edit();
        //mieditor.putString("correo",cuenta);
        //mieditor.putString("contraseña",contraseña);
        mieditor.putInt("acceso",acceso);
        mieditor.apply();
    }
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }


}
