package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button log_btn;
    private EditText email_sign;
    private EditText pass;
    private Button reg_to_btn;


    private ProgressBar log_prog;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );


        email_sign = (EditText) findViewById( R.id.name_txt );
        pass = (EditText) findViewById( R.id.pass );
        log_btn = (Button) findViewById( R.id.test_btn );
        reg_to_btn = (Button) findViewById( R.id.reg_to_btn );
        log_prog = (ProgressBar) findViewById( R.id.login_prog );

        mAuth = FirebaseAuth.getInstance();


        log_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail = email_sign.getText().toString();
                String loginPass = pass.getText().toString();

                if (!TextUtils.isEmpty( loginEmail ) && !TextUtils.isEmpty( loginPass )) {
                    log_prog.setVisibility( View.VISIBLE );
                    mAuth.signInWithEmailAndPassword( loginEmail, loginPass ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    send_to_main();
                                } else {
                                    Toast.makeText( Login.this, "Molim te verificiraj E-mail adresu.", Toast.LENGTH_SHORT ).show();
                                }
                            } else {
                                String err_mess = task.getException().getMessage();
                                Toast.makeText( Login.this, "Molim te pokušaj ponovno... " + err_mess, Toast.LENGTH_LONG ).show();
                            }
                            log_prog.setVisibility( View.INVISIBLE );
                        }
                    } );
                }
            }
        } );

        reg_to_btn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {


                startActivity( new Intent( Login.this, Register.class ) );
            }
        } );


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser c_user = FirebaseAuth.getInstance().getCurrentUser();
        if (c_user != null ) {
            if(c_user.isEmailVerified()) {
                send_to_main();
                finish();
            }

        }


    }

    private void send_to_main() {

        Intent mainIntent = new Intent( Login.this, Main_Activity.class );
        startActivity( mainIntent );
        finish();
    }
}
