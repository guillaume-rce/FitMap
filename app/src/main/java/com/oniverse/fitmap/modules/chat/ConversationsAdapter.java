package com.oniverse.fitmap.modules.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.oniverse.fitmap.R;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    private List<Conversation> conversations;

    public ConversationsAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);

        holder.itemUsername.setText("username");
        holder.lastMessage.setText(conversation.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView itemUsername;
        TextView lastMessage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            itemUsername = itemView.findViewById(R.id.chatrecyclerView);
            lastMessage = itemView.findViewById(R.id.chatlistlastmessge);
        }
    }
}
