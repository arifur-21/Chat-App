package com.example.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserAdapter;
import com.example.myapplication.databinding.FragmentChatsBinding;
import com.example.myapplication.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.ProtectionDomain;
import java.util.ArrayList;

public class ChatsFragment extends Fragment {

   private FragmentChatsBinding binding;
    private ArrayList<Users> list = new ArrayList<>();
    private FirebaseDatabase database;
    private ProgressDialog dialog;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading data Please wait.....");

        dialog.show();
        UserAdapter adapter = new UserAdapter(list, getContext());
        binding.chatRecycleViewId.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecycleViewId.setLayoutManager(layoutManager);



        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                 Users users = dataSnapshot.getValue(Users.class);
                 users.setUserId(dataSnapshot.getKey());

                 if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                     list.add(users);
                 }
                }
               dialog.dismiss();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        return binding.getRoot();
    }
}