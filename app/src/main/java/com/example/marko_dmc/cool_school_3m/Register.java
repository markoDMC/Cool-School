package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private ProgressBar prog_bar;

    private EditText reg_email;
    private EditText reg_pass;
    private EditText conf_pass;
    private Button reg_btn;

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private String c_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prog_bar=  findViewById(R.id.prog_bar_reg) ;
        reg_email=  findViewById(R.id.registration_email);
        reg_pass=  findViewById(R.id.registration_pass);
        conf_pass=  findViewById(R.id.reg_pass_conf);
        reg_btn=  findViewById(R.id.registration_btn);

        mAuth=FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();



        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email=reg_email.getText().toString();
                String pass=reg_pass.getText().toString();
                String pass_two=conf_pass.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(pass_two)){

                    if(pass.equals(pass_two)){

                        prog_bar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Map<String, String> userMap = new HashMap<>();
                                    userMap.put("idSkole", "");


                                    c_user=mAuth.getCurrentUser().getUid();
                                    mStore.collection("Users" ).document(c_user).set(userMap).addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                        }
                                    } );

                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(Register.this,"Molim te verificiraj E-mail adresu.",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Register.this,Login.class));
                                            }
                                            else{
                                                Toast.makeText(Register.this, "Neuspješno slanje E-mail verifikacije, molim te probaj ponovno.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                                else{
                                    String errMessage=task.getException().getMessage();
                                    Toast.makeText(Register.this, "Greška:  "+ errMessage,Toast.LENGTH_LONG).show();



                                }
                                prog_bar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }

                    else{
                        Toast.makeText(Register.this, "Lozinke se ne podudaraju.", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });



    }


    private void sendToMain() {
        Intent mainIntent=new Intent(Register.this,Login.class);
        startActivity(mainIntent);
        finish();
    }

}
