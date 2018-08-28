package com.creations.roitman.menume;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.User;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;

public class SignUpActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<String> {

    EditText emailId, password, etUsername;
    String umail, upassword, username;
    Button btnSignUp;
    TextView signIn;
    public static final String SIGN_UP_TYPE = "signUp";
    public static final String SIGN_UP_POST_URL = "/api/users/create";
    private static final String LOG_TAG = SignUpActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailId = findViewById(R.id.user_mail);
        password = findViewById(R.id.user_password);
        etUsername = findViewById(R.id.username);
        btnSignUp = findViewById(R.id.btn_sign_up);
        signIn = findViewById(R.id.tv_sign_in);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                umail = emailId.getText().toString();
                upassword = password.getText().toString();
                username = etUsername.getText().toString();
                if (umail.isEmpty()) {
                    if (upassword.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        emailId.setError("Provide your Email first!");
                        emailId.requestFocus();
                    }
                } else if (upassword.isEmpty()) {
                    password.setError("Set your password");
                    password.requestFocus();
              //  } else if (emailID.isEmpty() && password.isEmpty()) {
                //    Toast.makeText(SignUpActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(umail.isEmpty() && upassword.isEmpty())) {
                    makeHttpRequest();
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

    private void makeHttpRequest() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            getLoaderManager().initLoader(5, null,
                    this);
        }
    }

    @NonNull
    @Override
    public android.content.Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AuthenticationLoader(this, QueryUtils.BASE_URL + SIGN_UP_POST_URL,
                SIGN_UP_TYPE, new User(username, umail, upassword));
    }

    @Override
    public void onLoadFinished(android.content.Loader<String> loader, String data) {
        Log.e(LOG_TAG, "The load was finished." +
                "");
        if (data != null) {
            PreferencesUtils.setToken(data, this);
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        } else {
            Toast.makeText(SignUpActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<String> loader) {
        //do nothing for now
    }

}
