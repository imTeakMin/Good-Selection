package com.example.goodselection;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LadderResultActivity extends AppCompatActivity {
    private ArrayList<Player> player;
    private String[] resultArrivalTexts;
    private String[] resultDepartureTexts;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladderresult);

        LinearLayout container = findViewById(R.id.LadderResultLayout);

        Intent intent = getIntent();
        resultDepartureTexts = intent.getStringArrayExtra("departureTexts");
        resultArrivalTexts = intent.getStringArrayExtra("arrivalTexts");
        ArrayList<Horizon> resultForHorizon = (ArrayList<Horizon>) intent.getSerializableExtra("resultForHorizon");
        player=new ArrayList<>();

        // departureTexts를 player로 등록
        for(int i = 1; i<= resultDepartureTexts.length; i++){
            player.add(new Player(i,0));
        }

        // departureTexts와 대응되는 arrivalTexts 찾기
        for (Player p : player) {
            p.findLastColumn(resultForHorizon);
        }

        // 결과 화면 생성
        for (int i = 0; i < resultDepartureTexts.length; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView topText = new TextView(this);
            topText.setText(resultDepartureTexts[i]);
            topText.setTextSize(18);
            topText.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            TextView bottomText = new TextView(this);
            bottomText.setText(resultArrivalTexts[player.get(i).getCurrentColumn() - 1]);
            bottomText.setTextSize(18);
            bottomText.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            row.addView(topText);
            row.addView(bottomText);
            container.addView(row);
        }

        // 버튼 두 개 추가
        LinearLayout buttonRow = new LinearLayout(this);
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
        buttonRow.setGravity(Gravity.CENTER);
        buttonRow.setPadding(0, 50, 0, 0);
        buttonRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // 메인으로 이동 버튼 설정
        Button goToMain = new Button(this);
        goToMain.setText("메인으로 이동");
        goToMain.setBackgroundColor(Color.parseColor("#9C27B0"));
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LadderResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        goToMain.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // 광고 보기 버튼 설정
        Button showAd = new Button(this);
        showAd.setText("광고 보기");
        showAd.setBackgroundColor(Color.parseColor("#9C27B0"));
        showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdvertisementActivity();
            }
        });
        showAd.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        buttonRow.addView(goToMain);
        buttonRow.addView(showAd);

        container.addView(buttonRow);

    }
    public void showAdvertisementActivity(){
        Intent intent=new Intent(LadderResultActivity.this, AdvertisementActivity.class);
        startActivity(intent);
        finish();
    }
}
