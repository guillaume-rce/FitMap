package com.oniverse.fitmap;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.request_listener.NexmoConnectionListener;

public class ChatActivity extends AppCompatActivity {

    private NexmoClient client;

    private ConstraintLayout chatContainer;
    private LinearLayout loginContainer;
    private EditText messageEditText;
    private TextView conversationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); // Ajout de cette ligne pour définir le layout de l'activité
        chatContainer = findViewById(R.id.chatContainer);
        loginContainer = findViewById(R.id.loginContainer);
        messageEditText = findViewById(R.id.messageEditText);
        conversationTextView = findViewById(R.id.conversationTextView);
        client = new NexmoClient.Builder().build(this);
        client.setConnectionListener((connectionStatus, connectionStatusReason) -> {
            if (connectionStatus == NexmoConnectionListener.ConnectionStatus.CONNECTED) {
                Toast.makeText(this, "User connected", Toast.LENGTH_SHORT).show();

                getConversation();
            } else if (connectionStatus == NexmoConnectionListener.ConnectionStatus.DISCONNECTED) {
                Toast.makeText(this, "User disconnected", Toast.LENGTH_SHORT).show();

                runOnUiThread(() -> {
                    chatContainer.setVisibility(View.GONE);
                    loginContainer.setVisibility(View.VISIBLE);
                });
            }
        });
        findViewById(R.id.loginAsAliceButton).setOnClickListener(it -> {
            client.login();

            runOnUiThread(() -> loginContainer.setVisibility(View.GONE));
        });

        findViewById(R.id.loginAsBobButton).setOnClickListener(it -> {
            client.login();

            runOnUiThread(() -> loginContainer.setVisibility(View.GONE));
        });

        findViewById(R.id.logoutButton).setOnClickListener(it -> client.logout());

    }
    private void getConversation() { }

}
