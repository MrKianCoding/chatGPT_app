package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText editTextMessage;
    TextView textViewWelcome;
    ImageView buttonSend;
    RecyclerView recyclerView;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objectAssignment();
        initAdapter();


        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            initChat(message, "person");
            callChatGPTApi(message);
            editTextMessage.setText("");
            textViewWelcome.setVisibility(View.GONE);
        });

    }

    private void objectAssignment() {
        editTextMessage = findViewById(R.id.editTextMessage);
        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonSend = findViewById(R.id.buttonSend);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initAdapter() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initChat(String message, String sender) {
        messageList.add(new Message(message, sender));
        messageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
    }

    private void callChatGPTApi(String message) {
        messageList.add(new Message("chatGPT is typing ...", "bot"));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", message);
            jsonObject.put("max_tokens", 4000);
            jsonObject.put("temperature", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "https://api.openai.com/v1/completions", jsonObject, response -> {
            try {
                String result = response.getJSONArray("choices").getJSONObject(0).getString("text");
                initResponse(result.trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            initResponse("Failed" + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer your token api");
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void initResponse(String response) {
        messageList.remove(messageList.size() - 1);
        initChat(response, "bot");
    }
}