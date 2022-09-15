package com.aditya.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class YourFeedActivity extends AppCompatActivity {
    ListView feedListView;
    ArrayList<String> feedList;
    ArrayAdapter arrayAdapter;
    DatabaseReference mRef;
    ArrayList<String> list = new ArrayList<>(HomeActivity.following);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_feed);

        feedListView = findViewById(R.id.feedListView);
        feedList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,feedList);
        feedListView.setAdapter(arrayAdapter);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String username = postSnapshot.child("Name").getValue().toString();
                    if(!key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && list.contains(key)) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(key).child("Tweets")
                                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    String feed = postSnapshot.getValue().toString();
                                    String time = postSnapshot.getKey().toString();
                                    String[] timeSplit = time.split(" ");
                                    feedList.add(username + " : " + feed + "\n\n" +
                                            timeSplit[3] + "\n" +
                                            timeSplit[2] + ", " + timeSplit[1] + ", " + timeSplit[5]);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}