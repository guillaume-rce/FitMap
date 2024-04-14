package com.oniverse.fitmap.modules.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oniverse.fitmap.R;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<Conversation> conversations;

    public ConversationAdapter(List<Conversation> conversations) {
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
        holder.textViewUsername.setText(conversation.getUserId());
        holder.textViewLastMessage.setText(conversation.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewLastMessage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textView);
            textViewLastMessage = itemView.findViewById(R.id.textView2);
        }
    }
}
