package com.example.goodselection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodselection.databinding.ActivityMembershipcancelBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MembershipCancelActivity extends AppCompatActivity {
    private UserAccount user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        ActivityMembershipcancelBinding mBinding= ActivityMembershipcancelBinding.inflate(getLayoutInflater());
        View view=mBinding.getRoot();
        setContentView(view);

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            mDatabaseRef.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.getValue(UserAccount.class);

                    if (user != null) {
                        mBinding.textPoint.setText(String.valueOf(user.getPoint()));
                        mBinding.textAccount.setText(user.getEmailId());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("FIREBASE", "데이터 로드 실패: " + error.getMessage());
                }
            });
        }

        mBinding.btnMembershipCancelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    deleteMembership(user);
                }
            }
        });

        mBinding.btnMembershipCancelNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainActivity();
            }
        });
    }

    public void deleteMembership(FirebaseUser user){
        String uid=user.getUid();

        FirebaseDatabase.getInstance().getReference("UserInformation").child("UserAccount").child(uid).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 데이터 삭제 성공 후 계정 삭제
                        user.delete().addOnCompleteListener(deleteTask -> {

                            if (deleteTask.isSuccessful()) {
                                // 계정 삭제 성공
                                Intent intent = new Intent(MembershipCancelActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 백스택 지우기
                                startActivity(intent);
                                finish(); // 뒤로가기 버튼으로 못 돌아오도록 설정
                            } else {
                                Toast.makeText(MembershipCancelActivity.this, "계정 탈퇴 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(MembershipCancelActivity.this, "사용자 데이터 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showMainActivity(){
        Intent intent=new Intent(MembershipCancelActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
