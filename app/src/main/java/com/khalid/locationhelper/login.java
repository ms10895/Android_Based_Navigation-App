package com.khalid.locationhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;

    private Button btnSignup, btnLogin;
    ImageView imj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //   progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imj=(ImageView)findViewById(R.id.logg) ;
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
       // btnReset = (Button) findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // auth = FirebaseAuth.getInstance();

        //   final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




     /*   if (user != null) {
            startActivity(new Intent(login.this, addPointsScreen.class));


        }*/


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, SignupActivity.class));


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //   progressBar.setVisibility(View.VISIBLE);


                //authenticate user
                try {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error

                                        Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    } else {


                                        // checkUserType=   auth.getCurrentUser().getUid();

                                        //   FirebaseDatabase.getInstance().getReference().child()


                                        try {
                                            if (!auth.getCurrentUser().isEmailVerified()) {

                                                Toast.makeText(login.this, "Please Verify Your Email we have sent you a mail", Toast.LENGTH_SHORT).show();
                                            } else {


                                                startActivity(new Intent(login.this, addPointsScreen.class));
Log.i("ddd","s");
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                            });
                } catch (Exception e) {
                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}