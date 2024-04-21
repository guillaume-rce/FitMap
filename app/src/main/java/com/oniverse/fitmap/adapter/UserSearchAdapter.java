package com.oniverse.fitmap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.models.ModelUsers;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder> {

    private List<ModelUsers> userList;

    public UserSearchAdapter(List<ModelUsers> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usersearch, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ModelUsers user = userList.get(position);
        holder.nameOnline.setText(user.getName());
        holder.profileImage.setImageResource(R.drawable.ic_profile);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nameOnline;
        CheckBox selectUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileimage);
            nameOnline = itemView.findViewById(R.id.nameonline);
            selectUser = itemView.findViewById(R.id.select_user);
        }
    }
}
