package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetup extends AppCompatActivity {

    private Button save;
    private TextView name;
    private TextView razred;
   // private TextView hobi;
    private String user_id;

    public  String idSkole;

    private StorageReference storageReference;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile_setup );

        save= findViewById( R.id.save );
        name= findViewById( R.id.name );
        razred= findViewById( R.id.razred );




        mAuth = FirebaseAuth.getInstance();

        user_id = mAuth.getCurrentUser().getUid();

        mStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name=name.getText().toString();
                String user_razred=razred.getText().toString();


                if(!TextUtils.isEmpty( user_name )  && !TextUtils.isEmpty( user_razred )){


                    mStore.collection( "Users" ).document(user_id).update("name",user_name, "razred",user_razred ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(ProfileSetup.this, "Korisnički podaci uspješno promjenjeni!", Toast.LENGTH_LONG).show();
                                startActivity( new Intent(ProfileSetup.this,Main_Activity.class) );


                            }else{

                                String error = task.getException().getMessage();
                                Toast.makeText(ProfileSetup.this, "Došlo je do greške : " + error, Toast.LENGTH_LONG).show();

                            }

                        }
                    } );


                }else{
                    Toast.makeText(ProfileSetup.this, "Molim te ispuni sve podatke!", Toast.LENGTH_LONG).show();
                }

            }
        } );








    }
}
