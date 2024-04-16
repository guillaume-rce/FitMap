package com.oniverse.fitmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oniverse.fitmap.databinding.ActivityFavoriteBinding;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding_favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_favorite = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding_favorite.getRoot());
    }
}