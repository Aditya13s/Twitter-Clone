package com.aditya.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;

public class PostTweetActivity extends AppCompatActivity {
    EditText tweetEditText;
    Button postTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        tweetEditText = findViewById(R.id.tweetEditText);
        postTweet = findViewById(R.id.postTweet);


        postTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTweet();
            }
        });
    }

    private void postTweet() {
        String Tweet = tweetEditText.getText().toString();
        if(Tweet.length() == 0) {
            Toast.makeText(this, "Tweet should not be empty", Toast.LENGTH_SHORT).show();
        } else {
//            HashMap<String, String> map = new HashMap<>();
            Date currentTime = Calendar.getInstance().getTime();
//            map.put(currentTime.toString(),Tweet);
            FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(FirebaseAuth.getInstance().getUid())
                    .child("Tweets").child(currentTime.toString()).setValue(Tweet)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                finish();
                                Toast.makeText(PostTweetActivity.this, "Tweet Posted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PostTweetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}