package com.example.luis9.xperto_plus.estadisticas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luis9.xperto_plus.GlobalVars;
import com.example.luis9.xperto_plus.login;
import com.example.luis9.xperto_plus.main;
import com.example.luis9.xpertp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class viajesFragment extends Fragment {
    public viajesFragment() {
    }
    //VARIABLES
    RecyclerView recyclerView;
    ArrayList<variablesBoViajes> listaDatos;
    String ip = "http://smarth.xperto.com.mx/mean/recuperajson.php?usuario=";
    SharedPreferences spLogin;
    JSONArray jsonArray;
    ArrayList<String> arrayListVelocidad,arrayListHr,arrayListBr,arrayListBpmax,arrayListBpmin,arrayListTired,arrayListVeryTired,arrayListNormal
            ,arrayListFecha, arrayListHora,arrayListHoraFin,arrayListCalm,arrayListExc,arrayListDepr,arrayListDuracion;
    Handler handler;
    TextView textView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        spLogin = getActivity().getSharedPreferences("login",Context.MODE_PRIVATE);
        arrayListFecha = new ArrayList<>();
        arrayListVelocidad = new ArrayList<>();
        arrayListBpmax = new ArrayList<>();
        arrayListBpmin = new ArrayList<>();
        arrayListBr = new ArrayList<>();
        arrayListCalm = new ArrayList<>();
        arrayListDepr = new ArrayList<>();
        arrayListDuracion = new ArrayList<>();
        arrayListExc = new ArrayList<>();
        arrayListHora = new ArrayList<>();
        arrayListHoraFin = new ArrayList<>();
        arrayListHr = new ArrayList<>();
        arrayListNormal = new ArrayList<>();
        arrayListTired = new ArrayList<>();
        arrayListVeryTired = new ArrayList<>();
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_viajes, container, false);
        //CAST
        textView = vista.findViewById(R.id.text);
        spLogin = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        listaDatos = new ArrayList<>();
        recyclerView = vista.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //
        /////////////////////////////////////////////////ID
        if (internet()) {
            VolleyPetition(ip + spLogin.getString("id", "id"));    //ESTE ES EL CORRECTO
        } else {
            textView.setText("No hay internet disponible");
        }
        Log.i("service123",spLogin.getString("id","id"));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adaptadorViajes adaptadorViajes = new adaptadorViajes(listaDatos);
                recyclerView.setAdapter(adaptadorViajes);
                llenarLista();
            }
        },1000);
        return vista;
    }

    public void VolleyPetition(String URL) {
        //Log.i("service123", "" + URL);
        RequestQueue queue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("service123","entroVolley");
                Log.i("service123",response);
                //
                try {
                    jsonArray = new JSONArray(response);
                    String a;
                    for (int i = 0; i < jsonArray.length(); i++){
                        a = jsonArray.getString(i);
                        JSONObject jsonObject = new JSONObject(a);
                        arrayListFecha.add(jsonObject.getString("fecha"));
                        arrayListHora.add(jsonObject.getString("horainicio"));
                        arrayListHoraFin.add(jsonObject.getString("horafin"));
                        arrayListDuracion.add(jsonObject.getString("duracion"));
                        arrayListVelocidad.add(jsonObject.getString("velocidad"));
                        arrayListHr.add(jsonObject.getString("hr"));
                        arrayListBr.add(jsonObject.getString("br"));
                        arrayListBpmax.add(jsonObject.getString("bpmax"));
                        arrayListBpmin.add(jsonObject.getString("bpmin"));
                        arrayListNormal.add(jsonObject.getString("normal"));
                        arrayListTired.add(jsonObject.getString("tired"));
                        arrayListVeryTired.add(jsonObject.getString("very_tired"));
                        arrayListCalm.add(jsonObject.getString("calm"));
                        arrayListExc.add(jsonObject.getString("excitement"));
                        arrayListDepr.add(jsonObject.getString("depression"));
                        //
                    }


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

    private void llenarLista() {
        if (!arrayListFecha.isEmpty()){
            textView.setVisibility(View.INVISIBLE);
            for (int i = 0; i < arrayListFecha.size(); i++){
                //
                listaDatos.add(new variablesBoViajes(arrayListFecha.get(i),arrayListHora.get(i),arrayListHoraFin.get(i),
                        arrayListDuracion.get(i),arrayListVelocidad.get(i), arrayListHr.get(i),arrayListBr.get(i),
                        arrayListBpmax.get(i) + "/" + arrayListBpmin.get(i),arrayListNormal.get(i),arrayListTired.get(i),
                        arrayListVeryTired.get(i),arrayListCalm.get(i),arrayListExc.get(i),arrayListDepr.get(i)));

            }
        }
    }

    protected boolean internet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
