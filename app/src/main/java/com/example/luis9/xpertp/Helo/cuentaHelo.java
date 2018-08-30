package com.example.luis9.xpertp.Helo;


import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.luis9.xpertp.R;
import com.worldgn.connector.Connector;
import com.worldgn.connector.ILoginCallback;

public class cuentaHelo extends AppCompatActivity {

    //VARIABLES
    //String email = "xpertoappf@gmail.com";
    String email = "healthcare4pef@gmail.com";
    String key = "152384277176254522";
    String token = "B9B2E74B829AC1FCE45FFF44BE772CE8C5DD2200";
    Handler handler;
    boolean aBooleanisLoggedIn = false;
    int width,height;
    //

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_helo);
        //
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                aBooleanisLoggedIn = Connector.getInstance().isLoggedIn();
                if (aBooleanisLoggedIn){
                    startActivity(new Intent( cuentaHelo.this, conexionHeloBluetooth.class));
                }
                //
                Connector.getInstance().initialize(getApplicationContext(), key, token);
                //
                Connector.getInstance().login(email, new ILoginCallback() {
                    @Override
                    public void onSuccess(long l) {
                        startActivity(new Intent(cuentaHelo.this, conexionHeloBluetooth.class));
                        finish();
                    }

                    @Override
                    public void onPinverification() {
                        Intent intent = new Intent(cuentaHelo.this, PinActivity.class);
                        intent.putExtra(PinActivity.ACTION_TYPE, PinActivity.ACTION_EMAIL);
                        intent.putExtra(PinActivity.ACTION_EMAIL, email);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void accountVerification() {

                    }
                });
            }
        },1800);

    }

}


