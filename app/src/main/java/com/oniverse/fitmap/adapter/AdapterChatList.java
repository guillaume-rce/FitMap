package com.oniverse.fitmap.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.oniverse.fitmap.ChatActivity;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.models.ModelUsers;
import com.oniverse.fitmap.modules.tracks.Track;

import java.util.HashMap;
import java.util.List;
/**
 * AdapterChatList.java
 * Purpose: Adapter for chatlist in ChatActivity
 * Description: This class is used to create the adapter for the chat list in the ChatActivity.
 * It is used to display the chat messages in the chat list.
 */
public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.Myholder> {

    Context context;
    FirebaseAuth firebaseAuth;
    String uid;
    Track track;
    /**
     * Constructor
     * This method is used to create the adapter for the chat list in the ChatActivity.
     * @param context The context of the activity
     * @param users The list of users
     * @return void
     */
    public AdapterChatList(Context context, List<ModelUsers> users) {
        this.context = context;
        this.usersList = users;
        lastMessageMap = new HashMap<>();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    List<ModelUsers> usersList;
    private HashMap<String, String> lastMessageMap;
    /**
     * onCreateViewHolder
     * This method is used to create the view holder for the chat list in the ChatActivity.
     * @param parent The parent view group
     * @param viewType The view type
     * @return Myholder
     */
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new Myholder(view);
    }
    /**
     * onBindViewHolder
     * This method is used to bind the view holder for the chat list in the ChatActivity.
     * @param holder The view holder
     * @param position The position
     * @return void
     */
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {

        final String hisuid = usersList.get(position).getUid();
        String userimage = usersList.get(position).getImage();
        String username = usersList.get(position).getName();
        String lastmess = lastMessageMap.get(hisuid);
        holder.name.setText(username);
        holder.block.setImageResource(R.drawable.unlock);

        // if no last message then Hide the layout
        if (lastmess == null || lastmess.equals("default")) {
            holder.lastmessage.setVisibility(View.GONE);
        } else {
            holder.lastmessage.setVisibility(View.VISIBLE);
            holder.lastmessage.setText(lastmess);
        }
        try {
            // loading profile pic of user
            Glide.with(context).load(userimage).into(holder.profile);
        } catch (Exception e) {

        }

        // redirecting to chat activity on item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);

                // putting uid of user in extras
                intent.putExtra("uid", hisuid);
                if (track != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("track", track);
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
            }
        });

    }
    /*
        * setlastMessageMap
        * This method is used to set the last message map.
        * @param userId The user id
     */
    public void setlastMessageMap(String userId, String lastmessage) {
        lastMessageMap.put(userId, lastmessage);
    }
    /**
     * getItemCount
     * This method is used to get the item count for the chat list in the ChatActivity.
     * @return int
     */
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * Myholder
     * Purpose: View holder for chat list in ChatActivity
     * Description: This class is used to create the view holder for the chat list in the ChatActivity.
     */
    class Myholder extends RecyclerView.ViewHolder {
        ImageView profile, status, block, seen;
        TextView name, lastmessage;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileimage);
            status = itemView.findViewById(R.id.onlinestatus);
            name = itemView.findViewById(R.id.nameonline);
            lastmessage = itemView.findViewById(R.id.lastmessge);
            block = itemView.findViewById(R.id.blocking);
            seen = itemView.findViewById(R.id.seen);
        }
    }
}

