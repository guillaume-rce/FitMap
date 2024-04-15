package com.oniverse.fitmap.modules.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oniverse.fitmap.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == 0 ? R.layout.message_sent : R.layout.message_sent;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MessageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTimestamp.setText(message.getTimestamp());
        // Aucune logique supplémentaire nécessaire ici, car l'apparence est gérée par le layout
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage, textViewTimestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textView_message);
            textViewTimestamp = itemView.findViewById(R.id.textView_timestamp);
        }
    }
    @Override
    public int getItemViewType(int position) {

        return messages.get(position).isSent() ? 0 : 1;
    }


}
