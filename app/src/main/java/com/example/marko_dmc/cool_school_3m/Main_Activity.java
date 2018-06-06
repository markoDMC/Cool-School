package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Main_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;


    private BottomNavigationView mainbottomNav;

    private Naslovna home;
    private Grupe groups;
    private MojiPostovi myposts;
    private Profil profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        if(mAuth.getCurrentUser() != null) {

            mainbottomNav = findViewById(R.id.mainBottomNav);

            // FRAGMENTS
            home=new Naslovna();
            groups=new Grupe();
            myposts=new MojiPostovi();
            profile=new Profil();


            initializeFragment();

            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (item.getItemId()) {

                        case R.id.bottom_action_home:

                            replaceFragment(home, currentFragment);
                            return true;

                        case R.id.bottom_action_groups:

                            replaceFragment(groups, currentFragment);
                            return true;

                        case R.id.bottom_action_myposts:

                            replaceFragment(myposts, currentFragment);
                            return true;

                        case R.id.bottom_action_profile:

                            replaceFragment(profile, currentFragment);
                            return true;

                        default:
                            return false;


                    }

                }
            });




        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

            sendToLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();


            firebaseFirestore.collection( "Users" ).document( current_user_id ).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {

                            String skola_exist = task.getResult().getString( "idSkole" );

                            if (TextUtils.isEmpty( skola_exist )) {

                                Intent setupIntent = new Intent( Main_Activity.this, Skole.class );
                                startActivity( setupIntent );
                                finish();

                            }
                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText( Main_Activity.this, "Error : " + errorMessage, Toast.LENGTH_LONG ).show();


                    }

                }
            } );

        }
    }






    private void logOut() {


        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(Main_Activity.this, Login.class);
        startActivity(loginIntent);
        finish();

    }

    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, home);
        fragmentTransaction.add(R.id.main_container, groups);
        fragmentTransaction.add(R.id.main_container, myposts);
        fragmentTransaction.add(R.id.main_container, profile);

        fragmentTransaction.hide(groups);
        fragmentTransaction.hide(myposts);
        fragmentTransaction.hide(profile);

        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == home){

            fragmentTransaction.hide(groups);
            fragmentTransaction.hide(myposts);
            fragmentTransaction.hide(profile);

        }

        if(fragment == groups){

            fragmentTransaction.hide(home);
            fragmentTransaction.hide(myposts);
            fragmentTransaction.hide(profile);

        }

        if(fragment == myposts){

            fragmentTransaction.hide(groups);
            fragmentTransaction.hide(home);
            fragmentTransaction.hide(profile);

        }

        if(fragment == profile){

            fragmentTransaction.hide(home);
            fragmentTransaction.hide(myposts);
            fragmentTransaction.hide(groups);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }


}
