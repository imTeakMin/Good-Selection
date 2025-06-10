package com.example.goodselection;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodselection.databinding.ActivityStorageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StorageActivity extends AppCompatActivity {
    private UserAccount user;
    private ActivityStorageBinding storageBinding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");

        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();

        storageBinding= ActivityStorageBinding.inflate(getLayoutInflater());
        View view=storageBinding.getRoot();
        setContentView(view);

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            mDatabaseRef.child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.getValue(UserAccount.class);

                    if (user != null) {
                        storageBinding.textPoint.setText(String.valueOf(user.getPoint()));
                        storageBinding.textAccount.setText(user.getEmailId());
                    }

                    GridLayout goodsGrid = findViewById(R.id.goodsGrid);
                    goodsGrid.removeAllViews(); // 기존 항목 제거

                    ArrayList<String> goodsList = user.getGoods();

                    if(goodsList!=null){
                        int dp197 = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 197, getResources().getDisplayMetrics());
                        int dp300 = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                        int dp8 = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

                        goodsGrid.setColumnCount(2); // GridLayout column 개수 설정

                        for (String good : goodsList) {
                            int resId = getResources().getIdentifier(good, "drawable", getPackageName());

                            if (resId == 0)
                                Log.d("RESOURCE", "good: " + good + ", resId: " + resId);

                            // 1. 이미지 뷰
                            ImageView imageView = new ImageView(StorageActivity.this);
                            imageView.setImageResource(resId);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    dp300
                            ));
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // 2. 버튼
                            Button button = new Button(StorageActivity.this);
                            button.setText("사용하기");
                            button.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            button.setOnClickListener(v -> useGoods(good));

                            // 3. 아이템 컨테이너
                            LinearLayout itemLayout = new LinearLayout(StorageActivity.this);
                            itemLayout.setOrientation(LinearLayout.VERTICAL);
                            itemLayout.setLayoutParams(new GridLayout.LayoutParams(
                                    new ViewGroup.MarginLayoutParams(dp197, ViewGroup.LayoutParams.WRAP_CONTENT)
                            ));

                            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) itemLayout.getLayoutParams();
                            marginParams.setMargins(dp8, dp8, dp8, dp8);
                            itemLayout.setLayoutParams(marginParams);

                            itemLayout.addView(imageView);
                            itemLayout.addView(button);

                            // 4. 그리드에 추가
                            goodsGrid.addView(itemLayout);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(StorageActivity.this,"데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }

    }

    private void useGoods(String goods){
        user.deleteGoods(goods);

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("UserInformation")
                .child("UserAccount")
                .child(user.getIdToken());

        userRef.setValue(user);
    }
}