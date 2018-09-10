package com.example.luis9.xperto_plus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xpertp.R;

import android.transition.Slide;



public class createAccount extends AppCompatActivity {

    //VARIABLES
    SharedPreferences spLogin;
    EditText etFname, etLname, etEmail, etRemail, etPass, etRpass, etPhone, etWeight, etHeight,etIDconductor;
    Spinner spYear, spMonth, spDay, spCountry, spEjercicio;
    RadioButton rbFemale, rbMale;
    TextView txtNext;
    String ip = "smarth.xperto.com.mx/altachofer/registrocel.php?";
    String CompleteName, Email, Password, Phone, Weight, Height, Birthday, Country, Gender, Ejercicio,ID;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        //
        setupWindowAnimations();
        //
        spLogin = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor spLoginEditor = spLogin.edit();
        //CAST
        txtNext = (TextView) findViewById(R.id.txtNext);
        etFname = (EditText) findViewById(R.id.etFname);
        etLname = (EditText) findViewById(R.id.etLname);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etRemail = (EditText) findViewById(R.id.etRemail);
        etPass = (EditText) findViewById(R.id.etPass);
        etRpass = (EditText) findViewById(R.id.etRpass);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etIDconductor = findViewById(R.id.etIdConductor);
        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);
        spDay = (Spinner) findViewById(R.id.spDay);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        spEjercicio = (Spinner)findViewById(R.id.spEjercicio);
        //


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.month, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.year, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.country, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.ejercicio, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spDay.setAdapter(adapter);
        spMonth.setAdapter(adapter2);
        spYear.setAdapter(adapter3);
        spCountry.setAdapter(adapter4);
        spEjercicio.setAdapter(adapter5);

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteName = etFname.getText().toString() + " " + etLname.getText().toString();

                if (etEmail.getText().toString().equals(etRemail.getText().toString())) {
                    Email = etEmail.getText().toString();
                }
                if (etPass.getText().toString().equals(etRpass.getText().toString())) {
                    Password = etPass.getText().toString();
                }
                if (rbFemale.isChecked()) {
                    Gender = "Femenino";
                }
                if (rbMale.isChecked()) {
                    Gender = "Masculino";
                }

                Weight = etWeight.getText().toString();
                Height = etHeight.getText().toString();
                Phone = etPhone.getText().toString();
                Country = spCountry.getSelectedItem().toString();
                Birthday = spYear.getSelectedItem().toString() + spMonth.getSelectedItem().toString() + spDay.getSelectedItem().toString();
                Ejercicio = spEjercicio.getSelectedItem().toString();
                ID = etIDconductor.getText().toString();

                VolleyPetition("http://"+ ip + "nombre=" + CompleteName + "&usuario=" + ID + "&correo=" + Email + "&password=" + Password +
                        "&celular=" + Phone + "&genero=" + Gender + "&peso=" + Weight + "&altura=" + Height + "&fecha=" + Birthday +
                        "&pais=" + Country + "&condicion=" + Ejercicio);

                spLoginEditor.putString("email",etEmail.getText().toString());
                spLoginEditor.putString("password",etPass.getText().toString());
                spLoginEditor.apply();
                Intent intent = new Intent(createAccount.this,login.class);
                startActivity(intent);
            }
        });
    }
    private void VolleyPetition(String URL) {
        Log.i("url", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.createAccount), "BIENVENIDO", Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent = new Intent(createAccount.this, login.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
    }
}

