package com.oniverse.fitmap;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.modules.chat.Message;
import com.oniverse.fitmap.modules.chat.MessageAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);

        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });

        // Configuration du bouton d'envoi de message
        ImageButton buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextMessage = findViewById(R.id.editText_message);
                String messageText = editTextMessage.getText().toString();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        String messageId = messagesRef.push().getKey();
        Message message = new Message(messageText, new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.FRANCE).format(new Date()), "senderId", true);
        messagesRef.child(messageId).setValue(message);
    }

}
