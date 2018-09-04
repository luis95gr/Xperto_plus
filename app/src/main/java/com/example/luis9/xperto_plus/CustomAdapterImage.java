package com.example.luis9.xperto_plus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luis9.xpertp.R;

public class CustomAdapterImage extends BaseAdapter {
    Context context;
    int[] images;
    String[] names;
    LayoutInflater inflater;

    public CustomAdapterImage(Context applicationContext, String[] names,int[] images){
        this.context = applicationContext;
        this.images = images;
        this.names = names;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_item,null);
        TextView nameG = (TextView)view.findViewById(R.id.name);
        ImageView imageG = (ImageView) view.findViewById(R.id.image);
        imageG.getLayoutParams().height = 700;
        imageG.getLayoutParams().width = 700;
        imageG.setImageResource(images[position]);
        nameG.setText(names[position]);
        return view;
    }
}
