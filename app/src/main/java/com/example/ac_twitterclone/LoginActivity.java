package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmailLogin, edtPasswordLogin;
    private Button btnLoginActivity, btnSignUpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LogIn : TwitterClone");
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        edtPasswordLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLoginActivity);
                }
                return false;
            }
        });

        btnLoginActivity = findViewById(R.id.btnLoginActivity);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);

        btnSignUpActivity.setOnClickListener(this);
        btnLoginActivity.setOnClickListener(this);
        if (ParseUser.getCurrentUser() != null) {
            finish();
            transactionToMainActivity();
        }
    }

    @Override
    public void onClick(View view) {
        rootLayoutTapped(view);
        switch (view.getId()){
            case R.id.btnLoginActivity:
                if (edtEmailLogin.getText().toString().equals("") ||
                        edtPasswordLogin.getText().toString().equals("")) {
                    FancyToast.makeText(LoginActivity.this, "Email, Password is required!" ,
                            FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show();
                } else {
                    ParseUser.logInInBackground(edtEmailLogin.getText().toString(), edtPasswordLogin.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Logged In successfully",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                finish();
                                transactionToMainActivity();
                            } else {
                                FancyToast.makeText(LoginActivity.this, "There was an error: " + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });
                }
                break;
            case R.id.btnSignUpActivity:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transactionToMainActivity(){
        String activityName = "";
        String resumeName = "";
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.get("is_Moderator").toString().equals("true")){
            activityName = "ModeratorsActivity";
            resumeName = ModeratorsActivity.class.getCanonicalName();
        } else {
//            resumeName = MainActivity.class.getCanonicalName();
            resumeName = TwitterUsers.class.getCanonicalName();
//            activityName = "MainActivity";
            activityName = "TwitterUsers";
        }
        try {
            Class newClass = Class.forName(resumeName);
            Intent resume = new Intent(LoginActivity.this, newClass);
            startActivity(resume);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        FancyToast.makeText(LoginActivity.this,activityName + "",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
    }
}