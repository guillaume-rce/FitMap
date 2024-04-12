package com.oniverse.fitmap;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionActivity extends AppCompatActivity {
    private ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
