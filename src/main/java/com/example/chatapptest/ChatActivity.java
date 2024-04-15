package com.example.chatapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

public class ChatActivity extends AppCompatActivity
{
    String receiverId, receiverName;
    String senderId, senderName;

    DatabaseReference dbRefSender, dbRefReceiver, userReference;

    ImageView sendButton;
    EditText messageText;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}