package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.modules.chat.Chat;
import com.oniverse.fitmap.modules.chat.ChatListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Chat> chats;
    private ChatListAdapter chatListAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlistview);

        recyclerView = findViewById(R.id.conversationlistView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chats = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chats);
        recyclerView.setAdapter(chatListAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadChats(currentUser.getUid());
        } else {
            // Rediriger vers la page de connexion
            startActivity(new Intent(ChatListActivity.this, SigninActivity.class));
        }

        // ---------------- Add navbar ----------------
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.navigation_chat);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(ChatListActivity.this, HomeActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_explore) {
                    startActivity(new Intent(ChatListActivity.this, ExploreActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_chat) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    private void loadChats(String uid) {
        mDatabase.child("ChatList").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    chats.add(chat);
                }
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }

    // ecrire une fonction qui permet d'ajouter un chat
    private void addChat(String uid) {
        Chat chat = new Chat(uid);
        mDatabase.child("ChatList").child(uid).push().setValue(chat);
        // rediriger vers la page de chat
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
    }
}
