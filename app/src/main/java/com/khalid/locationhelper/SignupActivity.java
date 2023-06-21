package com.khalid.locationhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText inputEmail;

    EditText inputPassword;

    DatabaseReference current_user_db;

    private FirebaseAuth auth;
    ImageView imv;


    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imv=(ImageView) findViewById(R.id.imh);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.sign_up_button);

        auth = FirebaseAuth.getInstance();





        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final String email = inputEmail.getText().toString().trim();
                    final String password = inputPassword.getText().toString().trim();


                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }


              /*  if (inputEnterTainer.isChecked()){
                  //  inputEntertainmentName.setVisibility(View.VISIBLE);
                }*/


                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        try {
                                            FirebaseUser user = auth.getCurrentUser();

                                            user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                                                if (!emailTask.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, emailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                } else {

                                                    Toast.makeText(SignupActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();


                                                    String user_id = auth.getCurrentUser().getUid();
                                                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                   startActivity(new Intent(SignupActivity.this, login.class));

                                                    finish();


                                                }
                                            });

                                        }catch (Exception e){
                                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                }
                catch (Exception e){
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });


    }
}
