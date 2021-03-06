package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.adapter.FragmentAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference myRef = database.getReference("Message");
        myRef.setValue("Hello world");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, ""+value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        auth = FirebaseAuth.getInstance();


        //TabLayout
        binding.viewPagerId.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayoutId.setupWithViewPager(binding.viewPagerId);

    }
     @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu,menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.settingId:
                    Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(settingIntent);
                    break;

                case R.id.groupChat:
                    Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
                    startActivity(intent);
                    break;

                case R.id.logOutId:
                    auth.signOut();
                    Intent intent1 = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent1);
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
}