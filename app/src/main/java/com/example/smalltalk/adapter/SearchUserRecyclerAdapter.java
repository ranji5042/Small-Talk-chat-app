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
import com.example.smalltalk.model.UserModel;
import com.example.smalltalk.utils.AndroidUtil;
import com.example.smalltalk.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.UserModelViewHolder> {
    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernametext.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if(model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.usernametext.setText(model.getUsername()+"(You)");

        }
        holder.itemView.setOnClickListener(v -> {
            //to chat
            Intent intent=new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);

        return new UserModelViewHolder(view);
    }

    class  UserModelViewHolder extends RecyclerView.ViewHolder{
TextView usernametext;
TextView phoneText;
ImageView profilepic;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernametext=itemView.findViewById(R.id.user_name_text);
            phoneText=itemView.findViewById(R.id.phone_text);
            profilepic=itemView.findViewById(R.id.profile_pic_image_view);

        }
    }
}
