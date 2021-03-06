package com.example.myapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<Users> list;
    private Context context;
    private ProgressDialog progressDialog;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        progressDialog = new ProgressDialog(context);

        Users users = list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.profile).into(holder.imageView);
        holder.userName.setText(users.getUserName());

FirebaseDatabase.getInstance().getReference().child("chats")
        .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
        .orderByChild("timeStamp").limitToLast(1)
         .addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if (snapshot.hasChildren())
        {
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                holder.lastMessage.setText(dataSnapshot.child("message").getValue().toString());
            }
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilepic());
                intent.putExtra("userName", users.getUserName());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameId);
            lastMessage = itemView.findViewById(R.id.lastMessageId);
        }
    }
}
