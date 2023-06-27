package com.example.smalltalk.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static  boolean isLoggedIn(){
        if(currentUserId()!=null){
            return  true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
public  static  DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
}
public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
}
public static String getChatroomId(String userId1,String userId2)
{
    if(userId1.hashCode()<userId2.hashCode()){
    return userId1+"_"+userId2;
    }
    else{
        return userId2+"_"+userId1;

    }
}
public  static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
}
public static  DocumentReference getOtherUserFromChatroom(List<String> userIds)
{if(userIds.get(0).equals(FirebaseUtil.currentUserId())){
   return allUserCollectionReference().document(userIds.get(1));
}
else{
    return allUserCollectionReference().document(userIds.get(0));
}

}
}