package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lstView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        setTitle("Users : ");

        FancyToast.makeText(TwitterUsers.this, "Welcome " + ParseUser.getCurrentUser().getUsername()
                , FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

        lstView = findViewById(R.id.lstView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);
        lstView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lstView.setOnItemClickListener(this);
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser());
            query.whereEqualTo("is_Moderator", false);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser twitterUser : objects) {
                            tUsers.add(twitterUser.getUsername());
                        }
                        lstView.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this,SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if  (checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now followed."
                    ,FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
        } else {
            FancyToast.makeText(TwitterUsers.this, tUsers.get(position) + " is now not followed."
                    ,FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
        }
    }
}