package com.example.ircsa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class create_new_account extends AppCompatActivity {

    private EditText email_signup;
    private EditText password_signup;
    private EditText conf_password_signup;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth=FirebaseAuth.getInstance();
        email_signup=(EditText)findViewById(R.id.mail_signup);
        password_signup=(EditText)findViewById(R.id.password_signup);
        conf_password_signup=(EditText)findViewById(R.id.confirm_password_signup);

    }
    public void onSubmit_signup(View view)
    {
        String s_email_signup=email_signup.getText().toString();
        String s_password_signup=password_signup.getText().toString();
        String s_conf_password_signup=conf_password_signup.getText().toString();
        if(!TextUtils.isEmpty(s_conf_password_signup)&& (!TextUtils.isEmpty(s_email_signup))&&(!TextUtils.isEmpty(s_password_signup)))
        {
            if(s_password_signup.equals(s_conf_password_signup))
            {
                Log.i("Create new account","New account created");
                mAuth.createUserWithEmailAndPassword(s_email_signup,s_password_signup).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("firebase_newAccount","USer added to database");
                            Intent side_navigationIntent=new Intent(create_new_account.this,MainActivity.class);
                            startActivity(side_navigationIntent);
                            finish();

                        }
                        else
                        {
                            String error_message=task.getException().getMessage();
                            Toast.makeText(create_new_account.this,"Error:"+error_message,Toast.LENGTH_LONG);
                            Log.i("firebase_newAccount",error_message);

                        }
                    }
                });

            }
            else{
                Toast.makeText(create_new_account.this,"Password and confirm password doesn't match",Toast.LENGTH_LONG);
            }
        }
    }
    public void onClick_Already_Account(View view)
    {
        Intent login_intent=new Intent(create_new_account.this,AccountSetup.class);
        startActivity(login_intent);
        finish();
    }
}