package com.example.goodselection;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvertisementActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserInformation");
        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();

        videoView=findViewById(R.id.videoView);
        mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.advertisement);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 영상이 끝난 후, 홈으로 가는 버튼을 표시
                Button goToMain = findViewById(R.id.btn_videoViewOut);
                goToMain.setVisibility(View.VISIBLE);

                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    mDatabaseRef.child("UserAccount").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            UserAccount user = snapshot.getValue(UserAccount.class);

                            if (user != null) {
                                user.setPoint(user.getPoint()+20);

                                DatabaseReference userRef = FirebaseDatabase.getInstance()
                                        .getReference("UserInformation")
                                        .child("UserAccount")
                                        .child(user.getIdToken()); // user.getUid()와 동일

                                userRef.setValue(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e("FIREBASE", "데이터 로드 실패: " + error.getMessage());
                        }
                    });
                }

                goToMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMainActivity();
                    }
                });

            }
        });

        playVideoView();
    }
    public void playVideoView(){
        videoView.start();
    }

    public void showMainActivity(){
        Intent intent = new Intent(AdvertisementActivity.this,MainActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }
}
