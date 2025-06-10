package com.example.goodselection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LadderSettingActivity extends AppCompatActivity {

    private EditText columnCntInputEditText;
    private Button checkColumnCntButton;
    private LinearLayout departureLayout, arrivalLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laddersetting);

        columnCntInputEditText = findViewById(R.id.text_ladderCount);
        checkColumnCntButton = findViewById(R.id.btn_checkLadderCount);
        departureLayout = findViewById(R.id.layout_top);
        arrivalLayout = findViewById(R.id.layout_bottom);

        checkColumnCntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = columnCntInputEditText.getText().toString();
                if (!input.isEmpty()) {
                    int numColumns = Integer.parseInt(input);
                    if(numColumns>6){
                        Toast.makeText(LadderSettingActivity.this, "최대 6개 가능", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        addEditTexts(numColumns);
                    }
                }
            }
        });
    }

    private void addEditTexts(int numColumns) {
        // 기존에 추가된 EditText들을 삭제
        departureLayout.removeAllViews();
        arrivalLayout.removeAllViews();

        // 사용자가 입력한 개수만큼 EditText를 추가
        for (int i = 0; i < numColumns; i++) {
            // 상단 EditText (시작 부분)
            EditText topEditText = new EditText(this);
            topEditText.setHint("시작 " + (i + 1));
            departureLayout.addView(topEditText);

            // 하단 EditText (도착 부분)
            EditText bottomEditText = new EditText(this);
            bottomEditText.setHint("도착 " + (i + 1));
            arrivalLayout.addView(bottomEditText);
        }

        // "게임 시작" 버튼을 생성하고, 하단에 추가
        Button startButton = new Button(this);
        startButton.setText("게임 시작");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 모든 EditText가 입력되었는지 확인
                boolean allFilled = checkIfAllEditTextsFilled();

                String[] departureTexts = new String[numColumns];
                String[] arrivalTexts = new String[numColumns];

                for (int i = 0; i < numColumns; i++) {
                    EditText topEditText = (EditText) departureLayout.getChildAt(i);
                    EditText bottomEditText = (EditText) arrivalLayout.getChildAt(i);

                    departureTexts[i] = topEditText.getText().toString();
                    arrivalTexts[i] = bottomEditText.getText().toString();
                }

                if (allFilled) {
                    showLadderActivity(departureTexts,arrivalTexts);
                } else {
                    makeLadderSettingErrorMsg();
                }
            }
        });

        // "게임 시작" 버튼을 하단에 추가
        arrivalLayout.addView(startButton);
    }

    private boolean checkIfAllEditTextsFilled() {
        // 상단과 하단의 EditText들이 모두 입력되었는지 확인
        for (int i = 0; i < departureLayout.getChildCount(); i++) {
            EditText topEditText = (EditText) departureLayout.getChildAt(i);
            EditText bottomEditText = (EditText) arrivalLayout.getChildAt(i);
            if (topEditText.getText().toString().isEmpty() || bottomEditText.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void showLadderActivity(String[] departureTexts, String[] arrivalTexts){
        Intent intent = new Intent(LadderSettingActivity.this, LadderActivity.class);
        intent.putExtra("departureTexts", departureTexts);
        intent.putExtra("arrivalTexts", arrivalTexts);
        startActivity(intent);
    }

    private void makeLadderSettingErrorMsg(){
        Toast.makeText(LadderSettingActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
    }
}
