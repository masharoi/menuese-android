package com.creations.roitman.menume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    public EditText emailId, passwd;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.user_mail);
        passwd = findViewById(R.id.user_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        signIn = findViewById(R.id.tv_sign_in);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String password = passwd.getText().toString();
                if (emailID.isEmpty()) {
                    if (password.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        emailId.setError("Provide your Email first!");
                        emailId.requestFocus();
                    }
                } else if (password.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
              //  } else if (emailID.isEmpty() && password.isEmpty()) {
                //    Toast.makeText(SignUpActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && password.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(SignUpActivity.this,
                            new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(SignUpActivity.this, ActivityLogin.class);
                startActivity(I);
            }
        });
    }
}
