package com.example.surbhimiglani.appetite;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Main3Activity extends AppCompatActivity {
    private EditText inputEmail, inputPassword,Vehicle,Phone;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference userdatabase1,userdatabase1a, ud2, ud3, ud2a, ud3a;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email2);
        inputPassword = (EditText) findViewById(R.id.password2);
        Vehicle = (EditText) findViewById(R.id.Vehicle);
        Phone = (EditText) findViewById(R.id.Phone);
        userdatabase1= FirebaseDatabase.getInstance().getReference().child("Ambulance").child("free");
        ud2= FirebaseDatabase.getInstance().getReference().child("Fire Brigade").child("free");
        ud3= FirebaseDatabase.getInstance().getReference().child("Police").child("free");
        userdatabase1a= FirebaseDatabase.getInstance().getReference().child("Ambulance").child("busy");
        ud2a= FirebaseDatabase.getInstance().getReference().child("Fire Brigade").child("busy");
        ud3a= FirebaseDatabase.getInstance().getReference().child("Police").child("busy");
        spinner=(Spinner) findViewById(R.id.spinner);
        btnSignUp = (Button) findViewById(R.id.signVehicle);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.emergencyvehicle, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Main3Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Toast.makeText(Main3Activity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Main3Activity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(Main3Activity.this, Welcome.class));
                                    finish();
                                }
                            }
                        });
                String email1 = inputEmail.getText().toString().trim();
                String password1 = inputPassword.getText().toString().trim();
                String Vehicle1 = Vehicle.getText().toString().trim();
                String phonenmber = Phone.getText().toString().trim();


                HashMap<String,String> DataMap1= new HashMap<String, String>();
                DataMap1.put("Name",email1);
                DataMap1.put("Password",password1);
                DataMap1.put("Address",Vehicle1);
                DataMap1.put("Number",phonenmber);

                if(spinner.getSelectedItemPosition()==1){
                    ud2.push().setValue(DataMap1);


                }

               else if(spinner.getSelectedItemPosition()==2){
                    userdatabase1.push().setValue(DataMap1);


                }

               else if(spinner.getSelectedItemPosition()==3){
                    ud3.push().setValue(DataMap1);


                }

            }

        });



    }
}
