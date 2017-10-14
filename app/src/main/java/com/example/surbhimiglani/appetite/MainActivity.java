package com.example.surbhimiglani.appetite;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView rText;
    Button button, button2, userLogin ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rText=(TextView) findViewById(R.id.textView2);
        button=(Button) findViewById(R.id.button);
        button2=(Button) findViewById(R.id.button2);
        userLogin=(Button) findViewById(R.id.userLogin);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/script.ttf");
        rText.setTypeface(custom_font);


        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, RegisterAsUser.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });




    }
}
