package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.databinding.ActivityChatBinding;
import com.oniverse.fitmap.modules.chat.Message;
import com.oniverse.fitmap.modules.chat.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private DatabaseReference mDatabase;
    private ActivityChatBinding binding_chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_chat = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding_chat.getRoot());
        setContentView(R.layout.activity_chat);

        // Initialisation de la référence à la base de données
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);

        // Récupérer les messages existants une seule fois au démarrage
        mDatabase.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (!messages.contains(message)) {
                        messages.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });

        // Écoute des nouveaux messages
        mDatabase.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (!messages.contains(message)) {
                    messages.add(message);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            // Implémentez les autres méthodes de ChildEventListener
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Ecouter les nouveaux messages
        listenForNewMessages();
        ImageButton sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextMessage = findViewById(R.id.editText_message);
                String messageContent = editTextMessage.getText().toString();
                if (!messageContent.isEmpty()) {
                    // Envoyer le message à Firebase
                    sendMessage(messageContent, "userId"); // Remplacez "userId" par l'ID de l'utilisateur actuel
                    // Effacer l'EditText après l'envoi
                    editTextMessage.setText("");
                }
            }
        });

        ImageButton returnButton = findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    // Méthode pour envoyer un message
    private void sendMessage(String messageContent, String userId) {
        // Création d'un nouvel enfant avec un ID unique
        String key = mDatabase.child("messages").push().getKey();
        Message message = new Message(userId, messageContent, true, System.currentTimeMillis(), userId);

        // Envoi du message à Firebase
        mDatabase.child("messages").child(key).setValue(message);

        // Ajout du message à la liste locale pour mettre à jour l'affichage
        // Vérifiez si le message existe déjà dans la liste
        if (!messages.contains(message)) {
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
        }
    }

    // implementer la méthode pour ecouter les nouveaux messages
     private void listenForNewMessages() {
        mDatabase.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);

                // ajouter la proprieté de message reçu
                message.setSent(false);
                if (!messages.contains(message)) {
                    messages.add(message);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            // Implémentez les autres méthodes de ChildEventListener
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
