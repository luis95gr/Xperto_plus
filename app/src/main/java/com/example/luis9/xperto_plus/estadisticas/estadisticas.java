package com.example.luis9.xperto_plus.estadisticas;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.luis9.xpertp.R;

import java.util.ArrayList;


public class estadisticas extends AppCompatActivity {

    //VARIABLES
    ArrayList<variablesBoViajes> listaDatos;
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticas);
        tabLayout = findViewById(R.id.tablayout_id);
        appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.viewPager);
        //
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new viajesFragment(),"Viajes");
        adapter.AddFragment(new diasFragment(),"Dias");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }



}
