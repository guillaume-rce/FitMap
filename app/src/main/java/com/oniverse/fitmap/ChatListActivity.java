package com.oniverse.fitmap;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.modules.chat.Conversation;
import com.oniverse.fitmap.modules.chat.ConversationsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationsAdapter conversationAdapter;
    private List<Conversation> conversations;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        mDatabase = FirebaseDatabase.getInstance("https://fitmap-bee8d-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        recyclerView = findViewById(R.id.conversationlistrecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationsAdapter(conversations);
        recyclerView.setAdapter(conversationAdapter);

        ImageButton optionsButton = findViewById(R.id.addconversation);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConversation("admins@oniverse.com");
            }
        });

        loadConversations();
        conversationAdapter.notifyDataSetChanged();

    }

    private void loadConversations() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            mDatabase.child("Conversations").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    conversations.clear();
                    for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren()) {
                        Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                        if (conversation != null && (conversation.getCurrentUserId().equals(currentUser.getUid()) || (conversation.getOtherUserId().equals(currentUser.getUid())))) {
                            conversations.add(conversation);
                        }
                    }
                    conversationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }


    public void addConversation(String email) {
      //ajouter un chat_item à la recycleview

        //ajouter une conversation à la base de données
        String conversationId = mDatabase.child("Conversations").push().getKey();
        conversationToDatabase(conversationId, "0wdOnJ5nj7T13kBU3l0Mr3VKFuf1", "K7y0c9qtxWU0rRaNq2SMdO3sQNa2");


    }
    private void conversationToDatabase(String conversationId, String currentUserId, String otherUserId) {
        Conversation newConversation = new Conversation(conversationId, currentUserId, otherUserId);
        mDatabase.child("Conversations").child(conversationId).setValue(newConversation)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Conversation enregistrée dans la base de données avec succès");
                    Toast.makeText(ChatListActivity.this, "Sauvegarde réussie", Toast.LENGTH_SHORT).show();
                    conversations.add(new Conversation("1", "0wdOnJ5nj7T13kBU3l0Mr3VKFuf1", "K7y0c9qtxWU0rRaNq2SMdO3sQNa2"));
                    conversationAdapter.notifyItemInserted(conversations.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de l'enregistrement de la conversation dans la base de données : " + e.getMessage(), e);
                    Toast.makeText(ChatListActivity.this, "Erreur lors de l'enregistrement de la conversation dans la base de données: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
