package com.oniverse.fitmap.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.adapter.UserSearchAdapter;
import com.oniverse.fitmap.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class UserSearchFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    List<ModelUsers> modelUsers;

    EditText searchUsers;

    public UserSearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.userRecyclerView);
        searchUsers = view.findViewById(R.id.searchNewUserEditText);

        // Initialiser la liste des utilisateurs
        modelUsers = new ArrayList<>();

        // Ajouter un listener pour la recherche
        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadUsersWithEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void loadUsersWithEmail(String searchText) {
        // Filtrer les utilisateurs en fonction de l'email ou du nom
        Query query = FirebaseDatabase.getInstance("https://fitmap-cb19a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
                .orderByChild("email")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelUsers user = ds.getValue(ModelUsers.class);
                    if (user != null && !user.getUid().equals(firebaseUser.getUid())) {
                        modelUsers.add(user);
                    }
                }
                // Mettre à jour l'adapter avec les nouveaux résultats
                updateRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateRecyclerView() {
        UserSearchAdapter adapter = new UserSearchAdapter(modelUsers);
        recyclerView.setAdapter(adapter);

    }
}
