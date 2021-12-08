package com.example.myolx;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import model.userSpecificModel;


public class userSpecificAdapter extends RecyclerView.Adapter<userSpecificAdapter.ViewHolder> {
    List<userSpecificModel> chatMessages;
    private String userEmail;
    FirebaseAuth mAuth;
    Context context;

DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Chat");

    private String fromMessageType;
    private String messageSenderId;
    private String fromUserId;

    public userSpecificAdapter(List<userSpecificModel> chatMessages, Context context) {

        this.chatMessages = chatMessages;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout,parent,false);

        mAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final  ViewHolder holder,int position) {
         messageSenderId = mAuth.getCurrentUser().getUid();

///        sen_name = mAuth.getCurrentUser().getUid();
      final  userSpecificModel messages = chatMessages.get(position);

       fromUserId = messages.getFrom();
               // String touserId = messages.getTo();
      // String mess = messages.getMessage();
         fromMessageType = messages.getType();
        Log.d("useriid", "onBindViewHolder: "+fromUserId);
       nm = FirebaseDatabase.getInstance().getReference().child("Chat").child(fromUserId);

       nm.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               if (fromMessageType.equals("text"))
               {
                   holder.receiverMessageText.setVisibility(View.GONE);
                  // holder.senderMessageText.setVisibility(View.INVISIBLE);

                   if (messageSenderId.equals(messages.getTo().toString()))
                   {
                       Log.d("hamza","sender: "+fromUserId.toString());
                       Log.d("hamza","rec: "+messages.getTo().toString());

                       holder.senderMessageText.setVisibility(View.GONE);
                       holder.receiverMessageText.setVisibility(View.VISIBLE);
                       holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                       holder.receiverMessageText.setTextColor(Color.BLACK);
                       holder.receiverMessageText.setText(messages.getMessage());



                   }
                   else
                   {
                       holder.receiverMessageText.setVisibility(View.GONE);
                       holder.senderMessageText.setVisibility(View.VISIBLE);
                       holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                       holder.senderMessageText.setTextColor(Color.BLACK);
                       holder.senderMessageText.setText(messages.getMessage());


                       //   holder.imageView.setVisibility(View.VISIBLE);

                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


       //get the message from child



    }
//
//    @Override
//    public int getItemViewType(int position) {
//        String Email=chatMessages.get(position).getMessageUser();
//
//
// // current code
//        if (Email.equals(userEmail))
//        {
//            return VIEW_SENDER;
//        }
//        else
//        {
//            return VIEW_REC;
//        }
//
//    }

    @Override
    public int getItemCount() {
   //  return chatMessages == null? 0:chatMessages.size();
     return chatMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView receiverMessageText;
       public TextView senderMessageText;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView)itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView)itemView.findViewById(R.id.receiver_message_text);
           imageView = itemView.findViewById(R.id.message_profile_image);
        }
    }
}



