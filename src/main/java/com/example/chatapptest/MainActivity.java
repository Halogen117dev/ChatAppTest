package com.example.chatapptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    String yourName;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String username = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(username);

        userAdapter = new UserAdapter(this);
        recyclerView = findViewById(R.id.recycler);

        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String uId = dataSnapshot.getKey();
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if((userModel != null) && (userModel.getUserID() != null) && !(userModel.getUserID().equals(FirebaseAuth.getInstance().getUid())))
                    {
                        userAdapter.add(userModel);
                    }
                }

                List<UserModel> userModelList = userAdapter.getUserModelList();
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();

            return true;
        }

        return false;
    }

}