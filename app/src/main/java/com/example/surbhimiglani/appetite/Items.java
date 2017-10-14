package com.example.surbhimiglani.appetite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String[] foods={"A","B","C","D","E"};
        ListAdapter myadapter=new CustomAdapter(this,foods);
        ListView listview=(ListView) findViewById(R.id.listView);

        if(SharedPreferncesHelper.getPlace(getApplicationContext()).equals("Goa,India")){

            listview.setAdapter(myadapter);

            listview.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                            String food=String.valueOf(parent.getItemAtPosition(position));
                            Toast.makeText(Items.this,food,Toast.LENGTH_LONG).show();
                        }
                    }

            );
        }


    }
}
