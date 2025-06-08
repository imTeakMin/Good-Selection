package com.example.goodselection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.WheelItem;

import java.util.ArrayList;
import java.util.List;

public class RouletteActivity extends AppCompatActivity {

    private List<WheelItem> wheelItems;
    private LuckyWheel luckyWheel;
    private Button startButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_roulette);

        startButton=findViewById(R.id.btn_spin);;
        Button goToMain=findViewById(R.id.btn_goToMain);
        Button showAd=findViewById(R.id.btn_showAd);

        luckyWheel=findViewById(R.id.luck_wheel);

        Resources res = getResources();

        wheelItems=new ArrayList<>();
        String[] middleTexts=getIntent().getStringArrayExtra("middleTexts");

        for (int i = 0; i < middleTexts.length; i++) {
            wheelItems.add(new WheelItem(Color.LTGRAY,
                    BitmapFactory.decodeResource(res, com.bluehomestudio.luckywheel.R.drawable.ic_action_name),
                    middleTexts[i])); // 각 섹션에 텍스트를 추가 (i번째 텍스트)
        }

        // 룰렛에 아이템 추가
        luckyWheel.addWheelItems(wheelItems);

        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RouletteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdvertisementActivity();
            }
        });

        showRoulette();
    }

    public void showRoulette(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 룰렛 회전 안되게 설정 (회전하지 않도록 0으로 설정)
                luckyWheel.rotateWheelTo(0);
            }
        });
    }
    public void showAdvertisementActivity(){
        Intent intent=new Intent(RouletteActivity.this, AdvertisementActivity.class);
        startActivity(intent);
        finish();
    }

}