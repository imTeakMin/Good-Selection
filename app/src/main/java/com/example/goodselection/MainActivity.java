package com.example.goodselection;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.goodselection.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private UserAccount user;
    private ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");
        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();

        mainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=mainBinding.getRoot();
        setContentView(view);

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            mDatabaseRef.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.getValue(UserAccount.class);

                    if (user != null) {
                        mainBinding.textPoint.setText(String.valueOf(user.getPoint()));
                        mainBinding.textAccount.setText(user.getEmailId());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("FIREBASE", "데이터 로드 실패: " + error.getMessage());
                }
            });
        }

        mainBinding.btnLadder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLadderSettingActivity();
            }
        });

        mainBinding.btnRoulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRouletteSettingActivity();
            }
        });

        mainBinding.btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPurchaseActivity();
            }
        });

        mainBinding.btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStorageActivity();
            }
        });

        mainBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutActivity();
            }
        });

        mainBinding.btnMembershipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMembershipCancelActivity();
            }
        });

    }

    public void showLadderSettingActivity(){
        Intent intent=new Intent(MainActivity.this,LadderSettingActivity.class);
        startActivity(intent);
    }

    public void showRouletteSettingActivity(){
        Intent intent=new Intent(MainActivity.this,RouletteSettingActivity.class);
        startActivity(intent);
    }

    public void showPurchaseActivity(){
        Intent intent=new Intent(MainActivity.this,PurchaseActivity.class);
        startActivity(intent);
    }

    public void showStorageActivity(){
        Intent intent=new Intent(MainActivity.this,StorageActivity.class);
        startActivity(intent);
    }

    public void showLogoutActivity(){
        Intent intent=new Intent(MainActivity.this,LogoutActivity.class);
        startActivity(intent);
    }

    public void showMembershipCancelActivity(){
        Intent intent=new Intent(MainActivity.this,MembershipCancelActivity.class);
        startActivity(intent);
    }

}