package com.oniverse.fitmap.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.TrackActivity;
import com.oniverse.fitmap.models.ModelChat;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    * AdapterChat.java
    * Purpose: Adapter for chatlist in ChatActivity
    * Description: This class is used to create the adapter for the chat list in the ChatActivity.
    * It is used to display the chat messages in the chat list.
 */
public class AdapterChat extends RecyclerView.Adapter<com.oniverse.fitmap.adapter.AdapterChat.Myholder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPR_RIGHT = 1;
    Context context;
    List<ModelChat> list;
    String imageurl;
    FirebaseUser firebaseUser;
    boolean isTrack = false;
    Track track = null;

    /**
     * Constructor
     * This method is used to create the adapter for the chat list in the ChatActivity.
     * @param context The context of the activity
     * @param list The list of chat messages
     * @param imageurl The image URL
     * @return void
     */
    public AdapterChat(Context context, List<ModelChat> list, String imageurl) {
        this.context = context;
        this.list = list;
        this.imageurl = imageurl;
    }

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
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new Myholder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_rigth, parent, false);
            return new Myholder(view);
        }
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
        String message = list.get(position).getMessage();
        String timeStamp = list.get(position).getTimestamp();
        String type = list.get(position).getType();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.message.setText(message);
        holder.time.setText(timedate);

        try {
            Glide.with(context).load(imageurl).into(holder.image);
        } catch (Exception e) { e.printStackTrace(); }
        if (type.equals("text")) {
            if (message.contains("TrackId:")) {
                // Get the id next to TrackId:
                String trackId = message.substring(message.indexOf("TrackId:") + 8);
                track = TrackList.getInstance().getTrack(Long.parseLong(trackId));
                if (track != null) {
                    isTrack = true;
                    message = "Track: " + track.name;
                } else {
                    message = "Unknown track with id " + trackId;
                }
            }
            holder.message.setVisibility(View.VISIBLE);
            holder.mimage.setVisibility(View.GONE);
            holder.message.setText(message);

        } else {
            holder.message.setVisibility(View.GONE);
            holder.mimage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message).into(holder.mimage);
        }

        holder.msglayput.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick
             * This method is used to handle the click event for the chat list in the ChatActivity.
             * @param v The view
             * @return void
             */
            @Override
            public void onClick(View v) {
                if (!isTrack) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Message");
                    builder.setMessage("Are You Sure To Delete This Message");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        /**
                         * onClick
                         * This method is used to handle the click event for the chat list in the ChatActivity.
                         *
                         * @param dialog The dialog
                         * @param which  The which
                         * @return void
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMsg(position);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        /**
                         * onClick
                         * This method is used to handle the click event for the chat list in the ChatActivity.
                         *
                         * @param dialog The dialog
                         * @param which  The which
                         * @return void
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    Intent intent = new Intent(context, TrackActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("track", track);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }
/*
    * deleteMsg
    * This method is used to delete the message from the chat list in the ChatActivity.
    * @param position The position
    * @return void
 */
    private void deleteMsg(int position) {
        final String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgtimestmp = list.get(position).getTimestamp();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chats");
        Query query = dbref.orderByChild("timestamp").equalTo(msgtimestmp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * onDataChange
             * This method is used to handle the data change event for the chat list in the ChatActivity.
             * @param dataSnapshot The data snapshot
             * @return void
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("sender").getValue().equals(myuid)) {
                        // any two of below can be used
                        dataSnapshot1.getRef().removeValue();
					/* HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("message", "This Message Was Deleted");
						dataSnapshot1.getRef().updateChildren(hashMap);
						Toast.makeText(context,"Message Deleted.....",Toast.LENGTH_LONG).show();
*/
                    } else {
                        Toast.makeText(context, "you can delete only your msg....", Toast.LENGTH_LONG).show();
                    }
                }
            }
            /**
             * onCancelled
             * This method is used to handle the cancelled event for the chat list in the ChatActivity.
             * @param databaseError The database error
             * @return void
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * getItemCount
     * This method is used to get the item count for the chat list in the ChatActivity.
     * @return int
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
    /*
    * getItemViewType
    * This method is used to get the item view type for the chat list in the ChatActivity.
    * @param position The position
    * @return int
     */
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPR_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
    /**
     * Myholder
     * This class is used to create the view holder for the chat list in the ChatActivity.
     */
    class Myholder extends RecyclerView.ViewHolder {

        CircleImageView image;
        ImageView mimage;
        TextView message, time, isSee;
        LinearLayout msglayput;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilec);
            message = itemView.findViewById(R.id.msgc);
            time = itemView.findViewById(R.id.timetv);
            isSee = itemView.findViewById(R.id.isSeen);
            msglayput = itemView.findViewById(R.id.msglayout);
            mimage = itemView.findViewById(R.id.images);
        }
    }
}

