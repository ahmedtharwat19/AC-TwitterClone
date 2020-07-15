package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtUsernameSignUp, edtEmailSignUp, edtPasswordSignUp, edtPassword2, edtWhatsAppPhone;
    private Button btnSignUp,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("SignUp  : TwitterClone");
        setTitleColor(Color.RED);

        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtUsernameSignUp = findViewById(R.id.edtUsernameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        edtPassword2 = findViewById(R.id.edtPassword2);

        edtPassword2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return false;
            }
        });

        edtWhatsAppPhone = findViewById(R.id.edtWhatsAppPhone);
        btnLogin = findViewById(R.id.btnLogIN);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            finish();
            transactionToMainActivity();
        }
    }

    @Override
    public void onClick(View view) {
        rootLayoutTapped(view);
        switch (view.getId()){
            case R.id.btnSignUp:
                if (edtUsernameSignUp.getText().toString().equals("")
                        || edtEmailSignUp.getText().toString().equals("")
                        || edtPasswordSignUp.getText().toString().equals("")
                        || edtPassword2.getText().toString().equals("")) {

                    FancyToast.makeText(this,"Please fill required fields: email,username,Password",FancyToast.LENGTH_SHORT,FancyToast.WARNING,true).show();
                } else {
                        String pwd2 = edtPassword2.getText().toString();
                        String pwd1 = edtPasswordSignUp.getText().toString();
//                    edtPassword2.getText().toString() != edtPasswordSignUp.getText().toString()
//                    boolean temp = true;
                    if (!pwd1.equals(pwd2)) {
                        FancyToast.makeText(this,"Password not match , Please Enter Match Password",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                        finish();
//                        temp = false;
                        //return temp;

                    } else {
                        final ParseUser parseUser = new ParseUser();
                        parseUser.setEmail(edtEmailSignUp.getText().toString());
                        parseUser.setUsername(edtUsernameSignUp.getText().toString());
                        parseUser.setPassword(edtPasswordSignUp.getText().toString());
                        parseUser.put("WhatsAppPhone",edtWhatsAppPhone.getText().toString());
//                        parseUser.put("isStaff",true);
//                        parseUser.put("isModerator",false);
/*                        boolean isStaff = true;
                        boolean isModerator = false;
                        parseUser.put("isStaff", true);*/
                        parseUser.put("is_Moderator", false);
                        parseUser.saveInBackground();

                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setTitle("Signing Up ...");
                        progressDialog.setMessage("Please Wait while registering finished.");
                        progressDialog.show();


                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(SignUpActivity.this,parseUser.getUsername() + " is Signing Up Successfully.",
                                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                    finish();
                                    transactionToMainActivity();
                                } else {
                                    FancyToast.makeText(SignUpActivity.this, "There was an error: " + e.getMessage(),
                                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }

                }
                break;
            case R.id.btnLogIN:
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
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
            resumeName = MainActivity.class.getCanonicalName();
            activityName = "MainActivity";
        }
        try {
            Class newClass = Class.forName(resumeName);
            Intent resume = new Intent(SignUpActivity.this, newClass);
            startActivity(resume);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        FancyToast.makeText(SignUpActivity.this,activityName + ""
                ,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
    }
}