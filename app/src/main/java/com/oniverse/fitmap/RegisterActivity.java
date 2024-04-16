package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Importez Log pour les journaux
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oniverse.fitmap.databinding.ActivityRegisterBinding;
import com.oniverse.fitmap.modules.chat.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; // Définir une étiquette pour les journaux

    private ActivityRegisterBinding binding_register;
    private EditText nameEditText, firstNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialise Firebase App Check
        FirebaseApp.initializeApp(this);

        binding_register = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding_register.getRoot());

        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        nameEditText = findViewById(R.id.nameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signInButton);
        signInTextView = findViewById(R.id.signUpTextView);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://fitmap-bee8d-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || firstName.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUp(email, password, name, firstName);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        TextView alreadyHaveAccount = findViewById(R.id.signUpTextView);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,SigninActivity.class));
                finish();
            }
        });
    }
    private void signUp(String email, String password, String name, String firstName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserToDatabase(user, name, firstName);
                    } else {
                        Log.e(TAG, "L'utilisateur est null");
                        Toast.makeText(RegisterActivity.this, "L'utilisateur est null", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        Log.e(TAG, "L'adresse e-mail est déjà utilisée par un autre compte.", e);
                        Toast.makeText(RegisterActivity.this, "L'adresse e-mail est déjà utilisée par un autre compte.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Erreur lors de la création de l'utilisateur : " + e.getMessage(), e);
                        Toast.makeText(RegisterActivity.this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(FirebaseUser user, String name, String firstName) {
        User newUser = new User(user.getUid(), name, firstName, user.getEmail());
        mDatabase.child("Users").child(user.getUid()).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Utilisateur enregistré dans la base de données avec succès");
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de l'enregistrement de l'utilisateur dans la base de données : " + e.getMessage(), e);
                    Toast.makeText(RegisterActivity.this, "Erreur lors de l'inscription de l'utilisateur dans la base de données: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
