package com.example.goodselection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.goodselection.databinding.ActivityPurchaseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PurchaseActivity extends AppCompatActivity {
    private UserAccount user;
    private ActivityPurchaseBinding purchaseBinding;
    private int[] goodsPrice={500,2000,3500,5000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        purchaseBinding=ActivityPurchaseBinding.inflate(getLayoutInflater());
        View view=purchaseBinding.getRoot();
        setContentView(view);

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            mDatabaseRef.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.getValue(UserAccount.class);

                    if (user != null) {
                        purchaseBinding.textPoint.setText(String.valueOf(user.getPoint()));
                        purchaseBinding.textAccount.setText(user.getEmailId());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("FIREBASE", "데이터 로드 실패: " + error.getMessage());
                }
            });
        }

        buyGoods();
    }

    // 버튼 클릭 이벤트 처리
    private void addPurchaseButtonClickEvent(int buttonNumber) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("UserInformation")
                .child("UserAccount")
                .child(user.getIdToken()); // user.getUid()와 동일

        switch (buttonNumber) {
            case 1:
                if(user.getPoint()>goodsPrice[0])
                {
                    user.addGoods("candy");
                    user.setPoint(user.getPoint()-500);
                    userRef.setValue(user);
                }
                break;
            case 2:
                if(user.getPoint()>goodsPrice[1])
                {
                    user.addGoods("pepero");
                    user.setPoint(user.getPoint()-2000);
                    userRef.setValue(user);
                }
                break;
            case 3:
                if(user.getPoint()>goodsPrice[2])
                {
                    user.addGoods("homerunball");
                    user.setPoint(user.getPoint()-3500);
                    userRef.setValue(user);
                }
                break;
            case 4:
                if(user.getPoint()>goodsPrice[3])
                {
                    user.addGoods("honeychip");
                    user.setPoint(user.getPoint()-5000);
                    userRef.setValue(user);
                }
                break;
            default:
                break;
        }
    }

    public void buyGoods(){
        purchaseBinding.purchaseButton1.setOnClickListener(v -> addPurchaseButtonClickEvent(1));
        purchaseBinding.purchaseButton2.setOnClickListener(v -> addPurchaseButtonClickEvent(2));
        purchaseBinding.purchaseButton3.setOnClickListener(v -> addPurchaseButtonClickEvent(3));
        purchaseBinding.purchaseButton4.setOnClickListener(v -> addPurchaseButtonClickEvent(4));
    }

}
