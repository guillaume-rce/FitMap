package com.oniverse.fitmap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.oniverse.fitmap.models.ChatListFragment;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list); // Assurez-vous d'avoir un layout pour cette activité

        // Vérifiez si l'activité est créée pour la première fois
        if (savedInstanceState == null) {
            // Ajoutez le fragment au conteneur de fragment
            ChatListFragment chatListFragment = new ChatListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, chatListFragment);
            transaction.commit();
        }
    }
}
