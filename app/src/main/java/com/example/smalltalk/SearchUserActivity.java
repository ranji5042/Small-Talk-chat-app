package com.example.smalltalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.smalltalk.adapter.SearchUserRecyclerAdapter;
import com.example.smalltalk.model.UserModel;
import com.example.smalltalk.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backbutton;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchButton=findViewById(R.id.search_user_btn);
        searchInput=findViewById(R.id.search_user_name_input);
        backbutton=findViewById(R.id.back_button);
        recyclerView=findViewById(R.id.search_user_recycler_view);
        searchInput.requestFocus();
        backbutton.setOnClickListener(v ->{
            onBackPressed();
        });
        searchButton.setOnClickListener(v ->{
            String searchTerm=searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3)
            {
                searchInput.setError("Invalid Username");
                return;
            }
            setupSearchReCyclerView(searchTerm);
        });

    }
    void setupSearchReCyclerView(String searchTerm){
        Query query= FirebaseUtil.allUserCollectionReference().whereGreaterThanOrEqualTo("username",searchTerm);
        FirestoreRecyclerOptions<UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>().
                setQuery(query,UserModel.class).build();
        adapter=new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStart() {

        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}