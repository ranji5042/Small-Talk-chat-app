package com.example.smalltalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smalltalk.ChatActivity;
import com.example.smalltalk.R;
import com.example.smalltalk.model.Chatroom;
import com.example.smalltalk.model.UserModel;
import com.example.smalltalk.utils.AndroidUtil;
import com.example.smalltalk.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<Chatroom,RecentChatRecyclerAdapter.ChatroomViewHolder> {
    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Chatroom> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position, @NonNull Chatroom model) {
       FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
               .get().addOnCompleteListener(task -> {
                  if(task.isSuccessful()){
                      UserModel otherUserModel =task.getResult().toObject(UserModel.class);
                      holder.usernametext.setText(otherUserModel.getUsername());
                      holder.lastMessageText.setText(model.getLastMessage());
                      holder.lastMessageTime.setText(model.getLastMessageTimestamp().toString());
                  }
               });
    }

    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row,parent,false);

        return new ChatroomViewHolder(view);
    }

    class  ChatroomViewHolder extends RecyclerView.ViewHolder{
        TextView usernametext;
        TextView lastMessageText;
        ImageView profilepic;
        TextView lastMessageTime;
        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            usernametext=itemView.findViewById(R.id.user_name_text);
            lastMessageText=itemView.findViewById(R.id.last_message_text);
            profilepic=itemView.findViewById(R.id.profile_pic_image_view);
            lastMessageTime=itemView.findViewById(R.id.last_message_time_text);


        }
    }
}
