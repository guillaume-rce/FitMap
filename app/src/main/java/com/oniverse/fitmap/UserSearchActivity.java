package com.oniverse.fitmap;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.adapter.UserSearchAdapter;
import com.oniverse.fitmap.databinding.ActivityUserSearchBinding;
import com.oniverse.fitmap.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    List<ModelUsers> modelUsers;

    EditText searchUsers;

    private ActivityUserSearchBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        binding = ActivityUserSearchBinding.inflate(getLayoutInflater());
          View view = binding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.userRecyclerView);
        searchUsers = findViewById(R.id.searchNewUserEditText);

        // recuperer la valeur de l'edit text
        searchUsers.getText().toString().trim();

        // rechercher l'utilisateur dans la base de données et recuperer son id
        reference = FirebaseDatabase.getInstance().getReference("Users");

        // lancer une requete pour recuperer toutes les infos de l'utilisateur

        // remplir la recyclerView avec les infos de l'utilisateur dans ce layout


}
