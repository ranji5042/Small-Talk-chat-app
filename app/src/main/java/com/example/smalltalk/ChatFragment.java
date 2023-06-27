package com.example.smalltalk;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smalltalk.adapter.RecentChatRecyclerAdapter;
import com.example.smalltalk.adapter.SearchUserRecyclerAdapter;
import com.example.smalltalk.model.Chatroom;
import com.example.smalltalk.model.UserModel;
import com.example.smalltalk.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

RecyclerView recyclerView;
RecentChatRecyclerAdapter adapter;
    public ChatFragment() {

    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_chat, container, false);
       recyclerView=view.findViewById(R.id.recycyler_view);
       setupRecyclerView();

        return view;
    }


    void setupRecyclerView(){
        Query query= FirebaseUtil.allUserCollectionReference().
                whereArrayContains("userIds",FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Chatroom> options=new FirestoreRecyclerOptions.Builder<Chatroom>().
                setQuery(query,Chatroom.class).build();
        adapter=new RecentChatRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStart() {

        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}