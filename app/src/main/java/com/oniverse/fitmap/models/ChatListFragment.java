package com.oniverse.fitmap.models;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.ChatActivity;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.adapter.AdapterChatList;
import com.oniverse.fitmap.fragment.UserSearchFragment;
import com.oniverse.fitmap.models.ModelChat;
import com.oniverse.fitmap.models.ModelChatList;
import com.oniverse.fitmap.modules.tracks.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelChatList> chatListList;
    List<ModelUsers> usersList;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    AdapterChatList adapterChatList;
    List<ModelChat> chatList;
    ImageButton startNewConversationButton;
    Track track;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatListFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the track
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            track = (Track) bundle.getSerializable("track");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        startNewConversationButton = view.findViewById(R.id.startNewConversationButton);
        startNewConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une boîte de dialogue pour demander l'email
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Start New Conversation");

                // Conception du champ de saisie
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                // Ajouter les boutons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        // Vérifier l'existence de l'email dans la base de données
                        checkEmailExistence(email);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // getting current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.chatlistrecycle);
        chatListList = new ArrayList<>();
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://fitmap-cb19a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatListList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChatList modelChatList = ds.getValue(ModelChatList.class);
                    if (!modelChatList.getId().equals(firebaseUser.getUid())) {
                        chatListList.add(modelChatList);
                    }

                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;

    }

    /**
     * Load chats
     */
    private void loadChats() {
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://fitmap-cb19a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelUsers user = dataSnapshot1.getValue(ModelUsers.class);
                    for (ModelChatList chatList : chatListList) {
                        if (user.getUid() != null && user.getUid().equals(chatList.getId())) {
                            usersList.add(user);
                            break;
                        }
                    }
                    adapterChatList = new AdapterChatList(getActivity(), usersList);
                    if (track != null) {
                        adapterChatList.setTrack(track);
                    }
                    recyclerView.setAdapter(adapterChatList);

                    // getting last message of the user
                    for (int i = 0; i < usersList.size(); i++) {
                        lastMessage(usersList.get(i).getUid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Get the last message
     *
     * @param uid The user id.
     */
    private void lastMessage(final String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://fitmap-cb19a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastmess = "default";
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelChat chat = dataSnapshot1.getValue(ModelChat.class);
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    // checking for the type of message if
                    // message type is image then set
                    // last message as sent a photo
                    if (chat.getReceiver().equals(firebaseUser.getUid()) &&
                            chat.getSender().equals(uid) ||
                            chat.getReceiver().equals(uid) &&
                                    chat.getSender().equals(firebaseUser.getUid())) {
                        if (chat.getType().equals("images")) {
                            lastmess = "Sent a Photo";
                        } else {
                            lastmess = chat.getMessage();
                        }
                    }
                }
                adapterChatList.setlastMessageMap(uid, lastmess);
                adapterChatList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkEmailExistence(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // L'email existe, récupérer l'ID de l'utilisateur
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        // Ouvrir la page de chat avec l'ID de l'utilisateur
                        openChatPage(userId);
                        break; // Sortir de la boucle après avoir trouvé le premier utilisateur correspondant
                    }
                } else {
                    // L'email n'existe pas, afficher un message d'erreur
                    Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
                }
            }
            private void openChatPage(String userId) {
                // Ici, vous pouvez naviguer vers une nouvelle activité ou un fragment de chat
                // Par exemple, en utilisant un Intent pour démarrer une nouvelle activité
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("uid", userId);
                startActivity(intent);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });
    }


    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}
