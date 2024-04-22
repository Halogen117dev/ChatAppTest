package com.example.chatapptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity
{
    String receiverId, receiverName, senderRoom, receiverRoom;
    DatabaseReference dbRefSender, dbRefReceiver, userReference;

    ImageView sendButton;
    EditText messageText;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        userReference = FirebaseDatabase.getInstance().getReference("users");
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(receiverName);
        if(receiverId!=null)
        {
            senderRoom = FirebaseAuth.getInstance().getUid()+receiverId;
            receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();
        }

        sendButton = findViewById(R.id.sendmessageicon);
        messageAdapter = new MessageAdapter(this);
        recyclerView = findViewById(R.id.chatrecycler);
        messageText = findViewById(R.id.messageedit);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbRefSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        dbRefReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        dbRefSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                List<MessageModel> messages = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }
                messageAdapter.clear();
                Collections.sort(messages, new Comparator<MessageModel>() {
                    @Override
                    public int compare(MessageModel o1, MessageModel o2) {
                        return o1.getTimestamp().compareTo(o2.getTimestamp());
                    }
                });
                for(MessageModel message: messages)
                {
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String message = messageText.getText().toString();
                if (message.trim().length() > 0) {
                    SendMessage(message);
                } else {
                    Toast.makeText(ChatActivity.this, "Empty message!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void SendMessage(String message)
    {
        String messageId = UUID.randomUUID().toString();
        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), message);
        messageAdapter.add(messageModel);
        dbRefSender.child(messageId).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused)
                    {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(ChatActivity.this, "Failed to send messaeg", Toast.LENGTH_SHORT).show();

                    }
                });

        dbRefReceiver.child(messageId).setValue(messageModel);

        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        messageText.setText("");
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
            startActivity(new Intent(ChatActivity.this, SignUpActivity.class));
            finish();
            return true;
        }

        return false;
    }
}