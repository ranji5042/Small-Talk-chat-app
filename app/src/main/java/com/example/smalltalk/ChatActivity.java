package com.example.smalltalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.smalltalk.adapter.ChatRecyclerAdapter;
import com.example.smalltalk.adapter.SearchUserRecyclerAdapter;
import com.example.smalltalk.model.ChatMessageModel;
import com.example.smalltalk.model.Chatroom;
import com.example.smalltalk.model.UserModel;
import com.example.smalltalk.utils.AndroidUtil;
import com.example.smalltalk.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
UserModel otherUser;
EditText messageInput;
ImageButton sendMessageBtn;
ImageButton backBtn;
TextView otherUsername;
RecyclerView recyclerView;
String chatroomId;
Chatroom chatroomModel;
ChatRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //get usermodel
        otherUser= AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId= FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());
        messageInput=findViewById(R.id.chat_message_input);
        sendMessageBtn=findViewById(R.id.message_send_btn);
        backBtn=findViewById(R.id.back_button);
        otherUsername=findViewById(R.id.other_username);
        recyclerView=findViewById(R.id.chat_recycler_view);
        backBtn.setOnClickListener((v)->{
            onBackPressed();  });
        otherUsername.setText(otherUser.getUsername());


        sendMessageBtn.setOnClickListener((v ->{
            String message =messageInput.getText().toString().trim();
            if(message.isEmpty())
            {return;}
            sendMessageToUser(message);
        }));
        getOrCreateChatroomModel();
        setupChatRecyclerView();

    }
    void setupChatRecyclerView(){
        Query query= FirebaseUtil.getChatroomMessageReference(chatroomId).
                orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options=new FirestoreRecyclerOptions.Builder<ChatMessageModel>().
                setQuery(query,ChatMessageModel.class).build();
        adapter=new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    void sendMessageToUser(String message){

      chatroomModel.setLastMessageTimestamp(Timestamp.now());
      chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
      chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
       ChatMessageModel chatMessageModel= new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());

       FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel).addOnCompleteListener(
               new OnCompleteListener<DocumentReference>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentReference> task) {
                       if(task.isSuccessful()){
                           messageInput.setText("");
                       }
                   }
               }
       );

    }
    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {chatroomModel=task.getResult().toObject(Chatroom.class);
                if(chatroomModel==null) {

                    //first time chat
                    chatroomModel = new Chatroom(chatroomId, Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(), "");
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }
}