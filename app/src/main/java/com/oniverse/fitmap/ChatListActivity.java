package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oniverse.fitmap.models.ChatListFragment;
import com.oniverse.fitmap.service.Localisation;

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
        Intent localisationIntent = new Intent(this, Localisation.class);
        startService(localisationIntent);

        // ---------------- Add navbar ----------------
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.navigation_explore);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    // Stop the service
                    stopService(localisationIntent);
                    startActivity(new Intent(ChatListActivity.this, HomeActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_explore) {
                    stopService(localisationIntent);
                    startActivity(new Intent(ChatListActivity.this, ExploreActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_chat) {
                    // Stop the service
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
