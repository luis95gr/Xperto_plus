package com.example.luis9.xpertp.Helo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
;import com.example.luis9.xpertp.R;
import com.worldgn.connector.Connector;
import com.worldgn.connector.IPinCallback;

public class PinActivity extends AppCompatActivity {

    public static final int TYPE_EMAIL = 1 ;
    public static final int TYPE_PHONE = 2 ;
    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_EMAIL = "email";
    public static final String ACTION_PHONE = "phone";
    TextView lbl;
    EditText editTextCode;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(false);
        lbl = (TextView)findViewById(R.id.lbl);

        editTextCode = (EditText)findViewById(R.id.code);

        int type = getIntent().getIntExtra(ACTION_TYPE,0);

        if(type == TYPE_EMAIL){
            String email = getIntent().getStringExtra(ACTION_EMAIL);
            lbl.setText("Enter code which you have recieved at " + email);
        }else if(type == TYPE_PHONE){
            String phone = getIntent().getStringExtra(ACTION_PHONE);
            lbl.setText("Enter code which you have recieved at " + phone);
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.verify){
            progressDialog.show();
            String code = editTextCode.getText().toString();

            Connector.getInstance().verifyPin(code, new IPinCallback() {
                @Override
                public void onSuccess(long heloUserId) {
                    progressDialog.cancel();
                    startActivity(new Intent(PinActivity.this, conexionHeloBluetooth.class));
                    finish();
                }

                @Override
                public void onFailure(String description) {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    }

