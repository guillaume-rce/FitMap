package com.oniverse.fitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.oniverse.fitmap.databinding.ActivityMainBinding;
import com.oniverse.fitmap.modules.tracks.TrackList;
import com.oniverse.fitmap.modules.tracks.api.ApiClient;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int total_page;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        System.out.println("-------------------------------------------------------");
        ApiClient.setMetaData(() -> {
            loadTracks();
            return null;
        });
    }

    public void loadTracks() {
        total_page = 5;
        for (int i = 1; i <= total_page; i++) {
            ApiClient.findTracks(i, true, this, () -> {
                setProgressBarValue();
                switchToHomeActivity();
                return null;
            });
        }
    }

    public void setProgressBarValue() {
        System.out.println("PROGRESS ---------------------------------------------------");
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        int total = TrackList.getInstance().getTotalWithGpx();
        int perPage = TrackList.getInstance().getApiMetaData().perPage;
        progress = (total * 100) / (total_page * perPage);
        System.out.println(progress);
        progressBar.setProgress(progress);
    }

    public void switchToHomeActivity() {
        if (progress >= 100) {
            System.out.println("All tracks loaded");
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
