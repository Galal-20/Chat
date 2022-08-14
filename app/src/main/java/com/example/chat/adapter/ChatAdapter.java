package com.example.chat.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.databinding.ItemContanierReceivedMessageBinding;
import com.example.chat.databinding.ItemContanierSentMessageBinding;
import com.example.chat.models.ChatMessage;
import com.example.chat.utilities.Constants;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final Bitmap receiverProfileImage;
    private final String  senderId;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_SENT){
            return new SendMessageViewHolder(
                    ItemContanierSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }else {
            return new ReceivedMessageViewHolder(
                    ItemContanierReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.VIEW_TYPE_SENT){
            ((SendMessageViewHolder) holder).setData(chatMessages.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position) , receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)){
            return Constants.VIEW_TYPE_SENT;
        }else {
            return Constants.VIEW_TYPE_RECEIVED;
        }
    }

    static class SendMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContanierSentMessageBinding bindind;

        SendMessageViewHolder(ItemContanierSentMessageBinding itemContanierSentMessageBinding) {
            super(itemContanierSentMessageBinding.getRoot());
            bindind = itemContanierSentMessageBinding;
        }
        void setData(ChatMessage chatMessage){
            bindind.textMessage.setText(chatMessage.message);
            bindind.textDateTime.setText(chatMessage.dateTime);
        }
    }
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContanierReceivedMessageBinding bindind;

        ReceivedMessageViewHolder(ItemContanierReceivedMessageBinding itemContanierReceivedMessageBinding){
            super(itemContanierReceivedMessageBinding.getRoot());
            bindind = itemContanierReceivedMessageBinding;
        }
        void setData(ChatMessage chatMessage , Bitmap receiverProfileImage){
            bindind.textMessage.setText(chatMessage.message);
            bindind.textDateTime.setText(chatMessage.dateTime);
            bindind.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
