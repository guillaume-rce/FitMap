package com.oniverse.fitmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.modules.chat.Conversation;
import com.oniverse.fitmap.modules.chat.ConversationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversations;
    private DatabaseReference conversationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlistview);

        recyclerView = findViewById(R.id.conversationlistView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(conversations);
        recyclerView.setAdapter(conversationAdapter);

        conversationsRef = FirebaseDatabase.getInstance().getReference("conversations");
        conversationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversations.clear();
                for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren()) {
                    Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                    conversations.add(conversation);
                }
                conversationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }
}
