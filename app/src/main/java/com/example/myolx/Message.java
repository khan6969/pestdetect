package com.example.myolx;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import model.userSpecificModel;

public class Message extends AppCompatActivity {
        RelativeLayout activity_message;
        EmojiconEditText emojiconEditText;
    private String rec_name;
    private String sen_name;
        ImageView emojiButton,submitButton;
        EmojIconActions emojIconActions;
        FirebaseAuth mAuth;
        DatabaseReference mDatabase;
      userSpecificAdapter adapter;
        RecyclerView listOfMessage;
      private LinearLayoutManager layoutManager;
    private List<userSpecificModel> messagesFromDB = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mAuth=FirebaseAuth.getInstance();
        toolbar();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        sen_name = mAuth.getCurrentUser().getUid();
        rec_name =intent.getExtras().getString("id");
         String title =intent.getExtras().getString("title");
        ((AppCompatActivity)Message.this).getSupportActionBar().setTitle(title);

        activity_message = (RelativeLayout)findViewById(R.id.message_activity);
        listOfMessage = (RecyclerView) findViewById(R.id.list_of_message);
        layoutManager = new LinearLayoutManager(this);
        listOfMessage.setHasFixedSize(true);
        listOfMessage.setLayoutManager(layoutManager);
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_message,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
//        if(FirebaseAuth.getInstance().getCurrentUser() == null)
//        {
//           // startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
//        }
//        else
//        {
//            // Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
//            //Load content
//            //displayChatMessage();
//        }

    }

    private void toolbar() {
    }

    private void sendmessage() {
        String tex = emojiconEditText.getText().toString();


        if (TextUtils.isEmpty(tex))
        {
            Toast.makeText(this, "first write your message", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Message/" + sen_name + "/" + rec_name;
            String messageReceiverRef = "Message/" + rec_name + "/" + sen_name;
                   //generate message id for sender and receiver
            DatabaseReference userMessageKeyRef = mDatabase.child("Chat")
                    .child(sen_name).child(rec_name).push();

            String messagePushID = userMessageKeyRef.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message",tex);
            messageTextBody.put("type","text");
            messageTextBody.put("from",sen_name);
            messageTextBody.put("to",rec_name);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID,messageTextBody);

            mDatabase.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {

                        Toast.makeText(Message.this, "message send successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Message.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    emojiconEditText.setText("");
                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("Message").child(sen_name).child(rec_name)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        userSpecificModel messages= snapshot.getValue(userSpecificModel.class);
                        messagesFromDB.add(messages);
                        Log.d("amjad", "rec: "+messages.getTo());
                        Log.d("amjad", "sender: "+messages.getFrom());
                        adapter = new userSpecificAdapter(messagesFromDB,getApplicationContext());
                        listOfMessage.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                           //  listOfMessage.smoothScrollToPosition(listOfMessage.getAdapter().getItemCount());

                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void showallmessages() {
       // loggedInUserName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userEmail=mAuth.getCurrentUser().getEmail();
       // Log.d("Main", "user id: " + loggedInUserName);

        //userSpecificAdapter = new userSpecificAdapter(this, ChatMessage.class, R.layout.item_in_message,
          //      FirebaseDatabase.getInstance().getReference());
      //  userSpecificAdapter adapter=new userSpecificAdapter(messagesFromDB,userEmail,Message.this);

    }
}