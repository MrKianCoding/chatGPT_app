package com.example.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.messageViewHolder> {

    List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new messageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.sender.equals("person")){
            holder.textViewBot.setVisibility(View.GONE);
            holder.textViewPerson.setVisibility(View.VISIBLE);
            holder.textViewPerson.setText(message.message);
        } else {
            holder.textViewPerson.setVisibility(View.GONE);
            holder.textViewBot.setVisibility(View.VISIBLE);
            holder.textViewBot.setText(message.message);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBot, textViewPerson;

        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBot = itemView.findViewById(R.id.textViewBot);
            textViewPerson = itemView.findViewById(R.id.textViewPerson);
        }
    }
}
