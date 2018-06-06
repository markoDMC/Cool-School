package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Profil extends Fragment {

    private Button logout;
    private Button edit;
    private TextView name;
    private TextView skola;
    private TextView razred;
    // private TextView hobi;

    private String user_id;
    private String skola_id;

    private StorageReference storageReference;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.profil, container, false );

        mAuth = FirebaseAuth.getInstance();

        user_id = mAuth.getCurrentUser().getUid();

        mStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        edit = rootView.findViewById( R.id.edit );
        name= rootView.findViewById( R.id.name );
        skola=rootView.findViewById( R.id.skola );
        razred=rootView.findViewById( R.id.razred );
        logout=rootView.findViewById( R.id.logout );



        /*

        String user_skola=task.getResult().getString( "imeSkole" );

                        skola.setText( user_skola );
         */



            mStore.collection( "Users" ).document( user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {

                             String user_name = task.getResult().getString( "name" );
                             String user_razred = task.getResult().getString( "razred" );
                            // String user_hobi = task.getResult().getString( "hobi" );
                             String skola_id = task.getResult().getString( "idSkole" );


                            name.setText( user_name );
                            razred.setText( user_razred );
                           // hobi.setText( user_hobi );

                            if (!TextUtils.isEmpty( skola_id )) {

                                mStore.collection( "Skole" ).document( skola_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        String user_skola = task.getResult().getString( "imeSkole" );

                                        skola.setText( user_skola );

                                    }
                                } );


                            }


                        }

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText( getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG ).show();

                    }


                }
            } );

            logout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAuth.signOut();
                    send_to_login();

                }
            } );





        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity( new Intent( getActivity(),ProfileSetup.class ) );


            }

        } );






        return rootView;
    }

    private void send_to_login () {
        Intent logIntent = new Intent( getActivity(), Login.class );
        startActivity( logIntent );
    }


    }


