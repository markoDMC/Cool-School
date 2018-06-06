package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Skole extends AppCompatActivity {

    private Button skola_1;
    private Button skola_2;
    private Button skola_3;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    private String user_id;
    public  String skola_id;
    private String ime_skole;

    private String skola_exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_skole );

        skola_1=findViewById( R.id.skola1 );
        skola_2=findViewById( R.id.skola2 );
        skola_3=findViewById( R.id.skola3 );

        mAuth=FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();

        user_id= mAuth.getCurrentUser().getUid();



        // looks if user has a school
        mStore.collection( "User" ).document( user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                skola_exist = task.getResult().getString( "idSkole" );

            }} }
        } );




        skola_1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Remete
                skola_id="qDfQteJMjDTQuzxIPspf";

                baza();
                startActivity( new Intent( Skole.this, Main_Activity.class ) );
                finish();
            }
        } );

        skola_2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ivan Mažuranić
                skola_id="jXcl1YYc0utOz77bVLSS";

                baza();
                startActivity( new Intent( Skole.this, Main_Activity.class ) );
                finish();

            }
        } );

        skola_3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Bukovac
                skola_id="dD47IcGz4VMjwUZHlYPA";
                startActivity( new Intent( Skole.this,Main_Activity.class ) );
                finish();

            }
        } );


    }

   // Intent main = new Intent( Skole.this, Main_Activity.class );

    private void baza() {


            mStore.collection( "Users" ).document(user_id).update("idSkole",skola_id).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        Toast.makeText(Skole.this, "Uspjesno si dodan u odabranu školu", Toast.LENGTH_LONG).show();

                    }else{

                        String error = task.getException().getMessage();
                        Toast.makeText(Skole.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                    }

                }
            } );
    }




}
