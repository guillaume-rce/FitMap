package com.oniverse.fitmap.modules.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.ChatActivity;
import com.oniverse.fitmap.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<Chat> chats;
    private DatabaseReference mDatabase;

    public ChatListAdapter(List<Chat> chats) {
        this.chats = chats;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item, parent, false);
        return new ChatViewHolder(view, parent.getContext()); // Passer le contexte
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.usernameTextView.setText("userdestination");
        holder.lastMessageTextView.setText("lastmessage");

        // Charger les informations de l'utilisateur
        loadUserInfo(chat.getUserId(), holder);

        // Charger le dernier message
        loadLastMessage(chat.getUserId(), holder); // Assurez-vous d'utiliser l'ID du chat correct

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, ChatActivity.class); // Utiliser le contexte
                intent.putExtra("chatId", chat.getUserId()); // Assurez-vous d'utiliser l'ID du chat correct
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, lastMessageTextView;
        Context context; // Ajouter un champ pour le contexte

        public ChatViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context; // Initialiser le contexte
            usernameTextView = itemView.findViewById(R.id.textView);
            lastMessageTextView = itemView.findViewById(R.id.textView2);
        }
    }

    private void loadUserInfo(String userId, ChatViewHolder holder) {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                // Mettez à jour l'affichage de l'utilisateur dans l'élément de la liste
                holder.usernameTextView.setText(user.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }

    private void loadLastMessage(String chatId, ChatViewHolder holder) {
        mDatabase.child("Chats").child(chatId).child("lastMessage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMessage = dataSnapshot.getValue(String.class);
                // Mettez à jour l'affichage du dernier message dans l'élément de la liste
                holder.lastMessageTextView.setText(lastMessage); // Exemple
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }
}
