package com.creations.roitman.menume;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.User;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;

/**
 * Activity for the user login.
 */
public class ActivityLogin extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<String> {

    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup;
    private SharedPreferences mSettings;
    String userEmail, userPswrd;

    public static final String LOG_IN_TYPE = "logIn";
    public static final String LOG_IN_POST_URL = "/api/users/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmailId = findViewById(R.id.login_mail);
        logInpasswd = findViewById(R.id.login_password);
        btnLogIn = findViewById(R.id.btn_log_in);
        signup = findViewById(R.id.tv_sign_in);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mSettings.registerOnSharedPreferenceChangeListener(preferenceListener);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(ActivityLogin.this, SignUpActivity.class);
                startActivity(I);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = loginEmailId.getText().toString();
                userPswrd = logInpasswd.getText().toString();
                if (userEmail.isEmpty()) {
                    loginEmailId.setError("Provide your Email first!");
                    loginEmailId.requestFocus();
                } else if (userPswrd.isEmpty()) {
                    logInpasswd.setError("Enter Password!");
                    logInpasswd.requestFocus();
                } else if (userEmail.isEmpty() && userPswrd.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPswrd.isEmpty())) {
                    makeHttpRequest();
                } else {
                    Toast.makeText(ActivityLogin.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void makeHttpRequest() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            getLoaderManager().initLoader(5, null,
                    (LoaderManager.LoaderCallbacks<String>) this);
        }
    }

    /**
     * Listener for shared preferences values.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if(key.equals(PreferencesUtils.USER_TOKEN)) {
                        if (!mSettings.getString(PreferencesUtils.USER_TOKEN, "")
                                .equals(PreferencesUtils.INVALID_USER_TOKEN)) {
                            Toast.makeText(ActivityLogin.this, "User logged in ", Toast.LENGTH_SHORT).show();
                            Intent I = new Intent(ActivityLogin.this, MainActivity.class);
                            startActivity(I);
                        } else {
                            Toast.makeText(ActivityLogin.this, "Login to continue", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            };

    @Override
    public android.content.Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new AuthenticationLoader(this, QueryUtils.BASE_URL + LOG_IN_POST_URL,
                LOG_IN_TYPE, new User(userPswrd, userEmail));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            PreferencesUtils.setToken(data, this);
            startActivity(new Intent(ActivityLogin.this, MainActivity.class));
        } else {
            Toast.makeText(ActivityLogin.this, "Not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //do nothing for now
    }

}
