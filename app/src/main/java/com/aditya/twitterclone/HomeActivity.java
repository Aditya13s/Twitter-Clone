package com.aditya.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView usersListView;
    ArrayList<String> usersList; //In this we will store the name and email address of the user
    ArrayList<String> uidList;
    ArrayAdapter arrayAdapter;
    public static ArrayList<String> following;
    private FirebaseAuth mAuth;
    private  DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        usersListView = findViewById(R.id.usersListView);

        usersListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        usersList = new ArrayList<>();
        uidList = new ArrayList<>();
        following = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,usersList);
        usersListView.setAdapter(arrayAdapter);

        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                uidList.clear();
                for(DataSnapshot postSnapshow: snapshot.getChildren()) {
                    if (!mAuth.getCurrentUser().getUid().equals(postSnapshow.getKey())) {
                        uidList.add(postSnapshow.getKey());
                        String username = (String) postSnapshow.child("Name").getValue();
                        String email = (String) postSnapshow.child("Email").getValue();
                        usersList.add(username + "\n" + email);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                String uid = uidList.get(i);
                if(checkedTextView.isChecked()) {
                    following.add(uid);
                } else {
                    following.remove(uid);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.tweet) {
            Intent intent = new Intent(HomeActivity.this, PostTweetActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.feed) {
            Intent intent = new Intent(HomeActivity.this, YourFeedActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}