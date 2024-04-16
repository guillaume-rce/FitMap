package com.oniverse.fitmap.modules.chat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
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
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.getContent());
        // Gérer l'affichage du fichier si l'URL est présente
        if (message.getFileUrl() != null) {
            // Vérifier si l'URL est une image ou une vidéo
            if (message.getFileUrl().endsWith(".jpg") || message.getFileUrl().endsWith(".png")) {
                // Charger l'image
                Glide.with(holder.messageImageView.getContext())
                        .load(message.getFileUrl())
                        .into(holder.messageImageView);
                holder.messageImageView.setVisibility(View.VISIBLE);
            } else if (message.getFileUrl().endsWith(".mp4")) {
                // Charger la vidéo
                holder.messageVideoView.setVideoURI(Uri.parse(message.getFileUrl()));
                holder.messageVideoView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSent() ? 0 : 1;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        VideoView messageVideoView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textView_message);
            messageImageView = itemView.findViewById(R.id.imageView_message); // Assurez-vous que ces IDs existent dans vos layouts
            messageVideoView = itemView.findViewById(R.id.videoView_message);
        }
    }
}