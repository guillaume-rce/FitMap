package com.oniverse.fitmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oniverse.fitmap.modules.chat.Chat;
import com.oniverse.fitmap.modules.chat.ChatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Chat> chats;
    private ChatAdapter chatAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlistview);

        recyclerView = findViewById(R.id.conversationlistView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chats = new ArrayList<>();
        chatAdapter = new ChatAdapter(chats);
        recyclerView.setAdapter(chatAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadChats();
    }

    private void loadChats() {
        // Récupérez les chats depuis Firebase ici
        // Par exemple, vous pouvez récupérer les chats en fonction de l'ID de l'utilisateur actuel
    }
}
