package com.example.goodselection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodselection.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogoutActivity extends AppCompatActivity {
    private UserAccount user;
    private ActivityLogoutBinding logoutBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        logoutBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        View view= logoutBinding.getRoot();
        setContentView(view);

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            mDatabaseRef.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.getValue(UserAccount.class);

                    if (user != null) {
                        logoutBinding.textPoint.setText(String.valueOf(user.getPoint()));
                        logoutBinding.textAccount.setText(user.getEmailId());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("FIREBASE", "데이터 로드 실패: " + error.getMessage());
                }
            });
        }

        logoutBinding.btnLogoutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });

        logoutBinding.btnLogoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogoutActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void confirmLogout(){
        Intent intent=new Intent(LogoutActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
