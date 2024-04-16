package com.oniverse.fitmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oniverse.fitmap.databinding.ActivityChatBinding;
import com.oniverse.fitmap.modules.chat.Message;
import com.oniverse.fitmap.modules.chat.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private ActivityChatBinding binding_chat;
    private String recieverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_chat = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding_chat.getRoot());

        // Initialisation de la référence à la base de données
        mDatabase = FirebaseDatabase.getInstance("https://fitmap-bee8d-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        recyclerView = findViewById(R.id.chatrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recieverId = "5LlfMhD3hwaBPb3V3EIwnYO8Cts2";
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);

        // Récupérer les messages existants et écouter les nouveaux messages
        mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear(); // Nettoyer la liste des messages
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (!messages.contains(message)) {
                        messages.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });

        ImageButton sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextMessage = findViewById(R.id.editText_message);
                String messageContent = editTextMessage.getText().toString();
                if (!messageContent.isEmpty()) {
                    // Envoyer le message à Firebase
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        sendMessage(messageContent, currentUser.getUid(), "");
                        // Effacer l'EditText après l'envoi
                        editTextMessage.setText("");
                    } else {
                        Toast.makeText(ChatActivity.this, "Veuillez vous connecter pour envoyer un message.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ImageButton returbutton = findViewById(R.id.chatreturnButton);
        returbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton attachmentButton = findViewById(R.id.attachment_button);
        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                startActivityForResult(Intent.createChooser(intent, "Choisissez un fichier"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        if (fileUri != null) {
            StorageReference fileRef = mStorageRef.child("chat_files/" + fileUri.getLastPathSegment());
            fileRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Obtenir l'URL du fichier téléchargé
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Envoyer l'URL du fichier à Firebase Database
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                sendMessage(downloadUri.toString(), currentUser.getUid(),recieverId);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Gérer les erreurs de téléchargement
                    Toast.makeText(ChatActivity.this, "Erreur de téléchargement du fichier.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Méthode pour envoyer un message
    private void sendMessage(String messageContent, String userId,String recieverId) {
        String key = mDatabase.child("Messages").push().getKey();
        Message message = new Message(userId, messageContent, true, System.currentTimeMillis(), recieverId);

        // Envoi du message à Firebase
        mDatabase.child("Messages").child(key).setValue(message);

        // Ajout du message à la liste locale pour mettre à jour l'affichage
        // Vérifiez si le message existe déjà dans la liste
        if (!messages.contains(message)) {
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
        }
    }
}
