package com.example.surbhimiglani.appetite;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Surbhi Miglani on 11-10-2017.
 */

class CustomAdapter extends ArrayAdapter<String> {

    int c=0;
    CustomAdapter(Context context, String[] foods) {
        super(context,R.layout.newlayout,foods);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater myinflator=LayoutInflater.from(getContext());
        View customView=myinflator.inflate(R.layout.newlayout,parent,false);

        String fooditem=getItem(position);
        final TextView textView=(TextView) customView.findViewById(R.id.textView);
        ImageView imageView=(ImageView) customView.findViewById(R.id.imageView);
        ImageView imageView2=(ImageView) customView.findViewById(R.id.imageView2);

        textView.setText(fooditem);
        imageView.setImageResource(R.drawable.dice);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c++;
                SharedPreferncesHelper.putSelect(getContext(), textView.getText().toString());
            }
        });
        SharedPreferncesHelper.putInt(getContext(), c);
        return customView;


    }
}
